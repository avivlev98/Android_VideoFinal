package com.example.videogamecatalog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            DataService.main();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}