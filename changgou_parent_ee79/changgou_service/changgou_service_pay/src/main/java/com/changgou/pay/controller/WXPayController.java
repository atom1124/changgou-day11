package com.changgou.pay.controller;


import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "weixin/pay")
@CrossOrigin
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;


    /**
     * 创建二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @RequestMapping("create/native")
    public Result createNative(String out_trade_no, String total_fee) {
        //调用业务层获取二维码链接
        Map<String, String> resultMap = wxPayService.createNative(out_trade_no, total_fee);

        return new Result(true, StatusCode.OK, "获取二维码链接成功!", resultMap);
    }

    /**
     * 支付状态查询
     * @param out_trade_no
     * @return
     */
    @RequestMapping("status/query")
    public Result queryStatus(String out_trade_no) {

        Map resultMap = wxPayService.queryPayStatus(out_trade_no);

        return new Result(true, StatusCode.OK, "支付状态查询成功!", resultMap);

    }

    /**
     * 支付信息回调
     * @param request
     * @return
     */
    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) {

        try {
            //1.读取字符回调数据
            InputStream inputStream = request.getInputStream();
            //2.使用Apache IOUtils把输入转换成字符
            String result = IOUtils.toString(inputStream, "UTF-8");
            //3.将xml字符串转换成Map结构
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println("支付回调的信息:" + map);

            //发送信息
            rabbitTemplate.convertAndSend(exchange,routing,JSON.toJSONString(map));


            //4.包装响应数据设置
            Map respMap = new HashMap();
            respMap.put("return_code", "SUCCESS");
            respMap.put("return_msg", "OK");
            return WXPayUtil.mapToXml(respMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭支付
     * @param orderId
     * @return
     */
    @RequestMapping("/closePay/{orderId}")
    public Result closePay(@PathVariable String orderId){
        try {
            Map<String, String> stringStringMap = wxPayService.closePay(orderId);
            return new Result(true,StatusCode.OK,"关闭成功",stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,StatusCode.ERROR,"未知错误");
    }




}