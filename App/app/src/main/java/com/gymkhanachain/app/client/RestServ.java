package com.gymkhanachain.app.client;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Falta: modificar gymk, borrar gymk,
// Falta: GymkhanaToGymkhanaBean

public class RestServ {
    public static Gymkhana gymk;
    public static List<Gymkhana> listgymk;

    public static Gymkhana getGymkhanaById(int id) {
        GymkhanasRestService service = GymkhanasClient.createServiceDes(GymkhanasRestService.class);
        Call<Gymkhana> gymkhanaCall = service.getGymkanaById(1);
        gymkhanaCall.enqueue(new Callback<Gymkhana>() {
            @Override
            public void onResponse(Call<Gymkhana> call, Response<Gymkhana> response) {
                try {
                    gymk = response.body();
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Gymkhana> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        return gymk;
    }

    public static List<Gymkhana> getNearbyGymks(LatLng position_sup, LatLng position_inf){
        GymkhanasRestService service = GymkhanasClient.createServiceDes(GymkhanasRestService.class);
        Call<List<Gymkhana>> gymkhanaCall = service.getNearbyGymks(position_sup.latitude, position_sup.longitude, position_inf.latitude, position_inf.longitude);
        gymkhanaCall.enqueue(new Callback<List<Gymkhana>>() {
            @Override
            public void onResponse(Call<List<Gymkhana>> call, Response<List<Gymkhana>> response) {
                try {
                    Log.d("onResponse", response.body().get(0).getName());
                    listgymk = response.body();
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Gymkhana>> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        return listgymk;
    }

    public static void addGymkhana(Gymkhana g){
        GymkhanasRestService service = GymkhanasClient.createServiceSer(GymkhanasRestService.class);
        Call<String> gymkhanaCall = service.doPost(g);
        gymkhanaCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.d("onResponse", "Correto");
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public static void modGymkhana(Gymkhana g){
        GymkhanasRestService service = GymkhanasClient.createServiceSer(GymkhanasRestService.class);
        Call<Void> gymkhanaCall = service.modGymkana(g.getGymk_id(),g);
        gymkhanaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    Log.d("onResponse", "Correto");
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public static void deleteGymkhana(int id){
        GymkhanasRestService service = GymkhanasClient.createServiceSimple(GymkhanasRestService.class);
        Call<Void> gymkhanaCall = service.deleteGymkhana(id);
        gymkhanaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    Log.d("onResponse", "Correto");
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public static List<Gymkhana> getUserGymkanas(String user){
        GymkhanasRestService service = GymkhanasClient.createServiceDes(GymkhanasRestService.class);
        Call<List<Gymkhana>> gymkhanaCall = service.getUserGymkanas(user);
        gymkhanaCall.enqueue(new Callback<List<Gymkhana>>() {
            @Override
            public void onResponse(Call<List<Gymkhana>> call, Response<List<Gymkhana>> response) {
                try {
                    // Log.d("onResponse", response.body().get(3).getPuntos().get(1).getName());
                    listgymk = response.body();
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Gymkhana>> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        return listgymk;
    }
}
