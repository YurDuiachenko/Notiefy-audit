package com.example.notiefyaudit.rabbitmq;

import com.example.notiefyaudit.domain.PlayedSong;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SongPlayedEventListener {
    private static final String queueName = "songPlayed";

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = queueName)
    public void listen(String massage) {
        PlayedSong song;
        try {
            song = objectMapper.readValue(massage, PlayedSong.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Message read from firstQueue : {}", song);
    }
}