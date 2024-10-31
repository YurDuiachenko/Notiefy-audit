package com.example.notiefyaudit.config;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.song-played.queue}")
    private String songPlayedQueueName;

    @Bean
    public Queue myQueue() {
        return new Queue(songPlayedQueueName, false);
    }
}