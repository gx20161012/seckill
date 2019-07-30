package com.nju.edu.seckill.service;

import com.nju.edu.seckill.domain.User;
import com.nju.edu.seckill.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public User getById(Integer id){
        return userMapper.getById(id);
    }

    public boolean tx(){
        User user1 = new User();
        user1.setId(2);
        user1.setName("qi");
        userMapper.insert(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setName("qi");
        userMapper.insert(user2);
        return true;
    }
}
