package com.example.covidcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import HOSPITAL.REGandLOG;
import USER.R_AND_L;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView hospital = (ImageView) findViewById(R.id.hospital);
        ImageView patient = (ImageView) findViewById(R.id.patient);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), REGandLOG.class);
                startActivity(intent);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), R_AND_L.class);
                startActivity(intent);
            }
        });


    }
}