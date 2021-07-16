package com.example.homepharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class report extends AppCompatActivity {

    private Button mReport;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Bottom Navigation----------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Clab_report);

        //Bottom Navigation Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Cadd_pres:
                        startActivity(new Intent(getApplicationContext(),AddPrescription.class));
                        return true;
                    case R.id.Clab_report:
                        //startActivity(new Intent(getApplicationContext(),AdminPanel.class));
                        return true;
                    case R.id.Cadd_groc:
                        //startActivity(new Intent(getApplicationContext(),Grocery.class));
                        return true;
                    case R.id.Clog_out:
                        startActivity(new Intent(getApplicationContext(),cLogin.class));
                        return true;
                }
                return false;
            }
        });
        //-----------------------------------

        mReport = findViewById(R.id.available_report);

        mReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImagesActivity1();
            }
        });
    }
    private void openImagesActivity1(){
        Intent intent =new Intent(this,Retrive.class);
        startActivity(intent);
    }
}