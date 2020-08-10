package com.czxy.xuecheng.rabbitmq.demo03;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer03 {

    // 队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_EMAIL2 = "queue_inform_email2";
    private static final String QUEUE_INFORM_SMS = "queue_inform_SMS";
    private static final String EXCHANGE_ROUTING_INFORM = "inform_exchange_routing";

    public static void main(String[] args) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");

            //获得连接
            Connection connection = connectionFactory.newConnection();
            // 获得管道
            Channel channel = connection.createChannel();

            // 声明队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL2,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            channel.queueDeclare(EXCHANGE_ROUTING_INFORM,true,false,false,null);

            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费:" + new String(body,"UTF-8"));
                }
            };
            channel.basicConsume(QUEUE_INFORM_EMAIL,true,consumer);
            channel.basicConsume(QUEUE_INFORM_SMS,true,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
