# 高并发秒杀系统

## DAO层设计

### 1.bean实体类的设计

Product:秒杀商品信息

```java
    private long id;//商品id
    private String title;//商品名称
    private String image;//商品图片
    private BigDecimal price;//商品原价格
    private BigDecimal costPrice;//商品秒杀价格

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;//秒杀创建时间

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private Date startTime;//秒杀开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private Date endTime;//秒杀结束时间

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private long stockCount;//库存量
```

Order:订单信息

```java
    private long id;//秒杀商品id
    private BigDecimal money;//付款
    private  long userPhone;//用户手机号


    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;//订单创建时间

    private boolean status;//订单状态-1:无效 0:成功 1:已付款

    private Product product;//秒杀商品， 和订单是一对多的关系，保证多个人都可以同时下单，但是只有一个人可以付款买到
```

实体类都实现Serializable接口，可直接使用Java内置序列化工具

### 2.表的设计

订单表的设计：

![1566443900866](C:\Users\morty\AppData\Roaming\Typora\typora-user-images\1566443900866.png)

商品表的设计：

![1566443948593](C:\Users\morty\AppData\Roaming\Typora\typora-user-images\1566443948593.png)

### 3.DAO层开发

秒杀业务流程：

商家与库存：添加、修改、发货和核账

用户与库存：秒杀、下单、付款和退货

就本项目而言，数据库操作主要是减库存和生成订单

减库存：

顾名思义就是减少当前被秒杀到的商品的库存数量，这也是秒杀系统中一个处理难点的地方。实现减库存即count-1，但是我们需要考虑Mysql的事务特性引发的种种问题、需要考虑如何避免同一用户重复秒杀的行为。

生成订单：

记录购买用户的姓名、手机号、购买的商品ID等。因为本项目中不涉及支付功能，所以记录用户的购买订单的业务并不复杂。

### 4.注意

因为我们必须要避免同一个用户多次抢购同一件商品，在SQL中必须限制这一点（因为即使前端怎么控制都无法避免用户多次请求同一个接口，所谓接口防刷）。所以在设计订单表的时候用了联合主键且不自增的方式，以用户ID和用户电话组成联合主键，这样当同一个用户（电话相同）多次抢购同一件商品时插入的SQL就会产生主键冲突的问题，这样就会报错。为了避免系统不直接报错设计了`ignore`实现主键冲突就直接返回0表示该条SQL执行失败。

## Service层设计

### 1.接口设计

```java
public interface SeckillService {

    /**
     * 获取所有的秒杀商品列表
     */
    List<Seckill> findAll();

    /**
     * 获取某一条商品秒杀信息
     *
     * @param seckillId
     * @return
     */
    Seckill findById(long seckillId);

    /**
     * 秒杀开始时输出暴露秒杀的地址
     * 否者输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀的操作
     *
     * @param seckillId
     * @param userPhone
     * @param money
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, BigDecimal money, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}
```

exportSeckillUrl：**暴露接口**用到的方法，目的就是**获取秒杀商品抢购的地址**

1.为什么要单独创建一个方法来获取秒杀地址？

在之前我们做的后端项目中，跳转到某个详情页一般都是：根据ID查询该详情数据，然后将页面跳转到详情页并将数据直接渲染到页面上。但是秒杀系统不同，它也不能就这样简单的定义，要知道秒杀技术的难点就是如何应对高并发？同一件商品，比如瞬间有十万的用户访问，而还存在各种黄牛，有各种工具去抢购这个商品，那么此时肯定不止10万的访问量的，并且开发者要尽量的保证每个用户抢购的公平性，也就是不能让一个用户抢购一堆数量的此商品。这就是我们常说的**接口防刷**问题。因此单独定义一个获取秒杀接口的方法是有必要的。

2.如何做到接口防刷？

接口方法：`Exposer exportSeckillUrl(long seckillId);`从参数列表中很易明白：就是根据该商品的ID获取到这个商品的秒杀url地址；但是返回值类型`Exposer`是什么呢？

思考一下如何做到**接口防刷？**

1. 首先要保证该商品处于秒杀状态。也就是秒杀开始时间要<当前时间&&秒杀截止时间要>当前时间。
2. 要保证一个用户只能抢购到一件该商品，应做到商品秒杀接口对应同一用户只能有唯一的一个URL秒杀地址，不同用户间秒杀地址应是不同的，且配合订单表`seckill_order`中*联合主键*的配置实现。

针对上面的两条分析，我们给出`Exposer`的设计（要注意此类定义在`/dto/`路径下表明此类是我们手动封装的结果属性，它类似JavaBean但又不属于，仅用来封装秒杀状态的结果，目的是提高代码的重用率）：

executeSeckill：执行秒杀的操作，将这两个操作减库存和记录购买明细合并为一个接口方法

返回一个执行结果的类型

### 2.异常设计

减库存操作和插入购买明细操作都会产生很多未知异常（RuntimeException），比如秒杀结束、重复秒杀等。除了要返回这些异常信息，还有一个非常重要的操作就是捕获这些RuntimeException，从而避免系统直接报错。定义了秒杀关闭异常、秒杀重复异常以及一个通用异常作为父类。

## Web层设计





## 并发优化













