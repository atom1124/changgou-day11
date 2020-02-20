package com.changgou.order.config;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.WeixinPayFeign;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2020/1/4 9:59
 */

@EnableScheduling
public class ScheduleTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayFeign weixinPayFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 定时任务
     */
    @Scheduled(cron = "0/5 * * * * ?")//时间间隔五秒
    private void configureTasks() {
        try {
            String orderList = redisTemplate.boundListOps("orderList").rightPop() + "";
            if (orderList != null && !"".equals(orderList) && !"null".equals(orderList)) {
                String[] split = orderList.split("-");
                //定时每5秒检查一次Redis 队列中是否有数据，如果有，则再去查询微信服务器支付状态
                Map<String, String> resultMap = (Map<String, String>) weixinPayFeign.queryStatus(split[0]).getData();
                if (resultMap.get("return_code") != null && "success".equalsIgnoreCase(resultMap.get("return_code"))) {
                    if ("success".equalsIgnoreCase(resultMap.get("result_code"))) {
                        //如果已支付，则修改订单状态
                        orderService.updateStatus(split[0], resultMap.get("transaction_id"));
                    }
                } else {
                    //如果没有支付，是等待支付，则再将订单存入到Redis队列中，等会再次检查
                    if (Long.parseLong(split[1]) + 1800000 > System.currentTimeMillis()) {
                        redisTemplate.boundListOps("orderList").leftPush(orderList);
                    } else {
                        Order order = (Order) redisTemplate.boundHashOps("orders").get(split[0]);
                        //支付失败
                        List<OrderItem> cart = (List) redisTemplate.boundHashOps("Copy" + order.getUsername()).get("cart");

                        //回滚库存信息
                        for (OrderItem orderItem : cart) {
                            Long skuId = orderItem.getSkuId();
                            Integer num = orderItem.getNum();
                            skuFeign.incrCount(skuId,num);
                        }

                        //如果是支付失败，直接删除订单信息
                        orderService.deleteByOrderId(split[0]);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
