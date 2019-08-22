package com.nju.edu.seckill.service.impl;

import com.nju.edu.seckill.common.Exposer;
import com.nju.edu.seckill.bean.Order;
import com.nju.edu.seckill.bean.Product;
import com.nju.edu.seckill.common.SeckillExecution;
import com.nju.edu.seckill.enums.SeckillStatEnum;
import com.nju.edu.seckill.exception.RepeatKillException;
import com.nju.edu.seckill.exception.SeckillCloseException;
import com.nju.edu.seckill.exception.SeckillException;
import com.nju.edu.seckill.mapper.OrderMapper;
import com.nju.edu.seckill.mapper.ProductMapper;
import com.nju.edu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * SeckillServiceImpl
 *
 * @blame gao xiang
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String salt = "sjajaspu-i-2jrfm;sd";

    private final String key = "product";

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Product> findAll() {
        List<Product> products = redisTemplate.boundHashOps("product").values();
        if(products == null || products.size() == 0){
            products = productMapper.findAll();
            for(Product product : products){
                redisTemplate.boundHashOps(key).put(product.getId(), product);
                logger.info("findAll -> 从数据库中读取放入缓存中");
            }
        }else{
            logger.info("findAll -> 从缓存中读取");
        }
        return products;
    }

    @Override
    public Product findById(long id) {
        return productMapper.findById(id);
    }

    @Override
    public Exposer exportSeckillUrl(long id) {
        Product product = (Product) redisTemplate.boundHashOps(key).get(id);
        if(product == null){
            product = productMapper.findById(id);
            if(product == null){
                return new Exposer(false, id);
            }else{
                redisTemplate.boundHashOps(key).put(id, product);
                logger.info("exportSeckillUrl -> 从数据库中读取放入缓存中");
            }
        }else{
            logger.info("exportSeckillUrl -> 从缓存中读取");
        }
        Date startTime = product.getStartTime();
        Date endTime = product.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, id, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 =getMD5(id);
        return new Exposer(true, md5, id);
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     * <p>
     * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long id, BigDecimal money, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(id))){
            throw new SeckillException("秒杀数据rewrite");
        }
        Date nowTime = new Date();
        try {
            int insertCount = orderMapper.insertOrder(id, money, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存
                int updateCount = productMapper.reduceStock(id, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    Order order = orderMapper.findById(id, userPhone);

                    //更新缓存（更新库存数量）
                    Product product = (Product) redisTemplate.boundHashOps(key).get(id);
                    product.setStockCount(product.getStockCount() - 1);
                    redisTemplate.boundHashOps(key).put(id, product);

                    return new SeckillExecution(id, SeckillStatEnum.SUCCESS, order);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
