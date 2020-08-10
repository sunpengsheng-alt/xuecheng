package com.czxy.xuecheng.rabbitmq.demo09;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer09 {

    private static final String QUEUE_HEHE_SMS = "hehehehe";
    private static final String EXCHANGE_FANOUT_INTORM = "inform_exchange_fanout";

    public static void main(String[] args) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_HEHE_SMS,true,false,false,null);
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(new String(body,"UTF-8"));
                }
            };
            channel.basicConsume(QUEUE_HEHE_SMS,true,consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
