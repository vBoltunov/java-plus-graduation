package ru.practicum.model.similarity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

/**
 * Entity representing a similarity score between two items in the system.
 * Uses a composite key to uniquely identify the pair of items being compared.
 *
 * Fields:
 * - `key` – Composite key consisting of the IDs of the two items being compared.
 * - `score` – Numeric value representing the similarity score between the items.
 * - `timestamp` – Time when the similarity was calculated or last updated.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "similarities")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Similarity {
    @EmbeddedId
    SimilarityCompositeKey key;

    double score;

    Instant timestamp;
}
