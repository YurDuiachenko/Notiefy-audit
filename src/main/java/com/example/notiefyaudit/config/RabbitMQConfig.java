package com.example.notiefyaudit.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.song-played.queue}")
    private String songPlayedQueueName;
    @Value("${rabbitmq.song-count.queue}")
    private String songCountQueueName;
    @Value("${rabbitmq.song-count.exchange}")
    private String songCountExchangeName;
    @Value("${rabbitmq.song-count.key}")
    private String songCountKey;

    @Bean
    public Queue songPlayedQueue() {
        return new Queue(songPlayedQueueName, false);
    }

    @Bean
    public Queue songCountQueue() {
        return new Queue(songCountQueueName, false);
    }

    @Bean
    public Exchange songCountExchange() {
        return new TopicExchange(songCountExchangeName, false, false);
    }

    @Bean
    public Binding songCountBinding(Queue songCountQueue, Exchange songCountExchange) {
        return BindingBuilder.bind(songCountQueue)
                .to(songCountExchange)
                .with(songCountKey)
                .noargs();
    }
}