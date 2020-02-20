package com.changgou.pay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AlipayService {

   /**
    * 支付宝支付调用接口
    * @param response
    * @param request
    * @throws IOException
    */

   void aliPay(HttpServletResponse response, HttpServletRequest request, String total_amount, String subject) throws Exception;
}

