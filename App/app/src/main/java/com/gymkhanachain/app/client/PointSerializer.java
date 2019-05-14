package com.gymkhanachain.app.client;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<Point> {
    @Override
    public JsonElement serialize(Point p, Type typeOfSrc, JsonSerializationContext context) {
        Log.d("onResponse", "pasa por aqui");
        JsonObject json = new JsonObject();

        json.addProperty("IMAGE", p.getImage());
        json.addProperty("LAT", p.getLatitude());
        json.addProperty("LNG",p.getLongitude());
        json.addProperty("SHORT_DESC", p.getDescription());
        json.addProperty("NAME", p.getName());
        if (p instanceof TextPoint){
            TextPoint t = (TextPoint) p;
            json.addProperty("LONG_DESC", t.getLong_desc());
        } else {
            QuizzPoint q = (QuizzPoint) p;
            json.addProperty("QUIZ_TEXT", q.getQuizz_text());
            json.addProperty("SOL1",q.getSol1());
            json.addProperty("SOL2",q.getSol2());
            json.addProperty("SOL3",q.getSol3());
            json.addProperty("SOL4",q.getSol4());
            json.addProperty("CORRECT",q.getSolution());
        }

        return json;
    }
}
