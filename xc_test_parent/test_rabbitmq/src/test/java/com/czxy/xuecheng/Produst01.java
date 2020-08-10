package com.czxy.xuecheng;

import com.czxy.xuecheng.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRabbitMQBootApplication.class)
public class Produst01 {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail() {
        //channel.basicPublish(EXCHANGE_TOPIC_INFORM, "inform.email", null, message.getBytes());
        for(int i = 0 ; i < 5 ; i ++) {
            String message = "email inform to user" + i;
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM,"inform.email",message);
            System.out.println("Send Message is:'" + message + "'");
        }
    }

    @Test
    public void testSendSms() {
        //channel.basicPublish(EXCHANGE_TOPIC_INFORM, "inform.sms", null, message.getBytes());
        for(int i = 0 ; i < 5 ; i ++) {
            String message = "sms inform to user" + i;
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM,"inform.sms",message);
            System.out.println("Send Message is:'" + message + "'");
        }
    }

    @Test
    public void testSendSmsAndEmail() {
        //channel.basicPublish(EXCHANGE_TOPIC_INFORM, "inform.sms.email", null, message.getBytes());
        for(int i = 0 ; i < 5 ; i ++) {
            String message = "sms and email inform to user" + i;
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM,"inform.sms.email",message);
            System.out.println("Send Message is:'" + message + "'");
        }
    }

}
