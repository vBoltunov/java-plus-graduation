package ru.practicum.user.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.subscriptions.model.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the user. Auto-generated.
 * - `name` – Full name of the user. Cannot be null.
 * - `email` – Email address of the user. Must be unique and not null.
 * - `subscriptions` – List of subscriptions initiated by the user.
 * - `subscribers` – List of users subscribed to this user.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "email", nullable = false, unique = true)
    String email;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "subscribedTo", cascade = CascadeType.ALL)
    List<Subscription> subscribers = new ArrayList<>();
}
