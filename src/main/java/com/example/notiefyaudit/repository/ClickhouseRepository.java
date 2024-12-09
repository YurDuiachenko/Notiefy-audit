package com.example.notiefyaudit.repository;

import com.clickhouse.client.api.Client;
import com.clickhouse.client.api.data_formats.ClickHouseBinaryFormatReader;
import com.clickhouse.client.api.query.QueryResponse;
import com.example.notiefyaudit.domain.SongCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ClickhouseRepository {
    private final Client client;

    public List<SongCount> getTopListenedSongs() {
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
                        reader.getInteger("plays_count")
                ));
            }

            return songCounts;
        } catch (Exception e) {
            log.error("Failed to read data", e);
        }

        return List.of();
    }
}
