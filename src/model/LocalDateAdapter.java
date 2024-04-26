package model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/* Local Date Adapter is the class that enables JSON file to correctly format to LocalDate.
   Inspired by Dr. Brian Fraser's video on the topic. */

public class LocalDateAdapter implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize
            (JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
