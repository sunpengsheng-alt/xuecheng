package com.czxy.xuecheng.rabbitmq.demo09;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Product09 {

    private static final String QUEUE_HEHE_SMS = "hehehehe";
    private static final String EXCHANGE_FANOUT_INTORM = "inform_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接
        Connection connection = null;
        // 创建通道
        Channel channel = null;
        try {
            // 连接
            // 创建连接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");
            // 创建连接
            connection = connectionFactory.newConnection();
            // 创建通道
            channel = connection.createChannel();

            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_FANOUT_INTORM, BuiltinExchangeType.FANOUT);
            // 声明队列
            channel.queueDeclare(QUEUE_HEHE_SMS,true,false,false,null);
            // 绑定
            channel.queueBind(QUEUE_HEHE_SMS,EXCHANGE_FANOUT_INTORM,"inform.#.sms.#");
            // 发送消息
            for (int i = 0; i < 5; i++) {
                String msg = "send sms" + i;
                channel.basicPublish(EXCHANGE_FANOUT_INTORM,"inform.sms",null,msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null){
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    }
}
