package ru.practicum.eventservice.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.eventservice.category.model.Category;
import ru.practicum.interaction.dto.event.enums.EventState;

import java.time.LocalDateTime;

/**
 * Entity representing an event in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the event. Auto-generated.
 * - `annotation` – Short description of the event.
 * - `category` – Category to which the event belongs.
 * - `createdOn` – Timestamp of event creation.
 * - `description` – Full textual description of the event.
 * - `eventDate` – Scheduled date and time of the event.
 * - `initiatorId` – ID of the user who created or initiated the event.
 * - `location` – Location where the event takes place.
 * - `paid` – Indicates if participation in the event requires payment.
 * - `participantLimit` – Maximum number of participants allowed.
 * - `publishedOn` – Timestamp of event publication.
 * - `requestModeration` – Indicates if participation requests require moderation.
 * - `state` – Current moderation state of the event.
 * - `title` – Title of the event.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "annotation", length = 2000, nullable = false)
    String annotation;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "created_on")
    LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "description", length = 7000)
    String description;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @Column(name = "initiator_id", nullable = false)
    Long initiatorId;

    @Embedded
    Location location;

    @Column(name = "paid", nullable = false)
    boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 50)
    EventState state = EventState.PENDING;

    @Column(name = "title", length = 120, nullable = false)
    String title;
}
