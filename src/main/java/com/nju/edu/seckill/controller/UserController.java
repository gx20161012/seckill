package com.nju.edu.seckill.controller;

import com.nju.edu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    @Autowired
    UserService userService;

    @RequestMapping("/user/{id}")
    @ResponseBody
    public String getById(@PathVariable("id") Integer id){
        return userService.getById(id).toString();
    }

}
