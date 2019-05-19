package com.gymkhanachain.app.ui.gymkpoint.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.GymkConstants;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.model.beans.PointType;
import com.gymkhanachain.app.model.commons.PointCache;
import com.gymkhanachain.app.ui.gymkpoint.fragments.QuizPointFragment;
import com.gymkhanachain.app.ui.gymkpoint.fragments.TextPointFragment;

public class PointActivity extends AppCompatActivity implements QuizPointFragment.OnQuizPointFragmentInteraction, TextPointFragment.OnTextPointFragmentInteraction {

    private static final PointCache pointCache = PointCache.getInstance();

    private static String TAG = "PlayGymkhanaActivity";

    SharedPreferences preferences;
    Integer pointId;
    Fragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean useDarkTheme = preferences.getBoolean("activate_dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        // Obtenemos el punto
        Intent intent = getIntent();
        pointId = intent.getIntExtra(GymkConstants.TAG_POINT_ID, -1);

        if (pointId > -1) {
            // Se obtiene el bean
            PointBean bean = pointCache.getPoint(pointId);

            if (bean != null) {
                // Se crea el fragmento que contendrá el punto
                switch (PointType.getPointType(bean)) {
                    case PointType.QUIZZ_POINT:
                        contentFragment = QuizPointFragment.newInstance(bean.getId());
                    case PointType.TEXT_POINT:
                        contentFragment = TextPointFragment.newInstance(bean.getId());
                    default:
                        contentFragment = null;
                }

                if (contentFragment != null) {
                    // Lo añadimos a la interfaz
                    getSupportFragmentManager().beginTransaction().replace(R.id.point_content, contentFragment).commit();
                }
            }
        }

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
