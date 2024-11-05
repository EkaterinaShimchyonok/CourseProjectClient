package org.example.courseproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.DoubleProperty;

public class GsonUtils {
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(DoubleProperty.class, new DoublePropertySerializer())
                .registerTypeAdapter(DoubleProperty.class, new DoublePropertyDeserializer())
                .create();
    }
}
