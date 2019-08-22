package com.nju.edu.seckill.service;

import com.nju.edu.seckill.bean.Exposer;
import com.nju.edu.seckill.bean.Product;
import com.nju.edu.seckill.bean.SeckillExecution;
import com.nju.edu.seckill.exception.RepeatKillException;
import com.nju.edu.seckill.exception.SeckillCloseException;
import com.nju.edu.seckill.exception.SeckillException;

import java.math.BigDecimal;
import java.util.List;

/**
 * SeckillService
 *
 * @blame gao xiang
 */
public interface SeckillService {

    /**
     * 获取全部商品列表
     * @return
     */
    List<Product> findAll();

    /**
     * 获取单个商品
     * @param id
     * @return
     */
    Product findById(long id);

    /**
     * 暴露秒杀商品地址，否则输出系统时间和秒杀时间
     * @param id
     * @return
     */
    Exposer exportSeckillUrl(long id);


    SeckillExecution executeSeckill(long id, BigDecimal money, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;


}
