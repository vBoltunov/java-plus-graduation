package ru.practicum.eventservice.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.eventservice.events.model.Event;

import java.util.List;
import java.util.Set;


public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByIdIn(@Param("ids") Set<Long> ids);

    boolean existsByCategoryId(long categoryId);

    Event findByIdAndInitiatorId(Long id, Long initiatorId);
}