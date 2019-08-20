package com.nju.edu.seckill;

import com.nju.edu.seckill.bean.Product;
import com.nju.edu.seckill.mapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillApplicationTests {

    @Autowired
    ProductMapper productMapper;

    @Test
    public void test(){
        List<Product> list = productMapper.findAll();
        System.out.println(list);
    }

}
