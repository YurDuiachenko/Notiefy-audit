package com.example.notiefyaudit.service;

import com.example.notiefyaudit.rabbitmq.SongCountEventSupplier;
import com.example.notiefyaudit.repository.ClickhouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsScheduler {
    private final ClickhouseRepository clickhouseRepository;
    private final SongCountEventSupplier songCountEventSupplier;

    @Scheduled(cron = "0 12 1 * *")
    @Async
    public void sendAnalytics() {
        long epochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        log.info("Время: {}", epochSeconds);
        var songs = clickhouseRepository.getTopListenedSongs();
        songCountEventSupplier.supply(songs);
    }
}