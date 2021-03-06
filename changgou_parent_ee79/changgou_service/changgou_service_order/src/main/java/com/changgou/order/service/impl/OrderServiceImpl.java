package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderService;
import com.changgou.user.feign.UserFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:sz.itheima
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page  页码
     * @param size  页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size) {
        //分页
        PageHelper.startPage(page, size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size) {
        //静态分页
        PageHelper.startPage(page, size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order) {
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public Example createExample(Order order) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (order != null) {
            // 订单id
            if (!StringUtils.isEmpty(order.getId())) {
                criteria.andEqualTo("id", order.getId());
            }
            // 数量合计
            if (!StringUtils.isEmpty(order.getTotalNum())) {
                criteria.andEqualTo("totalNum", order.getTotalNum());
            }
            // 金额合计
            if (!StringUtils.isEmpty(order.getTotalMoney())) {
                criteria.andEqualTo("totalMoney", order.getTotalMoney());
            }
            // 优惠金额
            if (!StringUtils.isEmpty(order.getPreMoney())) {
                criteria.andEqualTo("preMoney", order.getPreMoney());
            }
            // 邮费
            if (!StringUtils.isEmpty(order.getPostFee())) {
                criteria.andEqualTo("postFee", order.getPostFee());
            }
            // 实付金额
            if (!StringUtils.isEmpty(order.getPayMoney())) {
                criteria.andEqualTo("payMoney", order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if (!StringUtils.isEmpty(order.getPayType())) {
                criteria.andEqualTo("payType", order.getPayType());
            }
            // 订单创建时间
            if (!StringUtils.isEmpty(order.getCreateTime())) {
                criteria.andEqualTo("createTime", order.getCreateTime());
            }
            // 订单更新时间
            if (!StringUtils.isEmpty(order.getUpdateTime())) {
                criteria.andEqualTo("updateTime", order.getUpdateTime());
            }
            // 付款时间
            if (!StringUtils.isEmpty(order.getPayTime())) {
                criteria.andEqualTo("payTime", order.getPayTime());
            }
            // 发货时间
            if (!StringUtils.isEmpty(order.getConsignTime())) {
                criteria.andEqualTo("consignTime", order.getConsignTime());
            }
            // 交易完成时间
            if (!StringUtils.isEmpty(order.getEndTime())) {
                criteria.andEqualTo("endTime", order.getEndTime());
            }
            // 交易关闭时间
            if (!StringUtils.isEmpty(order.getCloseTime())) {
                criteria.andEqualTo("closeTime", order.getCloseTime());
            }
            // 物流名称
            if (!StringUtils.isEmpty(order.getShippingName())) {
                criteria.andEqualTo("shippingName", order.getShippingName());
            }
            // 物流单号
            if (!StringUtils.isEmpty(order.getShippingCode())) {
                criteria.andEqualTo("shippingCode", order.getShippingCode());
            }
            // 用户名称
            if (!StringUtils.isEmpty(order.getUsername())) {
                criteria.andLike("username", "%" + order.getUsername() + "%");
            }
            // 买家留言
            if (!StringUtils.isEmpty(order.getBuyerMessage())) {
                criteria.andEqualTo("buyerMessage", order.getBuyerMessage());
            }
            // 是否评价
            if (!StringUtils.isEmpty(order.getBuyerRate())) {
                criteria.andEqualTo("buyerRate", order.getBuyerRate());
            }
            // 收货人
            if (!StringUtils.isEmpty(order.getReceiverContact())) {
                criteria.andEqualTo("receiverContact", order.getReceiverContact());
            }
            // 收货人手机
            if (!StringUtils.isEmpty(order.getReceiverMobile())) {
                criteria.andEqualTo("receiverMobile", order.getReceiverMobile());
            }
            // 收货人地址
            if (!StringUtils.isEmpty(order.getReceiverAddress())) {
                criteria.andEqualTo("receiverAddress", order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if (!StringUtils.isEmpty(order.getSourceType())) {
                criteria.andEqualTo("sourceType", order.getSourceType());
            }
            // 交易流水号
            if (!StringUtils.isEmpty(order.getTransactionId())) {
                criteria.andEqualTo("transactionId", order.getTransactionId());
            }
            // 订单状态,0:未完成,1:已完成，2：已退货
            if (!StringUtils.isEmpty(order.getOrderStatus())) {
                criteria.andEqualTo("orderStatus", order.getOrderStatus());
            }
            // 支付状态,0:未支付，1：已支付，2：支付失败
            if (!StringUtils.isEmpty(order.getPayStatus())) {
                criteria.andEqualTo("payStatus", order.getPayStatus());
            }
            // 发货状态,0:未发货，1：已发货，2：已收货
            if (!StringUtils.isEmpty(order.getConsignStatus())) {
                criteria.andEqualTo("consignStatus", order.getConsignStatus());
            }
            // 是否删除
            if (!StringUtils.isEmpty(order.getIsDelete())) {
                criteria.andEqualTo("isDelete", order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * 删除订单
     * @param orderId
     */
    @Override
    public void deleteByOrderId(String orderId) {
        //1.从redis查询订单
        Order order = (Order) redisTemplate.boundHashOps("orders").get(orderId);
        //2.修改状态
        order.setUpdateTime(new Date());
        order.setPayStatus("2");//支付失败
        //更新order状态到数据库
        orderMapper.updateByPrimaryKeySelective(order);

        //回滚库存信息
        List<OrderItem> cart = (List) redisTemplate.boundHashOps("Copy" + order.getUsername()).get("cart");
        for (OrderItem orderItem : cart) {
            Long skuId = orderItem.getSkuId();
            Integer num = orderItem.getNum();
            skuFeign.incrCount(skuId,num);
        }

        //2.删除redis中的订单记录
        redisTemplate.boundHashOps("orders").delete(orderId);
        //2.1删除备份数据
        redisTemplate.boundHashOps("Copy" + order.getUsername()).delete("cart");
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }


    /**
     * 增加Order
     * @param order
     */
    @Override
    public Order add(Order order) {
        //1、查询购物车列表
        List<OrderItem> orderItemList = redisTemplate.boundHashOps("Carts_" + order.getUsername()).values();

        if (orderItemList != null && orderItemList.size() > 0) {
            //2、构建订单对象，保存订单
            String orderId = "NO" + idWorker.nextId();
            order.setId(orderId);

            Integer totalNum = 0;  //总数量
            Double totalMoney = 0.0;  //总金额
            Double payMoney = 0.0;  //实付金额=总金额-优惠金额+邮费

            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());
            order.setBuyerRate("0");  //未评价
            order.setOrderStatus("0");  //未完成
            order.setPayStatus("0");  //未支付
            order.setConsignStatus("0");  //未发货
            order.setIsDelete("0");  //未删除

            //3、构建订单商品对象,保存数据库
            for (OrderItem orderItem : orderItemList) {
                orderItem.setId("NO" + idWorker.nextId());  //订单商品id
                orderItem.setOrderId(orderId);  //订单id


                //统计相关金额
                totalNum += orderItem.getNum();
                totalMoney += orderItem.getMoney();
                payMoney = totalMoney;

                //保存订单商品列表
                orderItemMapper.insertSelective(orderItem);
            }
            //把统计后的金额设置到订单中
            order.setTotalNum(totalNum);
            order.setTotalMoney(totalMoney.intValue());
            order.setPayMoney(payMoney.intValue());


            //把订单保存到数据库中
            orderMapper.insertSelective(order);

            //4、扣库存....，必须要清空购物车前完成此操作
            skuFeign.decrCount(order.getUsername());
            //5、加积分....
            userFeign.addUserPoints(10);

            //5.1保存一份进redis备份用于库存回滚
            redisTemplate.boundHashOps("Copy" + order.getUsername()).put("cart", orderItemList);


            //6、清空购物车
            redisTemplate.delete("Carts_" + order.getUsername());

            //如果是在线支付
            if (order.getPayType().equals("1")) {
                //把订单放入redis中，便于查询
                redisTemplate.boundHashOps("orders").put(order.getId(), order);
            }
            //将订单添加到list队列
            redisTemplate.boundListOps("orderList").leftPush(order.getId() + "-" + System.currentTimeMillis());
        }
        return order;
    }

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 支付成功---修改订单状态
     * @param orderId       订单id
     * @param transactionid 交易流水号
     */
    @Override
    public void updateStatus(String orderId, String transactionid) {

        //从redis中查询订单
        Order order = (Order) redisTemplate.boundHashOps("orders").get(orderId);


        //修改状态
        order.setUpdateTime(new Date());//时间也可以从微信接口返回过来，这里为了方便，我们就直接使用当前时间了
        order.setPayTime(order.getUpdateTime());
        order.setTransactionId(transactionid);//交易流水号
        order.setPayStatus("1"); //已支付
        orderMapper.updateByPrimaryKeySelective(order);

        //2.删除redis中的订单记录
        redisTemplate.boundHashOps("orders").delete(orderId);


    }
}
