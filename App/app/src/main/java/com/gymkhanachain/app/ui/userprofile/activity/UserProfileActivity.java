package com.gymkhanachain.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    //Not the real one
    private UserProfileInfoVo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //userinfo
        if (user == null)  new FetchProfileInfo().execute(Long.getLong("0")) ;

        setTitle("Perfil de usuario");// TODO, remove harcoded strings
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    //
    }

    @Override
    protected void onPause(){
        super.onPause();
    //Free resources
    }

    @Override
    protected void onStop(){
        super.onStop();
    // Save state
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEditProfile(View view) {

        Context context = getApplicationContext();
        CharSequence text = "ClickEdit";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP| Gravity.LEFT, 51, 51);
        toast.show();
    }

    public void onClickAchievements(View view){
        Context context = getApplicationContext();
        CharSequence text = "Click Logros";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP| Gravity.LEFT, 51, 51);
        toast.show();


    }

    private class FetchProfileInfo extends AsyncTask<Long, Integer, UserProfileInfoVo> {

        protected UserProfileInfoVo doInBackground(Long... userId) {
            //getAchievements()//AskGoogleAPI()
            //doStuff
            //maybe fetchAchievements should be A separate Async task. Consider whether this one may be needed or not.
            return new UserProfileInfoVo();
        }

        protected void onPostExecute(UserProfileInfoVo result) {

            ((TextView)findViewById(R.id.UserName)).setText(result.getUserName());
            ((TextView)findViewById(R.id.UserLevelValue)).setText(String.valueOf(result.getUserLevel()));
            ((TextView)findViewById(R.id.UserLocationValue)).setText(result.getUserLocation());
            ((TextView)findViewById(R.id.GymkanasCountValue)).setText(String.valueOf(result.getNumberOfGymkanas()));
            ((TextView)findViewById(R.id.PointsSecuredValue)).setText(">9000");
            ((ImageView)findViewById(R.id.profileImageView)).setImageBitmap(result.getProfileImage());

        }
    }

    //User profile info mock stub, actual user retrieval yet to do (googleApis)
    protected class UserProfileInfoVo {

        private long userId;
        private int numberOfGymkanas;
        private int pointsSecured;
        private int userLevel;
        private String userName;
        private String userLocation;

        public Bitmap getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Bitmap profileImage) {
            this.profileImage = profileImage;
        }

        private Bitmap profileImage;

        public long getUserId() {
            return userId;
        }

        public UserProfileInfoVo(){
            this.userId = 0;
            this.setNumberOfGymkanas(99);
            this.setUserLevel(13);
            this.setUserName("Bushwick");
            this.setUserLocation("Manchester");
            this.setProfileImage(((BitmapDrawable)getResources().getDrawable(R.drawable.profile_default)).getBitmap());
        }

        public int getNumberOfGymkanas() {
            return numberOfGymkanas;
        }

        public void setNumberOfGymkanas(int numberOfGymkanas) {
            this.numberOfGymkanas = numberOfGymkanas;
        }

        public int getPointsSecured() {
            return pointsSecured;
        }

        public void setPointsSecured(int pointsSecured) {
            this.pointsSecured = pointsSecured;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserLocation() {
            return userLocation;
        }

        public void setUserLocation(String userLocation) {
            this.userLocation = userLocation;
        }
    }
}
