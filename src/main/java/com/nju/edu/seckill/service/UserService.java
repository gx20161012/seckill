package com.nju.edu.seckill.service;

import com.nju.edu.seckill.domain.User;
import com.nju.edu.seckill.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public User getById(Integer id){
        return userMapper.getById(id);
    }
}
