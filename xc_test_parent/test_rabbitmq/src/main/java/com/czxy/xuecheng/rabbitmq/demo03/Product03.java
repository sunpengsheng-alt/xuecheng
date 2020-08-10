package com.czxy.xuecheng.rabbitmq.demo03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Product03 {

    // 队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_EMAIL2 = "queue_inform_email2";
    private static final String QUEUE_INFORM_SMS = "queue_inform_SMS";
    private static final String EXCHANGE_ROUTING_INFORM = "inform_exchange_routing";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = null;
        Channel channel = null;

        try {
            // 1. 创建工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");

            // 1.1. 创建连接
            connection = connectionFactory.newConnection();
            // 1.2 获得管道
            channel = connection.createChannel();

            // 2 声明
            // 2.1 声明交换机
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, ExchangeTypes.DIRECT);
            // 2.2 声明3个队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL2,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            // 2.3 绑定
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);
            channel.queueBind(QUEUE_INFORM_EMAIL2,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL2);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS);

            // 3 发布消息
            for (int i = 0; i < 5; i++) {
                String msg ="热预测燃油费" + i;
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS,null,msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    }

}
