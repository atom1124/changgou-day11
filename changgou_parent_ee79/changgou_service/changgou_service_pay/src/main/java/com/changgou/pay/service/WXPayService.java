package com.changgou.pay.service;

import java.util.Map;

/**
 * 微信支付接口
 */
public interface WXPayService {

    //微信支付接口
    Map createNative(String out_trade_no,String total_fee);

    //支付状态查询
    Map queryPayStatus(String out_trade_no);

    //关闭支付
    Map<String,String> closePay(String orderId);

}
