package com.sumain.mq.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqListener {

    @RabbitListener(queues = "my-queue")
    public void handleMessage(String message) {
        System.out.println("接收到消息：" + message);
    }
}
