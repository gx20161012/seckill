package com.nju.edu.seckill.controller;

import com.nju.edu.seckill.bean.Product;
import com.nju.edu.seckill.common.Exposer;
import com.nju.edu.seckill.common.SeckillExecution;
import com.nju.edu.seckill.common.SeckillResult;
import com.nju.edu.seckill.enums.SeckillStatEnum;
import com.nju.edu.seckill.exception.RepeatKillException;
import com.nju.edu.seckill.exception.SeckillCloseException;
import com.nju.edu.seckill.exception.SeckillException;
import com.nju.edu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/list")
    public String findAll(Model model){
        List<Product> list = seckillService.findAll();
        model.addAttribute("list", list);
        return "page/seckill";
    }

    @ResponseBody
    @RequestMapping("/findById")
    public Product findById(@RequestParam("id") Long id){
        return seckillService.findById(id);
    }

    @RequestMapping("/{id}/detail")
    public String detail(@PathVariable("id") Long id, Model model){
        if(id == null){
            return "page/seckill";
        }
        Product product = seckillService.findById(id);
        model.addAttribute("product", product);
        if(product == null)
            return "page/seckill";
        else
            return "page/seckill_detail";
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("id") Long id){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(id);
            result = new SeckillResult<>(true, exposer);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{id}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execution(@PathVariable("id") Long id,
                                                     @PathVariable("md5") String md5,
                                                     @RequestParam("money")BigDecimal money,
                                                     @CookieValue(value = "killPhone", required = false) Long userPhone){
        if(userPhone == null){
            return new SeckillResult<>(false, "未注册");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, money, userPhone, md5);
            return new SeckillResult<>(true, seckillExecution);
        }catch (SeckillCloseException e){
            SeckillExecution seckillExecution = new SeckillExecution(id, SeckillStatEnum.END);
            return new SeckillResult<>(true, seckillExecution);
        }catch (RepeatKillException e){
            SeckillExecution seckillExecution = new SeckillExecution(id, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        }catch (SeckillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(id, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/time/now")
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
