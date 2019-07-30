package com.nju.edu.seckill.controller;

import com.nju.edu.seckill.domain.User;
import com.nju.edu.seckill.result.Result;
import com.nju.edu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    @Autowired
    UserService userService;

    @RequestMapping("/user/{id}")
    @ResponseBody
    public Result<User> getById(@PathVariable("id") Integer id){
        return Result.success(userService.getById(id));
    }
    @ResponseBody
    @RequestMapping("/user/tx")
    public Result<Boolean> f(){
        userService.tx();
        return Result.success(true);
    }
}
