package com.gymkhanachain.app.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.gymkhanachain.app.commons.GymkConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GymkhanasClient {

    private static JsonDeserializer<Point> deserializer = new PointDeserializer();
    private static JsonSerializer<Point> serializer = new PointSerializer();

    private static Gson gson_des = new GsonBuilder().registerTypeAdapter(Point.class, deserializer).create();
    private static Gson gson_ser = new GsonBuilder().registerTypeAdapter(Point.class, serializer).create();

    private static Retrofit.Builder builderSer
            = new Retrofit.Builder()
            .baseUrl(GymkConstants.ENDPOINT)//(ApiRoutes.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson_ser));

    private static Retrofit.Builder builderDes
            = new Retrofit.Builder()
            .baseUrl(GymkConstants.ENDPOINT)//(ApiRoutes.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson_des));

    private static Retrofit.Builder builderSimple
            = new Retrofit.Builder()
            .baseUrl(GymkConstants.ENDPOINT)//(ApiRoutes.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofitSer = builderSer.build();
    private static Retrofit retrofitDes = builderDes.build();
    private static Retrofit retrofitSimple = builderSimple.build();

    private static OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    //Envío al servidor (serializador)
    public static <S> S createServiceSer(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            // builder.addConverterFactory(Point.class, deserializer).create();

            httpClient.addInterceptor(logging);
            builderSer.client(httpClient.build());
            retrofitSer = builderSer.build();
        }
        return retrofitSer.create(serviceClass);
    }

    //Recibo del servidor (deserializador)
    public static <S> S createServiceDes(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            // builder.addConverterFactory(Point.class, deserializer).create();

            httpClient.addInterceptor(logging);
            builderDes.client(httpClient.build());
            retrofitDes = builderDes.build();
        }
        return retrofitDes.create(serviceClass);
    }

    //Servicio simple (borrados)
    public static <S> S createServiceSimple(Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            // builder.addConverterFactory(Point.class, deserializer).create();

            httpClient.addInterceptor(logging);
            builderSimple.client(httpClient.build());
            retrofitSimple = builderSimple.build();
        }
        return retrofitSimple.create(serviceClass);
    }
}

