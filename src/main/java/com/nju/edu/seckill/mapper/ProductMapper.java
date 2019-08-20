package com.nju.edu.seckill.mapper;

import com.nju.edu.seckill.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * ProductMapper
 *
 * @blame gao xiang
 */
@Component
@Mapper
public interface ProductMapper {

    /**
     * 查询所有商品列表
     * @return
     */
    List<Product> findAll();

    /**
     * 按id查询
     * @param id
     * @return
     */
    Product findById(long id);

    /**
     *减库存
     * @param id 秒杀商品id
     * @param killTime 秒杀时间
     * @return
     */
    int reduceStock(@Param("id") long id, @Param("killTime") Date killTime);
}
