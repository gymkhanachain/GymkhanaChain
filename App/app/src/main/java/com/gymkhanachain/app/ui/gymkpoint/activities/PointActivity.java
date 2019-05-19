package com.gymkhanachain.app.ui.gymkpoint.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
                Fragment contentFragment = null;

                // Se crea el fragmento que contendrá el punto
                if (PointType.getPointType(bean) == PointType.QUIZZ_POINT) {
                    contentFragment = QuizPointFragment.newInstance(bean.getId());
                } else if (PointType.getPointType(bean) == PointType.TEXT_POINT) {
                    contentFragment = TextPointFragment.newInstance(bean.getId());
                } else {
                    Log.i(TAG, "Punto no soportado: " + PointType.getPointType(bean));
                }

                if (contentFragment != null) {
                    // Lo añadimos a la interfaz
                    getSupportFragmentManager().beginTransaction().replace(R.id.point_content, contentFragment).commit();
                } else {
                    Log.i(TAG, "No se crea la interfaz");
                }
            } else {
                Log.i(TAG, "El punto no existe");
            }
        } else {
            Log.i(TAG, "No se ha pasado un punto");
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
        onBackPressed();
        return true;
    }

    @Override
    public void onCorrectQuestion() {
        Toast toast = Toast.makeText(this, getString(R.string.correct_answer), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onIncorrectQuestion(String correctAnswer) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.wrong_answer) + " " + correctAnswer);
        builder.setPositiveButton(R.string.bt_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickAccept() {
        onBackPressed();
    }
}
