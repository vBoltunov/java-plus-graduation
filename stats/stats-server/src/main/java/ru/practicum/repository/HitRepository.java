package ru.practicum.repository;

import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT app AS app, uri AS uri, COUNT(ip) AS hits " +
            "FROM hits " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC",
            nativeQuery = true)
    List<StatsDto> getAllStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from ru.practicum.model.Hit h " +
            "where h.timestamp >= :start and h.timestamp <= :end " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> getAllStatsUniqueIp(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.StatsDto(h.app, h.uri, count(h.ip)) " +
            "from ru.practicum.model.Hit h " +
            "where h.timestamp >= :start and h.timestamp <= :end and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatsDto> getStats(@Param("uris") List<String> uris,
                            @Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from ru.practicum.model.Hit h " +
            "where h.timestamp >= :start and h.timestamp <= :end and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> getStatsUniqueIp(@Param("uris") List<String> uris,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);
}
