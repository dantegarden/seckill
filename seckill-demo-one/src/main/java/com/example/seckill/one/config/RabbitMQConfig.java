package com.example.seckill.one.config;

import com.example.seckill.one.constants.GlobalConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue(){
        //队列名称，是否持久化
        return new Queue(GlobalConstants.MQ_SECKILL_QUEUE, true);
    }
}
