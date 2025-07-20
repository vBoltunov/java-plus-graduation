package ru.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDtoResponse;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/comments/{eventId}")
@RequiredArgsConstructor
public class CommentControllerPublic {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDtoResponse>> getComments(@PathVariable Long eventId,
                                                                @RequestParam(required = false, defaultValue = "0")
                                                                @PositiveOrZero Integer from,
                                                                @RequestParam(required = false, defaultValue = "10")
                                                                @Positive Integer size) {
        log.info("Эндпоинт /comments/{}. GET запрос на получение всех комментариев для события.",
                eventId);
        return new ResponseEntity<>(commentService.getCommentsOfEventPublic(eventId, from, size), HttpStatus.OK);
    }
}