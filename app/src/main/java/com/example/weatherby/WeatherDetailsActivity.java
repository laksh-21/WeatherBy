package com.example.weatherby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WeatherDetailsActivity extends AppCompatActivity {

    private TextView mDetailsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        mDetailsTextView = findViewById(R.id.tv_weather_details);
        Intent creationIntent = getIntent();

        if(creationIntent.hasExtra(Intent.EXTRA_TEXT)){
            mDetailsTextView.setText(creationIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}