package com.example.seckill.one.mq;

import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.model.dto.SeckillRequest;
import com.example.seckill.one.utils.FastjsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**单独测试用的示例*/
    public void send(Object message){
        amqpTemplate.convertAndSend(GlobalConstants.MQ_DEMO_EXCHANGE, GlobalConstants.MQ_DEMO_ROUTINGKEY,
                FastjsonUtils.convertObjectToJSON(message));
    }
}
