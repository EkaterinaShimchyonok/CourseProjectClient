package org.example.courseproject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.lang.reflect.Type;

public class DoublePropertyDeserializer implements JsonDeserializer<DoubleProperty> {
    @Override
    public DoubleProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            double value = jsonObject.get("value").getAsDouble();
            return new SimpleDoubleProperty(value);
        } else if (json.isJsonPrimitive()) {
            double value = json.getAsDouble();
            return new SimpleDoubleProperty(value);
        } else {
            throw new IllegalStateException("Unexpected JSON type: " + json);
        }
    }
}
