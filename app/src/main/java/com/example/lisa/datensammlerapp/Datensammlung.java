package com.example.lisa.datensammlerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class Datensammlung extends AppCompatActivity {


    private void initGuiElements(){
        Switch swchAc = findViewById(R.id.swchAc);
        Switch swchGy = findViewById(R.id.swchGy);
        Switch swchLo = findViewById(R.id.swchLo);
        Switch swchLi = findViewById(R.id.swchLi);
        Switch swchRe = findViewById(R.id.swchRe);

        TextView acValX = findViewById(R.id.acValX);
        TextView acValY = findViewById(R.id.acValX);
        TextView acValZ = findViewById(R.id.acValX);

        TextView gyValX = findViewById(R.id.gyValX);
        TextView gyValY = findViewById(R.id.gyValX);
        TextView gyValZ = findViewById(R.id.gyValX);

        TextView loValLo = findViewById(R.id.loValLo);
        TextView loValLa = findViewById(R.id.loValLa);

        TextView liValCa = findViewById(R.id.liValCa);

        EditText acHz = findViewById(R.id.acHz);
        EditText gyHz = findViewById(R.id.gyHz);
        EditText loHz = findViewById(R.id.loHz);
        EditText liHz = findViewById(R.id.liHz);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datensammlung);
    }
}
