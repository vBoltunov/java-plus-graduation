package ru.practicum.client;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "stats-server")
public interface StatsFeignClient {

    String STATS_PATH = "/stats";
    String HIT_PATH = "/hit";

    @PostMapping(HIT_PATH)
    void addHit(@Valid @RequestBody HitDto hitDto);

    @GetMapping(STATS_PATH)
    List<StatsDto> getStats(@RequestParam("start") String start,
                            @RequestParam("end") String end,
                            @RequestParam("uris") List<String> uris,
                            @RequestParam("unique") Boolean unique);

}