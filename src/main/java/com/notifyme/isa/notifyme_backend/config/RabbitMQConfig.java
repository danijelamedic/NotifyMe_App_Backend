package com.notifyme.isa.notifyme_backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${notifyme.exchange}")
    private String exchangeName;

    @Value("${notifyme.queue}")
    private String queueName;

    @Value("${notifyme.routing-key}")
    private String routingKey;

    @Bean
    public TopicExchange notifyMeExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue notifyMeQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding notifyMeBinding(Queue notifyMeQueue, TopicExchange notifyMeExchange) {
        return BindingBuilder
                .bind(notifyMeQueue)
                .to(notifyMeExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
