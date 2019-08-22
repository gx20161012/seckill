package com.nju.edu.seckill.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Order
 *
 * @blame gao xiang
 */
public class Order implements Serializable {

    /**
     * 秒杀商品id
     */
    private long id;
    /**
     * 付款
     */
    private BigDecimal money;
    private  long userPhone;//用户手机号


    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;//订单创建时间

    private boolean status;//订单状态-1:无效 0:成功 1:已付款

    private Product product;//秒杀商品， 和订单是一对多的关系，保证多个人都可以同时下单，但是只有一个人可以付款买到
}
