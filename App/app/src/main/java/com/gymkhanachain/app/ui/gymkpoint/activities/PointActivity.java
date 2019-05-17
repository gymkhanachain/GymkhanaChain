package com.gymkhanachain.app.ui.gymkpoint.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.ui.gymkpoint.fragments.QuizPoint;
import com.gymkhanachain.app.ui.gymkpoint.fragments.TextPoint;

public class PointActivity extends AppCompatActivity implements QuizPoint.OnFragmentInteractionListener, TextPoint.OnFragmentInteractionListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    public void onFragmentInteraction(Uri uri) {
        // TODO ...
    }
}
