package com.example.notiefyaudit.rabbitmq;

import com.clickhouse.client.api.Client;
import com.example.notiefyaudit.domain.PlayedSong;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SongPlayedEventListener {
    private static final String queueName = "songPlayed";

    private final ObjectMapper objectMapper;
    private final Client client;

    @RabbitListener(queues = queueName)
    public void listen(String massage) {
        PlayedSong song;
        try {
            song = objectMapper.readValue(massage, PlayedSong.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Message read from firstQueue : " + song);
        client.insert("played_song", List.of(song));
    }
}