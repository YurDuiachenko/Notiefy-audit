package com.example.notiefyaudit.service;

import com.clickhouse.client.api.Client;
import com.clickhouse.client.api.data_formats.ClickHouseBinaryFormatReader;
import com.clickhouse.client.api.query.QueryResponse;
import com.example.notiefyaudit.domain.SongCount;
import com.example.notiefyaudit.rabbitmq.SongCountEventSupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsScheduler {
    private final Client client;
    private final SongCountEventSupplier songCountEventSupplier;

    // 0 0 12 30 * *
    @Scheduled(cron = "0 * 14 16 * *")
    @Async
    public void sendAnalytics() {
        log.info("Время: {}", LocalDateTime.now());
        songCountEventSupplier.supply(getTopListenedSongs());
    }

    private List<SongCount> getTopListenedSongs() {
        final String sql = """
                SELECT
                    name,
                    COUNT(*) AS plays_count
                FROM played_song
                GROUP BY name
                ORDER BY plays_count DESC
                LIMIT 10;
                """;

        try (QueryResponse response = client.query(sql).get(3, TimeUnit.SECONDS)) {
            ClickHouseBinaryFormatReader reader = Client.newBinaryFormatReader(response);

            List<SongCount> songCounts = new ArrayList<>();
            while (reader.hasNext()) {
                reader.next();

                songCounts.add(new SongCount(
                        reader.getString("name"),
                        reader.getBigInteger("plays_count").intValue()
                ));
            }

            songCounts.forEach(System.out::println);
            return songCounts;
        } catch (Exception e) {
            log.error("Failed to read data", e);
        }

        return List.of();
    }
}
