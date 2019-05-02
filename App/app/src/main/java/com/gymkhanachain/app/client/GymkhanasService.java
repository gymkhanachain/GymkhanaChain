package com.gymkhanachain.app.client;

import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.ui.userprofile.activity.UserProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GymkhanasService {

    //String API_ROUTE = "/gymkhanas";


    @GET(ApiRoutes.GYMKHANAS)
    Call< List<GymkhanaBean> > getGymkanas(); //Rename
    //Call<GymkhanasClient.GymkhanaList> getGymkanas(); //Rename

    @GET(ApiRoutes.GYMKHANAS+"/{id}")    //Call<GymkhanaBean> getGymkanaById(@Query("GYMK_ID") int id);
    Call<GymkhanaBean> getGymkanaById(@Path("id") int id);

    @POST(ApiRoutes.GYMKHANAS)
    Call<String> doPost();

    //TODO: usar otra clase

}