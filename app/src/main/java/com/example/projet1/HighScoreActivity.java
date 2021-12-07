package com.example.projet1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HighScoreActivity  extends AppCompatActivity {

    JSONObject jsonScore;
    TableLayout tabLay;
    TableRow tabRow;
    TextView label1, label2;
    String TAG = "TEST";

    ArrayList<Score> scoreArrayList = new ArrayList<Score>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        ListView listView = (ListView)findViewById(R.id.listView);

        SharedPreferences sharedPreferences= this.getSharedPreferences("gameScore6", Context.MODE_PRIVATE);

        String strJson = sharedPreferences.getString("jsondata","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        Log.e(TAG, strJson);
        if (strJson != null) {
            try {
                this.jsonScore = new JSONObject(strJson);
                JSONArray array = this.jsonScore.getJSONArray("score");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jObject = (JSONObject) array.get(i);
                    Score score = new Score(jObject.getString("username"),jObject.getString("score"),new Date(jObject.getString("date")),jObject.getString("hunter"));
                    scoreArrayList.add(score);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(scoreArrayList);

        ArrayAdapter<Score> arrayAdapter
                = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1 , scoreArrayList);

        listView.setAdapter(arrayAdapter);

    }

    public void quitActivity(View v) {
        this.finish();
    }
}
