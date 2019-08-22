package com.nju.edu.seckill.common;

import com.nju.edu.seckill.bean.Order;
import com.nju.edu.seckill.enums.SeckillStatEnum;

/**
 * SeckillExecution
 * 秒杀执行结果类型
 * @blame gao xiang
 */
public class SeckillExecution {

    private Long id;

    //秒杀执行结果状态
    private int state;

    //状态表示
    private String stateInfo;

    //秒杀成功的订单对象
    private Order order;

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "id=" + id +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", order=" + order +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SeckillExecution(Long id, SeckillStatEnum seckillStatEnum, Order order) {
        this.id = id;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.order = order;
    }

    public SeckillExecution(Long id, SeckillStatEnum seckillStatEnum) {
        this.id = id;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }
}
