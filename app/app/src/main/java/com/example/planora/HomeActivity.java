package com.example.planora;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class HomeActivity extends AppCompatActivity {

    private String name, destination;
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Should match WeatherActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = getIntent().getStringExtra("name");
        destination = getIntent().getStringExtra("destination");

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        TextView tvDestination = findViewById(R.id.tvDestination);

        tvGreeting.setText("Hello, " + name + "! 👋");
        tvDestination.setText("📍 " + destination);

        setupBottomNavigation();
        setupCards();
        fetchWeatherSummary();

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            getSharedPreferences("PlanoraPrefs", MODE_PRIVATE).edit()
                    .putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_itinerary) {
                openItinerary();
                return true;
            } else if (id == R.id.nav_checklist) {
                openChecklist();
                return true;
            } else if (id == R.id.nav_map) {
                openMap();
                return true;
            }
            return id == R.id.nav_home;
        });
    }

    private void setupCards() {
        findViewById(R.id.cardMap).setOnClickListener(v -> openMap());
        findViewById(R.id.cardItinerary).setOnClickListener(v -> openItinerary());
        findViewById(R.id.cardChecklist).setOnClickListener(v -> openChecklist());
        findViewById(R.id.btnRefreshWeather).setOnClickListener(v -> {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("destination", destination);
            startActivity(intent);
        });
    }

    private void openMap() {
        startActivity(new Intent(this, MapActivity.class));
    }

    private void openItinerary() {
        Intent intent = new Intent(this, ItineraryActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }

    private void openChecklist() {
        startActivity(new Intent(this, ChecklistActivity.class));
    }

    private void fetchWeatherSummary() {
        TextView tvTemp = findViewById(R.id.tvWeatherTemp);
        TextView tvDesc = findViewById(R.id.tvWeatherDesc);
        TextView tvEmoji = findViewById(R.id.tvWeatherEmoji);

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + destination + "&appid=" + API_KEY + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double temp = response.getJSONObject("main").getDouble("temp");
                        String desc = response.getJSONArray("weather")
                                .getJSONObject(0).getString("description");
                        String main = response.getJSONArray("weather")
                                .getJSONObject(0).getString("main");

                        tvTemp.setText((int) temp + "°C");
                        tvDesc.setText(desc.substring(0, 1).toUpperCase() + desc.substring(1) + " in " + destination);
                        tvEmoji.setText(getWeatherEmoji(main));
                    } catch (Exception e) {
                        tvDesc.setText("Weather data unavailable");
                    }
                },
                error -> tvDesc.setText("Connect to see weather")
        );
        queue.add(req);
    }

    private String getWeatherEmoji(String main) {
        switch (main) {
            case "Clear": return "☀️";
            case "Clouds": return "☁️";
            case "Rain": return "🌧";
            case "Snow": return "❄️";
            case "Thunderstorm": return "⛈";
            case "Drizzle": return "🌦";
            default: return "🌤";
        }
    }
}