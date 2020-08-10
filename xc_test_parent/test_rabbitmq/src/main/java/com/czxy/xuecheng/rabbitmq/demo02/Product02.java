package com.czxy.xuecheng.rabbitmq.demo02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Product02 {

    // 队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "inform_exchange_fanout";

    public static void main(String[] args) {
        try {
            // 1.获得管道
            // 1.1 连接工程
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");

            // 1.2 获得连接
            Connection connection = connectionFactory.newConnection();
            // 1.3 获得管道
            Channel channel = connection.createChannel();

            // 2 声明
            // 2.1 声明交换机

            // 2.2 声明队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            // 2.3 绑定交换机和队列
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");

            // 3 给交换机发消息
            for (int i = 0; i < 5; i++) {
                String msg = "而VB古城" + i;
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
