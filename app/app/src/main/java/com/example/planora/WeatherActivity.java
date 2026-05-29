package com.example.planora;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    // ⚠️ REPLACE with your free key from openweathermap.org
    private static final String API_KEY = "YOUR_API_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String destination = getIntent().getStringExtra("destination");

        TextView tvCity = findViewById(R.id.tvCity);
        TextView tvTemp = findViewById(R.id.tvTemp);
        TextView tvDesc = findViewById(R.id.tvDesc);
        TextView tvHumidity = findViewById(R.id.tvHumidity);
        TextView tvIcon = findViewById(R.id.tvIcon);
        TextView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        tvCity.setText(destination);

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + destination + "&appid=" + API_KEY + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double temp = response.getJSONObject("main").getDouble("temp");
                        int humidity = response.getJSONObject("main").getInt("humidity");
                        String desc = response.getJSONArray("weather")
                                .getJSONObject(0).getString("description");
                        String main = response.getJSONArray("weather")
                                .getJSONObject(0).getString("main");

                        tvTemp.setText((int) temp + "°C");
                        tvDesc.setText(desc.substring(0, 1).toUpperCase() + desc.substring(1));
                        tvHumidity.setText("💧 Humidity: " + humidity + "%");
                        tvIcon.setText(getWeatherEmoji(main));
                    } catch (Exception e) {
                        tvDesc.setText("Could not parse weather data");
                    }
                },
                error -> tvDesc.setText("Error: Check API key or internet connection")
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