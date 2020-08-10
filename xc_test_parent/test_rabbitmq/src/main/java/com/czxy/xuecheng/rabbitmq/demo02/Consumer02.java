package com.czxy.xuecheng.rabbitmq.demo02;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer02 {

    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    public static void main(String[] args) {
        try {
            // 1. 获得工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");

            // 2. 获得连接
            Connection connection = connectionFactory.newConnection();
            // 3. 获得管道
            Channel channel = connection.createChannel();
            // 4. 声明队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            // 5. 消息消费
            // 5.1 消费方式
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费:" + new String(body,"UTF-8"));
                }
            };
            // 5.2 消费
            channel.basicConsume(QUEUE_INFORM_EMAIL,true,defaultConsumer);
            channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
