package com.example.projet1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParametersActivity extends AppCompatActivity {

    private EditText nameEditText;
    private Spinner targerSpinner;
    private Spinner hunterSpinner;
    private Spinner resultSpinner;
    private Spinner soundSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        this.nameEditText = findViewById(R.id.edit_name);

        this.targerSpinner = (Spinner) findViewById(R.id.spinner_target);
        this.hunterSpinner = (Spinner) findViewById(R.id.spinner_hunter);
        this.resultSpinner = (Spinner) findViewById(R.id.spinner_result);
        this.soundSpinner = (Spinner) findViewById(R.id.spinner_sound);

        Button buttonSave = (Button) this.findViewById(R.id.btn_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParametersActivity.this.doSave(view);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                ParametersActivity.this.finish();
            }
        });

        this.loadGameSetting();


    }

    public void quitActivity(View v) {
        this.finish();
    }

    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            String name = sharedPreferences.getString("name", "");
            String targer = sharedPreferences.getString("target", "");
            String hunter = sharedPreferences.getString("hunter", "");
            String result = sharedPreferences.getString("result", "");
            String sound = sharedPreferences.getString("sound", "");

            this.nameEditText.setText(name);

            ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.typeArray, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapterSound = ArrayAdapter.createFromResource(this, R.array.soundArray, android.R.layout.simple_spinner_item);

            adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterSound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.targerSpinner.setAdapter(adapterType);
            this.hunterSpinner.setAdapter(adapterType);
            this.resultSpinner.setAdapter(adapterType);
            this.soundSpinner.setAdapter(adapterSound);

            int spinnerPosition = adapterType.getPosition(targer);
            this.targerSpinner.setSelection(spinnerPosition);

            spinnerPosition = adapterType.getPosition(hunter);
            this.hunterSpinner.setSelection(spinnerPosition);

            spinnerPosition = adapterType.getPosition(result);
            this.resultSpinner.setSelection(spinnerPosition);

            spinnerPosition = adapterSound.getPosition(sound);
            this.soundSpinner.setSelection(spinnerPosition);
        }
    }

    public void doSave(View view)  {
        SharedPreferences sharedPreferences = this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", this.nameEditText.getText().toString());
        editor.putString("target", this.targerSpinner.getSelectedItem().toString());
        editor.putString("hunter", this.hunterSpinner.getSelectedItem().toString());
        editor.putString("result", this.resultSpinner.getSelectedItem().toString());
        editor.putString("sound", this.soundSpinner.getSelectedItem().toString());

        editor.apply();

        Toast.makeText(this,"Game Setting saved!",Toast.LENGTH_LONG).show();
    }
}
