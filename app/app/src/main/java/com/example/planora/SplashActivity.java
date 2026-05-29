package com.example.planora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout layout = findViewById(R.id.splashLayout);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        layout.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            SharedPreferences pref = getSharedPreferences("PlanoraPrefs", MODE_PRIVATE);
            boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("name", pref.getString("currentUserName", "Traveler"));
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 2500);
    }
}