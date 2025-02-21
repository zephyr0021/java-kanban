package http.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import statuses.StatusTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonTaskBuilder {
    public Gson gson;

    public JsonTaskBuilder() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    class TaskDeserializer implements JsonDeserializer<Task> {
        @Override
        public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("id") && jsonObject.get("id").getAsInt() != 0) {
                int id = jsonObject.get("id").getAsInt();
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                StatusTask status = StatusTask.valueOf(jsonObject.get("status").getAsString());
                return new Task(name, description, status, id, duration, startTime);
            } else {
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                return new Task(name, description, duration, startTime);
            }

        }
    }

    class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(dtf));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(String.format("%d", duration.toMinutes()));
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(jsonReader.nextInt());
        }
    }


}
