package com.gymkhanachain.app.client;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class PointDeserializer implements JsonDeserializer<Point> {
    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.d("onResponse", json.toString());
        JsonObject obj = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry: entries) {
            switch(entry.getKey()) {
                case "CORRECT":
                    return context.deserialize(json, QuizzPoint.class);
                case "LONG_DESC":
                    return context.deserialize(json, TextPoint.class);
                default:
                    continue;
            }
        }
        throw new IllegalArgumentException("Can't deserialize " + json.toString());
    }
}
