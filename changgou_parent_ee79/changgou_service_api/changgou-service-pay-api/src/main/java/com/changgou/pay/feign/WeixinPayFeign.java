package com.changgou.pay.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Li
 * @version 1.0
 * @date 2020/1/4 10:14
 */
@FeignClient(name="pay")
@RequestMapping(value = "/weixin/pay")
public interface WeixinPayFeign {

    /**
     * 关闭支付
     * @param orderId
     * @return
     */
    @RequestMapping("/closePay")
    public Result closePay(Long orderId);


    /**
     * 支付状态查询
     * @param out_trade_no
     * @return
     */
    @RequestMapping("status/query")
    public Result queryStatus(String out_trade_no);
}
