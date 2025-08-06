package ru.practicum.model.similarity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * Composite key representing a pair of events for similarity comparison.
 * Used as an embedded ID in the {@link Similarity} entity.
 *
 * Fields:
 * - `eventId` – ID of the first event in the pair.
 * - `otherEventId` – ID of the second event in the pair.
 *
 * @see Similarity
 */
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimilarityCompositeKey implements Serializable {
    Long eventId;
    Long otherEventId;
}
