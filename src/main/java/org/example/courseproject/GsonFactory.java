package org.example.courseproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

public class GsonFactory {

    public static Gson create() {
        return new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new SqlDateTypeAdapter())
                .registerTypeAdapter(java.sql.Time.class, new SqlDateTypeAdapter())
                .create();
    }
}


