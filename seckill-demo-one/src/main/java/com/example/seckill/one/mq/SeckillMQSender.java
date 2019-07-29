package com.example.seckill.one.mq;

import com.example.seckill.one.constants.GlobalConstants;
import com.example.seckill.one.model.dto.SeckillRequest;
import com.example.seckill.one.utils.FastjsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillMQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**发送秒杀信息*/
    public void sendSeckillRequest(SeckillRequest seckillRequest) {
        amqpTemplate.convertAndSend(GlobalConstants.MQ_SECKILL_EXCHANGE, GlobalConstants.MQ_SECKILL_ROUTINGKEY,
                FastjsonUtils.convertObjectToJSON(seckillRequest));
    }
}
