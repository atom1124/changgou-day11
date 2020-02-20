package com.changgou.pay.service.impl;

import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付业务实现类
 */
@Service
public class WXPayServiceImpl implements WXPayService {


    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.notifyurl}")
    private String notifyurl;
    @Value("${weixin.partnerkey}")
    private String partnerkey;


    @Override
    public Map createNative(String out_trade_no, String total_fee) {

        Map map = new HashMap<>();

        try {
            //1、包装微信接口需要的参数
            Map param = new HashMap();
            param.put("appid", appid);  //公众号ID
            param.put("mch_id", partner);  //商户号
            param.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            param.put("body", "畅购");  //商品描述，扫码后用户看到的商品信息
            param.put("out_trade_no", out_trade_no); //订单号
            param.put("total_fee", total_fee);  //订单总金额，单位为分
            param.put("spbill_create_ip", "127.0.0.1");  //终端IP，只要附合ip地址规范，可以随意写
            param.put("notify_url", notifyurl);  //回调地址
            param.put("trade_type", "NATIVE");  //交易类型，NATIVE 扫码支付
            //2、生成xml，通过httpClient发送请求得到数据
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("正在调起统一下单接口，请求参数:" + xmlParam);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //3、解析结果
            String xmlResult = httpClient.getContent();
            System.out.println("调起统一下单接口成功，返回结果：" + xmlResult);
            //xml转成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlResult);
            map.put("code_url", resultMap.get("code_url"));//支付地址
            map.put("total_fee", total_fee);//总金额
            map.put("out_trade_no", out_trade_no);//订单号

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 支付信息状态查询
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {


        try {
            //1、包装微信接口需要的参数
            Map param = new HashMap();
            param.put("appid", appid);  //公众号ID
            param.put("mch_id", partner);  //商户号
            param.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            param.put("out_trade_no", out_trade_no); //订单号
            //2、生成xml，通过httpClient发送请求得到数据
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("正在调起查询订单接口，请求参数:" + xmlParam);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //3、解析结果
            String xmlResult = httpClient.getContent();
            System.out.println("调起查询订单接口成功，返回结果：" + xmlResult);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlResult);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 关闭支付
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closePay(String orderId) {
        try {
            //参数设置
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("appid", appid); //应用ID
            paramMap.put("mch_id", partner);    //商户编号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符
            paramMap.put("out_trade_no", String.valueOf(orderId));   //商家的唯一编号
            //将Map数据转成XML字符
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //确定url
            String url = "https://api.mch.weixin.qq.com/pay/closeorder";
            //发送请求
            HttpClient httpClient = new HttpClient(url);
            //https
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlParam);
            //提交
            httpClient.post();
            //获取返回数据
            String content = httpClient.getContent();

            //将返回数据解析成Map
            return WXPayUtil.xmlToMap(content);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
