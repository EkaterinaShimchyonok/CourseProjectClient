package org.example.courseproject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.DoubleProperty;

import java.lang.reflect.Type;

public class DoublePropertySerializer implements JsonSerializer<DoubleProperty> {
    @Override
    public JsonElement serialize(DoubleProperty src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", src.get());
        return jsonObject;
    }
}
