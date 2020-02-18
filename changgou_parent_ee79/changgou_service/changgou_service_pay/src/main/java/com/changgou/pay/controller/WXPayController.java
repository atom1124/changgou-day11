package com.changgou.pay.controller;


import com.changgou.pay.service.WXPayService;
import com.sun.org.apache.regexp.internal.RE;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "weixin/pay")
@CrossOrigin
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    /**
     * 创建二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @RequestMapping("create/native")
    public Result createNative(String  out_trade_no, String total_fee){
        //调用业务层获取二维码链接
        Map<String,String> resultMap = wxPayService.createNative(out_trade_no, total_fee);

        return new Result(true, StatusCode.OK,"获取二维码链接成功!",resultMap);



    }


}
