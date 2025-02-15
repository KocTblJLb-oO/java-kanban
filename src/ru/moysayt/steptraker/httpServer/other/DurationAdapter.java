package ru.moysayt.steptraker.httpServer.other;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext jsonSerializationContext) {
        // Сериализуем Duration как количество секунд
        return new JsonPrimitive(duration.getSeconds());
    }

    @Override
    public Duration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // Десериализуем Duration из количества секунд
        long seconds = jsonElement.getAsLong();
        return Duration.ofSeconds(seconds);
    }
}
