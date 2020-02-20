package com.changgou.pay.config;



/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;
@Configuration
public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101900722674";

	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWyaH9Eu5ebYX2cgl5iIKyBTuuUTpLiFMUSZIOCDiUG/3Cgkvbk7BI/WM3tpb1g0koMAqhw/Ns4btVLYXYOGCDeeIC96RYVMcQgSOfZZPB+xLs97EhuMngnQndJzl+lOg6X9KfEOq9NeuxYM8RCyOOXm6u3zG+aQdu5RrbROwD5iVHPcOVX+BLQTnB/Xaoeqm51en6juUgbZwW8I/8j8OQqXkAPKYL2BDK26zVpp4EL3hlbPhDBbK784Aw4XPuoB0+sJIR9OrBFSJxyXPbA49vIl7w1GxvvofvKMDPquqsmN0QbgmpixExw47914XiDHTaoi530SqdRuv6GEqS299bAgMBAAECggEBAJQfkJRlk2mJV26nyWzsz86G24C65HQsf8hPGT3Vj5bXBGmdy1zw8NxSpbOmFxcol6X1w4vzTN5pxLfA11l20EB3a7EUigoHheFNqqu7gfGtbYMBMqz4tL+EJJtuG8mgB5zIYsPFP4MNQ/HkMqUAdzY0x6/bKydsT1P9lWtOgja0x1v8KmgoIEuiuFZnmw5k+WxClOLfRDG4GuZyBQDiLiRx2y2i7LHl7LP0tRyESbka+nQ8NfyUr1+Yk0voJ3/WsNAHDlXUi/HT+mPSA80HODpJLGFdI61nFFZvIOZrLE7Y1hY9LQ3atQyYur5gLWC5MqMYhTZRnobCY1RJM5kCweECgYEA6nySwgLl0KF/nNLIdyIClkXptIk2zFA1l158wmWn6XoLZhUlr7f9od/P7q8wJhvnwyB183mTb+IHdQPjfuTwXXTDC145naSzCWUIOsFfAV+F66BM4lGwTKOzPyEZqiPaZVmxp0r4rQjvSJxY350zPD876M2yoXJo0418d/EypJECgYEApJ81Dx1cN1M/n3eHKWe/MhAHvLY+VlM83P9faPlENn2bitqmkACRZ0GJOjbzlykjIWVvGUU7CU6RRxAsQeMDx+v3f2fejq1WTIGDg5U6E5KDq6efpQUZsFXqTDBBTn3N4XB9pYgVPaJo/CPbJpPXqTHzidBILoroPClzxsHICysCgYA2THKPiqw9C6jJX+Yb8Dw5ICdmLwAJf+lpC/BH89JWNnS9RthbQHfzLCiyVM8JhBy/Cp1FrDmABw9DAYuEru8aNReVYlRlNOZgnoyxTmX5o20Xix9CqTfyB1ZSoA+ZXoQMRCDnZG0BZLhEUDAkV/qtUNdGqZtb12p+0pyoCap4gQKBgF8iBjaa3/y05jKQj5J0+v/CVcZPhwy6A5UvGwL98daPlgCTROg4+84xCE2VDgYP5tZY2PBAmtJRDhGEHh9hQoEZMyj0bKAFObbyrX6wESTYabEcKFzj6qmFrOWkiUkD2KNsL6AmWSgKICN90RqlG18Sl8vPirci3PxPdDGrMKCLAoGBAKrYn6g799n7JQI3MauxXlIVUD1CLpp92E8vFH8IkbkNJxm68YWVVAqxqk2IXFT3RMMxOM8zvCjOCl2aGrHPOt+2Vz7q8Dp5crcQeGQp/tT2LWYoxuNrRERSz1LG0uJN0YcZPjVL/xcTrkfbd+goi0Cb10jX3J5Z9J15BHfsxfu/";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlsmh/RLuXm2F9nIJeYiCsgU7rlE6S4hTFEmSDgg4lBv9woJL25OwSP1jN7aW9YNJKDAKocPzbOG7VS2F2Dhgg3niAvekWFTHEIEjn2WTwfsS7PexIbjJ4J0J3Sc5fpToOl/SnxDqvTXrsWDPEQsjjl5urt8xvmkHbuUa20TsA+YlRz3DlV/gS0E5wf12qHqpudXp+o7lIG2cFvCP/I/DkKl5ADymC9gQytus1aaeBC94ZWz4QwWyu/OAMOFz7qAdPrCSEfTqwRUicclz2wOPbyJe8NRsb76H7yjAz6rqrJjdEG4JqYsRMcOO/deF4gx02qIud9EqnUbr+hhKktvfWwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://127.0.0.1:18090/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://127.0.0.1:18090/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

