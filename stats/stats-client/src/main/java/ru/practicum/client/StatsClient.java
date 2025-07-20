package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.exception.StatsClientException;
import ru.practicum.exception.StatsServerUnavailable;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatsClient {

    private final RestClient restClient;
    private final String statsServiceId;
    private final DiscoveryClient discoveryClient;
    private final RetryTemplate retryTemplate;

    public StatsClient(@Value("${stats-server.id:STATS-SERVER}") String statsServiceId, DiscoveryClient discoveryClient) {
        this.restClient = RestClient.create();
        this.statsServiceId = statsServiceId;
        this.discoveryClient = discoveryClient;
        this.retryTemplate = new RetryTemplate();
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(3000L);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
    }

    private ServiceInstance getInstance() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances(statsServiceId);
            if (instances.isEmpty()) {
                throw new StatsServerUnavailable("Сервис " + statsServiceId + " не найден в Eureka");
            }
            return instances.getFirst();
        } catch (Exception exception) {
            throw new StatsServerUnavailable("Ошибка обнаружения адреса сервиса статистики с id: " + statsServiceId, exception);
        }
    }

    private URI makeUri(String path) {
        ServiceInstance instance = retryTemplate.execute(cxt -> getInstance());
        return URI.create("http://%s:%d%s".formatted(instance.getHost(), instance.getPort(), path));
    }

    public void hit(StatDto statDto) {
        String path = "/hit";
        URI uri = makeUri(path);

        restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(statDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new StatsClientException(response.getStatusCode().value(), response.getBody().toString());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new StatsClientException(response.getStatusCode().value(), response.getBody().toString());
                })
                .toBodilessEntity();
    }

    public List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String path = "/stats?start=%s&end=%s&uris=%s&unique=%s"
                .formatted(start, end, String.join(",", uris), unique);
        URI uri = makeUri(path);

        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new StatsClientException(response.getStatusCode().value(), response.getBody().toString());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new StatsClientException(response.getStatusCode().value(), response.getBody().toString());
                })
                .body(new ParameterizedTypeReference<>() {});
    }
}