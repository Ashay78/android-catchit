package com.example.projet1;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;

public class Jeux extends AppCompatActivity {

    MonJeux monJeux;
    SensorManager mgr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle b = getIntent().getExtras();
        String name   = "mouse";
        String targer = "mouse";
        String hunter = "mouse";
        String result = "mouse";
        String sound  = "mouse";
        String bestScore  = "0";
        if(b != null) {
            name   = b.getString("name");
            targer = b.getString("targer");
            hunter = b.getString("hunter");
            result = b.getString("result");
            sound = b.getString("sound");
            bestScore = b.getString("bestScore");

        }
        TextView textScore = (TextView) findViewById(R.id.text_score_game);
        TextView textBestScore = (TextView) findViewById(R.id.text_best_score_game);
        textBestScore.setText("Best score : " + bestScore);
        TextView textName = (TextView) findViewById(R.id.name);
        textName.setText("Name : " + name);



        this.monJeux = (MonJeux) findViewById(R.id.view_mon_jeux);
        this.monJeux.setParams(name, targer, hunter, result, sound, textScore);

        mgr = (SensorManager) getSystemService( SENSOR_SERVICE );
    }

    /**
     * methode pour lancer l'applicarion quand elle revient en premier plan
     */
    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener( monJeux , mgr.getDefaultSensor( Sensor. TYPE_ACCELEROMETER ) , mgr.SENSOR_DELAY_GAME);
    }

    /**
     * methode pour mettre en pause quand l'application n'est pas en premier plan
     */
    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener( monJeux );
    }
}