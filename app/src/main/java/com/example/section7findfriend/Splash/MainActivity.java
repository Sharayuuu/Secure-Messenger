package com.example.section7findfriend.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.section7findfriend.R;

import java.util.Timer;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button btn;
    int timeout=5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}