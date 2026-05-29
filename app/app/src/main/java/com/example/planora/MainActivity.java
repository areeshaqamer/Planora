package com.example.planora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText etName, etDestination;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etDestination = findViewById(R.id.etDestination);
        btnStart = findViewById(R.id.btnStart);

        String prefillName = getIntent().getStringExtra("name");
        if (prefillName != null) {
            etName.setText(prefillName);
        }

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();

            if (name.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("destination", destination);
            startActivity(intent);
        });
    }
}