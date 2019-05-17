package com.gymkhanachain.app.ui.userprofile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageToImageViewAsyncTask;
import com.gymkhanachain.app.ui.commons.fragments.LoginFragment;


public class UserProfileActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener{

    private UserProfileInfoVo user;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String LOGIN_FRAGMENT_TAG = "LoginFragment";
    private static final String TAG = "UserProfile";

    FragmentManager fragmentManager;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        //userinfo ???
        //if (user == null)  new FetchProfileInfo().execute(Long.getLong("0")) ;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        fragmentManager = getSupportFragmentManager();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (account!=null){
            setTitle(R.string.user_profile);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                // TODO: cambiar sobre por lapiz ???
                @Override
                public void onClick(View view) {
                    // TODO add edit profile functionality
                    Toast newToast = Toast.makeText(getApplicationContext(), "Editar perfil", Toast.LENGTH_SHORT);
                    newToast.show();
                }
            });
            updateUI(account);
        } else {
            DialogFragment dialog = new LoginFragment();
            dialog.show(fragmentManager,LOGIN_FRAGMENT_TAG);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // TODO add functionality, doesnt work properly
        Toast newToast = Toast.makeText(getApplicationContext(), "Back pressed", Toast.LENGTH_SHORT);
        newToast.show();
        onBackPressed();
        return true;
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
        CharSequence text = "Aquí aparecerán tus logros";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP| Gravity.LEFT, 51, 51);
        toast.show();

    }

    @Override
    public void refusedToLogin() {
        Toast.makeText(getApplicationContext(), getString(R.string.toast_no_login), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        }

         catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account!=null) {
            Uri personPhotoUrl = account.getPhotoUrl();
            ((TextView) findViewById(R.id.UserName)).setText(account.getDisplayName());
            if (user != null) new FetchProfileInfo().execute(Long.getLong("0"));
            if (preferences.getBoolean("profilepic_pref_switch", false)) {
                ImageView imageView = (ImageView)findViewById(R.id.profileImageView);
                new DownloadImageToImageViewAsyncTask(getApplicationContext(),imageView).execute(personPhotoUrl);
            }
        }
        else{
            Log.e(TAG, " account is null");
        }
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
            /// ??? ((ImageView)findViewById(R.id.profileImageView)).setImageBitmap(result.getProfileImage());

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
