package com.nju.edu.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    
    @Autowired
    RedisTemplate redisTemplate;

    public String get(String key){
        Object o = redisTemplate.opsForValue().get(key);
        return o.toString();
    }
}
