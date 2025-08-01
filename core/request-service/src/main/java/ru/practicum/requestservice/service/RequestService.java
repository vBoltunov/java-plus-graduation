package ru.practicum.requestservice.service;

import ru.practicum.interaction.dto.request.RequestDto;
import ru.practicum.interaction.dto.request.RequestUpdateDto;
import ru.practicum.interaction.dto.request.RequestUpdateResultDto;

import java.util.List;
import java.util.Map;

public interface RequestService {

    List<RequestDto> getUserRequests(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getEventRequests(Long userId, Long eventId);

    RequestUpdateResultDto updateEventRequests(Long userId, Long eventId, RequestUpdateDto updateDto);

    Map<Long, Long> getConfirmedRequests(List<Long> eventIds);

    Long countAllByEventIdAndStatus(Long eventId, String requestStatus);
}
