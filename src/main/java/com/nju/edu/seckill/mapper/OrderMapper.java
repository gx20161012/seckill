package com.nju.edu.seckill.mapper;

import com.nju.edu.seckill.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * OrderMapper
 *
 * @blame gao xiang
 */
@Component
@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param id
     * @param money
     * @param userPhone
     * @return
     */
    int insertOrder(@Param("id") long id, @Param("money") BigDecimal money, @Param("userPhone") long userPhone);

    /**
     * 按id查询订单数据
     * @param id
     * @param userPhone
     * @return
     */
    Order findById(@Param("id") long id, @Param("userPhone") long userPhone);

}
