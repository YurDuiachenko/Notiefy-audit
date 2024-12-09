package com.example.notiefyaudit.config;

import com.clickhouse.client.api.Client;
import com.example.notiefyaudit.domain.PlayedSong;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickhouseClientConfig {
    @Bean
    public Client client() {
        Client client = new Client.Builder()
                .addEndpoint("http://localhost:8123/default")
                .setUsername("default")
                .setPassword("")
                .build();
        client.register(PlayedSong.class, client.getTableSchema("played_song"));
        return client;
    }
}
