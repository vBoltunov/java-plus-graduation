package serializer;

import org.springframework.stereotype.Component;
import ru.practicum.EventSimilarityAvro;

@Component
public class EventSimilarityDeserializer extends BaseAvroDeserializer<EventSimilarityAvro> {
    public EventSimilarityDeserializer() {
        super(EventSimilarityAvro.getClassSchema());
    }
}
