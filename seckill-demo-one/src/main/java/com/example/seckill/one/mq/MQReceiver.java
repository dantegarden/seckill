package com.example.seckill.one.mq;

import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.model.dto.SeckillRequest;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.FastjsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(GlobalConstants.MQ_DEMO_QUEUE),
            key = GlobalConstants.MQ_DEMO_ROUTINGKEY,
            exchange = @Exchange(GlobalConstants.MQ_DEMO_EXCHANGE)))
    public void receive(String message){
        log.info("MqReceiver:{}", message);
    }
}
