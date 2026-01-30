package com.notifyme.isa.notifyme_backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    // DLQ/DLX
    @Value("${notifyme.dlx}")
    private String dlxName;

    @Value("${notifyme.dlq}")
    private String dlqName;

    @Value("${notifyme.dlq-routing-key}")
    private String dlqRoutingKey;

    @Bean
    public TopicExchange notifyMeExchange() {
        return new TopicExchange(exchangeName, true, false);
    }
//    @Bean
//    public DirectExchange deadLetterExchange() {
//        return new DirectExchange(dlxName, true, false);
//    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(dlxName, true, false);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(dlqRoutingKey);
    }


    /**
     * Main queue sa DLX podešavanjem:
     * Ako consumer ne uspe nakon retry-a, poruka ide na DLX sa dlqRoutingKey
     */
    @Bean
    public Queue notifyMeQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", dlxName)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding notifyMeBinding(Queue notifyMeQueue, TopicExchange notifyMeExchange) {
        return BindingBuilder
                .bind(notifyMeQueue)
                .to(notifyMeExchange)
                .with(routingKey);
    }

//    @Bean
//    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
//        return BindingBuilder
//                .bind(deadLetterQueue)
//                .to(deadLetterExchange)
//                .with(dlqRoutingKey);
//    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }


}

