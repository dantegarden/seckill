package com.example.seckill.one.mq;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.model.dto.SeckillRequest;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.FastjsonUtils;
import com.example.seckill.one.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillMQReceiver {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(GlobalConstants.MQ_SECKILL_QUEUE),
            key = GlobalConstants.MQ_SECKILL_ROUTINGKEY,
            exchange = @Exchange(GlobalConstants.MQ_SECKILL_EXCHANGE)))
    public void receiveSeckillRequest(String message){
        log.info("SeckillMQReceiver:{}", message);
        SeckillRequest seckillRequest = (SeckillRequest) FastjsonUtils.convertJsonToObject(message, SeckillRequest.class);
        User user = seckillRequest.getUser();
        Long goodsId = seckillRequest.getGoodsId();

        //是否已经秒杀到了
        SeckillOrder seckillOrder = orderService.fetchSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(seckillOrder != null){
            return;
        }

        //是否还有库存
        GoodsVO goods = goodsService.fetchGoodsvoById(goodsId);
        Integer stock = goods.getStockCount();
        if(stock <= 0){
            seckillService.setGoodsOver(goodsId);
            return;
        }

        //减库存 下订单 写入秒杀订单
        Order orderInfo = seckillService.seckill(user, goods);
    }
}
