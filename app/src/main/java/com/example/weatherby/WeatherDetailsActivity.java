package com.example.weatherby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WeatherDetailsActivity extends AppCompatActivity {

    private TextView mDetailsTextView;
    private String weatherText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        mDetailsTextView = findViewById(R.id.tv_weather_details);
        Intent creationIntent = getIntent();


        if(creationIntent.hasExtra(Intent.EXTRA_TEXT)){
            weatherText = creationIntent.getStringExtra(Intent.EXTRA_TEXT);
            mDetailsTextView.setText(weatherText);
        }
    }

    private void shareWeather(){
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(weatherText)
                .getIntent();
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.detaal_share_btn:
                shareWeather();
                return true;
            case R.id.settings_btn:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}