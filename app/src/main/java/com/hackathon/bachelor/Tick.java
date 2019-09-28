package com.hackathon.bachelor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Tick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tick);


    }

    public void next(View view){
        Intent intent = new Intent(this,MainMaps.class);
        startActivity(intent);
    }
}
