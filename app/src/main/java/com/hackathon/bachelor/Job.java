package com.hackathon.bachelor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Job extends AppCompatActivity {

    public Spinner spinner;
    EditText tv1;
    EditText tv2;
    EditText tv3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        tv1 = findViewById(R.id.type);
        tv2 = findViewById(R.id.desc);
        tv3 = findViewById(R.id.req);

        String item;
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.drop,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                switch (item) {
                    case "Full Time":
                        Log.d("TAG", item);
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        break;

                    case "Part Time":
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.INVISIBLE);
                        break;
            }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void next(View view){
        Intent intent = new
                Intent(this,SetLocation.class);
        if(spinner.getSelectedItem().toString().equals("Full Time")){
            intent.putExtra("Where","Full jobs");
            intent.putExtra("salary",tv1.getText().toString());
            intent.putExtra("desc",tv2.getText().toString());
            intent.putExtra("req",tv3.getText().toString());
        }
        startActivity(intent);
    }
}
