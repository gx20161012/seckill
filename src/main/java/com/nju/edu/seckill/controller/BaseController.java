package com.nju.edu.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    /**
     * 跳转到秒杀商品页
     *
     * @return
     */
    @RequestMapping("/seckill")
    public String seckillGoods() {
        return "redirect:/seckill/list";
    }

}
