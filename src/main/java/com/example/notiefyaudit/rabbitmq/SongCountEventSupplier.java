package com.example.notiefyaudit.rabbitmq;

import com.example.notiefyaudit.domain.SongCount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SongCountEventSupplier {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.song-count.exchange}")
    private String songCountExchangeName;
    @Value("${rabbitmq.song-count.key}")
    private String songCountKey;

    /**
     * Отправить топ список песен
     *
     * @param songs песни
     */
    public void supply(List<SongCount> songs) {

        try {
            var countedSongJson = objectMapper.writeValueAsString(songs);
            rabbitTemplate.convertAndSend(songCountExchangeName, songCountKey, countedSongJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}