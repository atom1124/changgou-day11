package com.changgou.pay.controller;

import com.changgou.pay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * @author zhaoliancan
 * @description 支付控制类
 * @create 2019-08-08 18:52
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    AlipayService alipayService;


    @RequestMapping("/pay")
    public void payMent(HttpServletResponse response, HttpServletRequest request,String total_amount,String subject) {
        try {
            alipayService.aliPay(response,request,total_amount,subject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
