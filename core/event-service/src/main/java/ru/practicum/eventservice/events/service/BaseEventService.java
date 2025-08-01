package ru.practicum.eventservice.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import ru.practicum.client.StatRestClient;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.eventservice.category.model.QCategory;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;
import ru.practicum.eventservice.events.model.QEvent;
import ru.practicum.eventservice.events.repository.EventRepository;
import ru.practicum.interaction.dto.event.EventDto;
import ru.practicum.interaction.dto.event.EventShortDto;
import ru.practicum.interaction.dto.user.UserShortDto;
import ru.practicum.interaction.feign.RequestFeignClient;
import ru.practicum.interaction.feign.UserFeignClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseEventService {

    protected final EventRepository eventRepository;
    protected final EventMapper eventMapper;
    protected final JPAQueryFactory jpaQueryFactory;
    protected final StatRestClient statRestClient;
    protected final RequestFeignClient requestFeignClient;
    protected final UserFeignClient userFeignClient;
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected List<EventShortDto> fetchEventsWithStats(BooleanExpression queryExpression, Pageable pageRequest) {
        List<EventShortDto> events = fetchEvents(queryExpression, pageRequest);
        enrichWithStats(events);
        return events;
    }

    protected List<EventShortDto> fetchEvents(BooleanExpression queryExpression, Pageable pageRequest) {
        List<Event> events = jpaQueryFactory
                .selectFrom(QEvent.event)
                .leftJoin(QEvent.event.category, QCategory.category)
                .fetchJoin()
                .where(queryExpression)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .stream()
                .toList();

        List<Long> initiatorIds = events.stream().map(Event::getInitiatorId).toList();
        Map<Long, UserShortDto> initiators = userFeignClient.getUsersByIDS(initiatorIds);

        return events.stream()
                .map(event -> eventMapper.toEventShortDto(event, initiators.get(event.getInitiatorId())))
                .toList();
    }

    protected void enrichWithStats(List<? extends EventDto> events) {
        List<Long> eventIds = events.stream().map(EventDto::getId).toList();
        Map<Long, Long> confirmedRequestsMap = getConfirmedRequests(eventIds);

        Set<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toSet());

        LocalDateTime start = events.stream()
                .min(Comparator.comparing(EventDto::getEventDate))
                .orElseThrow(() -> new ru.practicum.interaction.exception.NotFoundException("Нет доступных дат событий"))
                .getEventDate();

        Map<String, Long> viewMap = statRestClient.getStats(start, LocalDateTime.now(), uris.stream().toList(), false)
                .stream()
                .collect(Collectors.groupingBy(StatsDto::getUri, Collectors.summingLong(StatsDto::getHits)));

        events.forEach(event -> {
            event.setViews(viewMap.getOrDefault("/events/" + event.getId(), 0L));
            event.setConfirmedRequests(confirmedRequestsMap.getOrDefault(event.getId(), 0L));
        });
    }

    protected Map<Long, Long> getConfirmedRequests(List<Long> eventIds) {
        return requestFeignClient.getConfirmedRequests(eventIds);
    }

    protected void recordHit(String uri, String ip) {
        HitDto hitDto = new HitDto("main-server", uri, ip, LocalDateTime.now().format(dateTimeFormatter));
        statRestClient.addHit(hitDto);
    }
}