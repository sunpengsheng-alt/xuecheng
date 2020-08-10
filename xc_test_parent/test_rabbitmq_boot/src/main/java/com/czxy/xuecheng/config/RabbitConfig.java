package com.czxy.xuecheng.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // 交换机名称
    public static final String EXCHANGE_TOPIC_INFORM = "inform_exchange_topic";

    //队列名称
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    /** 交换机配置
     *  ExchangeBuilder提供了fanout、direct、topic、header交换机类型的配置
     *  channel.exchangeDeclare(EXCHANGE_TOPIC_INFORM, BuiltinExchangeType.TOPIC);
     * @return
     */
    @Bean(EXCHANGE_TOPIC_INFORM)
    public Exchange exchange_topic() {
        //durable(true)持久化，消息队列重启后交换机仍然存在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFORM).durable(true).build();
    }

    /**
     * 声明队列
     * channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
     * @return
     */
    @Bean(QUEUE_INFORM_SMS)
    public Queue queue_inform_sms(){
        return new Queue(QUEUE_INFORM_SMS);
    }
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue queue_inform_email(){
        return new Queue(QUEUE_INFORM_EMAIL,true,false,false);
    }

    /**
     * 绑定队列到交换机
     * channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPIC_INFORM, "inform.#.email.#");
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding binding_queue_inform_sms(@Qualifier(QUEUE_INFORM_SMS) Queue queue, @Qualifier(EXCHANGE_TOPIC_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }
    @Bean
    public Binding binding_queue_inform_email(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue, @Qualifier(EXCHANGE_TOPIC_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }
}
