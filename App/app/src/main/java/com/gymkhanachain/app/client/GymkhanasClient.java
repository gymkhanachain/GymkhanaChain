package com.gymkhanachain.app.client;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.ui.userprofile.activity.UserProfileActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GymkhanasClient {

    public HttpLoggingInterceptor interceptor;
    public OkHttpClient okClient;
    public Retrofit retrofit;
    public GymkhanasService service;

    //Parametrizar?
    public GymkhanasClient() {

        interceptor = new HttpLoggingInterceptor();
        okClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiRoutes.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
    }

    public GymkhanasService getService(){
        service = retrofit.create(GymkhanasService.class);
        return service;
    }


    //Esto de abajo ni caso
    public GymkhanaBean getById(int id) {
        GymkhanaBean ret;
        Call<GymkhanaBean> call = service.getGymkanaById(id);
        call.enqueue(new Callback<GymkhanaBean>() {
            @Override
            public void onResponse(Call<GymkhanaBean> call, Response<GymkhanaBean> response) {
                /*
                response.body();
                Log.d("Client",response.body().getDescription());
                */ //No parsean el response bien, peticion HTTP OK
            }

            //Tiene que hacer esto pero de otra manera. (No tiene sentido usar callbacks aqui?)
            @Override
            public void onFailure(Call<GymkhanaBean> call, Throwable t) {
                Log.d("Client", t.getMessage());
            }
        });
        return null;

    }

    public List<List<GymkhanaBean>> getAll() {
        Call<List<GymkhanaBean>> call = service.getGymkanas();
        call.enqueue(new Callback<List<GymkhanaBean>>() {
            @Override
            public void onResponse(Call<List<GymkhanaBean>> call, Response<List<GymkhanaBean>> response) {
                //response.doStuff(); //TODO: Con el json que le llega actualmente da error
                //llegan sin "clave" (en plan ("id":1) y como un array de arrays en vez de como un
                //array de objetos, con lo que no lo puede parsear a un pojo. O bien se parsea aqui a mano o ver en el servidor como
                //hacer para cambiar el JSON que devuelve.
                //  for(Gymkhana bean : response.body().gymkhanas) {
                //x.add(bean.getTitle());
                // }
            }

            @Override
            public void onFailure(Call<List<GymkhanaBean>> call, Throwable t) {
                Log.d("Client", t.getMessage());
            }
            //De momento se usa gymkhanabean
        });

        return null;
    }

    public String postGymkana() {

        Call<String> call = service.doPost();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Client", t.getMessage());
            }
            //De momento se usa gymkhanabean
        });
        return null;
    }

}

