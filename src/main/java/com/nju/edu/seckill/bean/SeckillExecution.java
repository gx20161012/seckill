package com.nju.edu.seckill.bean;

import com.nju.edu.seckill.enums.SeckillStatEnum;

/**
 * SeckillExecution
 *
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

    public SeckillExecution(Long id, SeckillStatEnum seckillStatEnum, Order order) {
        this.id = id;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.order = order;
    }

    public SeckillExecution(Long id, int state, String stateInfo) {
        this.id = id;
        this.state = state;
        this.stateInfo = stateInfo;
    }
}
