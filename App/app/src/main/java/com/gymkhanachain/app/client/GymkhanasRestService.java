package com.gymkhanachain.app.client;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GymkhanasRestService {

    //String API_ROUTE = "/gymkhanas";


    @GET(ApiRoutes.GYMKHANAS)
    Call< List<GymkhanaResponse> > getGymkanas();

    @GET(ApiRoutes.GYMKHANAS+"/{id}")
    Call<GymkhanaResponse> getGymkanaById(@Path("id") int id);

    @POST(ApiRoutes.GYMKHANAS)
    Call<String> doPost();


}
