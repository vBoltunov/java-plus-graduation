package serializer;

import org.springframework.stereotype.Component;
import ru.practicum.UserActionAvro;

@Component
public class UserAvroSpecificDeserializer extends BaseAvroDeserializer<UserActionAvro> {
    public UserAvroSpecificDeserializer() {
        super(UserActionAvro.getClassSchema());
    }
}
