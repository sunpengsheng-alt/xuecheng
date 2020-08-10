package com.czxy.xuecheng.rabbitmq.demo04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Product04 {

    // 队列名称
    private static final String QUEUE_INFORM_EMAIL_TOPIC = "queue_inform_email_topic";
    private static final String QUEUE_INFORM_SMS_TOPIC = "queue_inform_sms_topic";
    private static final String EXCHANGE_TOPIC_INFORM = "inform_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            // 连接工程
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");
            // 获得连接
            connection = connectionFactory.newConnection();
            // 获得管道
            channel = connection.createChannel();
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_TOPIC_INFORM, ExchangeTypes.TOPIC);
            // 声明3个队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL_TOPIC,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS_TOPIC,true,false,false,null);
            // 绑定
            channel.queueBind(QUEUE_INFORM_EMAIL_TOPIC,EXCHANGE_TOPIC_INFORM,"inform.#.email.#");
            channel.queueBind(QUEUE_INFORM_SMS_TOPIC,EXCHANGE_TOPIC_INFORM,"inform.#.sms.#");
            // 发布消息
            for (int i = 0; i < 5; i++) {
                String msg = "send email" + i;
                channel.basicPublish(EXCHANGE_TOPIC_INFORM,"inform.email",null,msg.getBytes());
            }
            for (int i = 0; i < 5; i++) {
                String msg = "send sms" + i;
                channel.basicPublish(EXCHANGE_TOPIC_INFORM,"inform.sms",null,msg.getBytes());
            }
            for (int i = 0; i < 5; i++) {
                String msg = "send sms and enail" + i;
                channel.basicPublish(EXCHANGE_TOPIC_INFORM, "inform.sms.email",null,msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (channel != null){
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    }
}
