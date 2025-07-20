package ru.practicum.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

/**
 * Entity representing an event in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the event. Auto-generated.
 * - `annotation` – Short description of the event.
 * - `paid` – Indicates if participation in the event requires payment.
 * - `title` – Title of the event.
 * - `eventDate` – Scheduled date and time of the event.
 * - `description` – Full textual description of the event.
 * - `requestModeration` – Indicates if participation requests require moderation.
 * - `participantLimit` – Maximum number of participants allowed.
 * - `publishedOn` – Timestamp of event publication.
 * - `createdOn` – Timestamp of event creation.
 * - `category` – Category to which the event belongs.
 * - `initiator` – User who created or initiated the event.
 * - `location` – Location where the event takes place.
 * - `state` – Current moderation state of the event.
 * - `confirmedRequests` – Number of confirmed participation requests.
 * - `views` – Number of views for this event.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String annotation;
    boolean paid;
    String title;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    String description;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_at")
    private LocalDateTime publishedOn;

    @Column(name = "created_at")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    @Enumerated(EnumType.STRING)
    EventState state;

    @Column(name = "confirmed_requests")
    Integer confirmedRequests = 0;

    @Column(name = "views")
    Long views;

}
