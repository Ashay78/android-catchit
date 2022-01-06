package com.example.projet1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final static int LAUNCH_GAME = 0;
    public final static int LAUNCH_PARAMETERS = 1;

    private TextView textViewScore;

    private String name;
    private String targer;
    private String hunter;
    private String result;
    private String sound;

    JSONObject jsonScore;
    ArrayList<Score> scoreArrayList = new ArrayList<Score>();

    private String resultScore = null;


    /**
     * Create activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewScore = findViewById(R.id.text_score);

        this.loadGameSetting();
    }

    /**
     * When user turn screen save instance
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString("username", this.name);
        savedInstanceState.putString("score", this.resultScore);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Restore instance when user turn screen restore instance
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String playerName = savedInstanceState.getString("username");
        String score = savedInstanceState.getString("score");

        this.resultScore = score;

        if (score != null) {
            this.textViewScore.setText("Last game: " + playerName + " : " + score);
        } else {
            this.textViewScore.setText(playerName + " will play");
        }
    }

    /**
     * When user start game activity
     * Get parameter en give their to the game activity
     * @param v
     */
    public void startGame(View v) {
        ListView listView = (ListView)findViewById(R.id.listView);

        SharedPreferences sharedPreferences= this.getSharedPreferences("gameScore6", Context.MODE_PRIVATE);

        String strJson = sharedPreferences.getString("jsondata","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
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

        Intent intentJeux = new Intent(this, Jeux.class);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("targer", targer);
        b.putString("hunter", hunter);
        b.putString("result", result);
        b.putString("sound", sound);
        b.putString("bestScore", scoreArrayList.get(0).getScore());
        intentJeux.putExtras(b);
        startActivityForResult(intentJeux, LAUNCH_GAME);
    }

    /**
     * Start activity
     * @param v
     */
    public void startParameters(View v) {
        Intent intentParameters = new Intent(this, ParametersActivity.class);
        startActivityForResult(intentParameters, LAUNCH_PARAMETERS);
    }

    /**
     * Start HighScore activity
     * @param v
     */
    public void startHighScore(View v) {
        Intent intentHighScore = new Intent(this, HighScoreActivity.class);
        startActivityForResult(intentHighScore, LAUNCH_PARAMETERS);
    }

    /**
     * Exit application
     * @param v
     */
    public void quit(View v) {
        this.finish();
    }

    /**
     * Load game setting
     */
    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            this.name = sharedPreferences.getString("name", "");
            this.targer = sharedPreferences.getString("target", "");
            this.hunter = sharedPreferences.getString("hunter", "");
            this.result = sharedPreferences.getString("result", "");
            this.sound = sharedPreferences.getString("sound", "");
            this.textViewScore.setText(name + " will play");
        } else {
            this.textViewScore.setText("....");
        }
    }

    /**
     * Function call when activity stop with result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LAUNCH_GAME:
                if(resultCode == Activity.RESULT_OK){
                    String result = data.getStringExtra("result");
                    this.textViewScore.setText("Last game: " + name + " : " + result);
                    this.resultScore = result;
                    this.saveScore(result);
                }
                break;
            case LAUNCH_PARAMETERS:
                if (resultCode == Activity.RESULT_CANCELED) {
                    this.loadGameSetting();
                }
                break;
        }
    }

    /**
     * Save score in the shared preferenced.
     * Score are save in json serialize
     * @param score
     */
    public void saveScore(String score) {
        SharedPreferences sharedPreferences= this.getSharedPreferences("gameScore6", Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            String jsonString = sharedPreferences.getString("jsondata", "0");
            if (jsonString.equals("0")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                JSONArray allGame = new JSONArray();

                editor.putString("jsondata", this.scoreJsonParse(allGame, score));
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    JSONObject json = null;
                    json = new JSONObject(jsonString);
                    JSONArray allGame = json.getJSONArray("score");

                    editor.putString("jsondata", this.scoreJsonParse(allGame, score));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
            }
        }
    }

    /**
     * Parse the score in the json
     * @param allGame
     * @param score
     * @return
     */
    private String scoreJsonParse(JSONArray allGame, String score) {

        JSONObject game = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            game.put("username", this.name);
            game.put("score", score);
            game.put("date", new Date().toString());
            game.put("hunter", this.hunter);
            allGame.put(game);

            json.put("score", allGame);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}