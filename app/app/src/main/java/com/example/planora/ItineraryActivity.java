package com.example.planora;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ItineraryActivity extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<>();
    ItineraryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        String destination = getIntent().getStringExtra("destination");

        // Add sample AI suggestions
        items.add("🛬 Arrive at " + destination + " airport");
        items.add("🏨 Check in to hotel");
        items.add("🍽 Try local cuisine");
        items.add("🏛 Visit top attractions");
        items.add("🛒 Shopping at local market");

        RecyclerView rv = findViewById(R.id.rvItinerary);
        adapter = new ItineraryAdapter(items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        EditText etActivity = findViewById(R.id.etActivity);
        Button btnAdd = findViewById(R.id.btnAdd);
        TextView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            String text = etActivity.getText().toString().trim();
            if (!text.isEmpty()) {
                items.add(text);
                adapter.notifyItemInserted(items.size() - 1);
                rv.scrollToPosition(items.size() - 1);
                etActivity.setText("");
            } else {
                Toast.makeText(this, "Enter an activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.VH> {
        ArrayList<String> list;
        ItineraryAdapter(ArrayList<String> list) { this.list = list; }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_itinerary, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.tv.setText(list.get(position));
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tv;
            VH(View v) { super(v); tv = v.findViewById(R.id.tvActivityText); }
        }
    }
}