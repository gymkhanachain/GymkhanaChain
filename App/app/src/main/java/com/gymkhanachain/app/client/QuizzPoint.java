package com.gymkhanachain.app.client;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizzPoint extends Point {
    @Expose
    @SerializedName("QUIZ_TEXT")
    private String quizz_text;
    @Expose
    @SerializedName("SOL1")
    private String sol1;
    @Expose
    @SerializedName("SOL2")
    private String sol2;
    @Expose
    @SerializedName("SOL3")
    private String sol3;
    @Expose
    @SerializedName("SOL4")
    private String sol4;
    @Expose
    @SerializedName("CORRECT")
    private int solution;

    public QuizzPoint(Integer point_id, String image, String name, String short_desc, LatLng latlng, String quizz_text, String sol1, String sol2, String sol3, String sol4, int solution) {
        super(point_id, image, name, short_desc, latlng.latitude, latlng.longitude);
        this.quizz_text = quizz_text;
        this.sol1 = sol1;
        this.sol2 = sol2;
        this.sol3 = sol3;
        this.sol4 = sol4;
        this.solution = solution;
    }

    public QuizzPoint(){
        super();
    }

    public String getQuizz_text() {
        return quizz_text;
    }

    public void setQuizz_text(String quizz_text) {
        this.quizz_text = quizz_text;
    }

    public String getSol1() {
        return sol1;
    }

    public void setSol1(String sol1) {
        this.sol1 = sol1;
    }

    public String getSol2() {
        return sol2;
    }

    public void setSol2(String sol2) {
        this.sol2 = sol2;
    }

    public String getSol3() {
        return sol3;
    }

    public void setSol3(String sol3) {
        this.sol3 = sol3;
    }

    public String getSol4() {
        return sol4;
    }

    public void setSol4(String sol4) {
        this.sol4 = sol4;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }
}
