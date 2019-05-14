package com.gymkhanachain.app.client;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GymkhanasRestService {

    //String API_ROUTE = "/gymkhanas";

    // Obtener gymkhanas (MainActivity)
    @GET(ApiRoutes.GYMKHANAS)
    Call<List<Gymkhana>> getNearbyGymks(@Query("lat_sup")Double lat_sup, @Query("long_sup")Double long_sup, @Query("lat_inf")Double lat_inf, @Query("long_inf")Double long_inf);

    // Obtener gymkhanas de un usuario
    @GET(ApiRoutes.GYMKHANAS+"/user/{user}")
    Call<List<Gymkhana>> getUserGymkanas(@Path("user") String user);

    //Operaciones CRUD con Gymkhanas
    // Obtener una gymkhana
    @GET(ApiRoutes.GYMKHANAS+"/{id}")
    Call<Gymkhana> getGymkanaById(@Path("id") int id);

    // Añadir una gymkhana
    @POST(ApiRoutes.GYMKHANAS)
    Call<String> doPost(@Body Gymkhana gymkhana);

    // Modificar una gymkhana
    @POST(ApiRoutes.GYMKHANAS+"/{id}")
    Call<Void> modGymkana(@Path("id") int id, @Body Gymkhana gymkhana);

    //Borrar una gymkhana
    @DELETE(ApiRoutes.GYMKHANAS+"/{id}")
    Call<Void> deleteGymkhana(@Path("id") int id);
}
