package com.example.planora;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {

    ArrayList<ChecklistItem> items = new ArrayList<>();
    ChecklistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Pre-load essential travel items
        String[] defaults = {"🛂 Passport", "✈️ Flight Tickets", "🏨 Hotel Booking",
                "💊 Medicines", "💳 Credit Card", "🔌 Charger & Adaptor",
                "👗 Clothes (5 days)", "🧴 Toiletries", "📸 Camera", "🗺 Offline Maps"};
        for (String d : defaults) items.add(new ChecklistItem(d));

        RecyclerView rv = findViewById(R.id.rvChecklist);
        adapter = new ChecklistAdapter(items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        EditText etItem = findViewById(R.id.etItem);
        Button btnAdd = findViewById(R.id.btnAdd);
        TextView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            String text = etItem.getText().toString().trim();
            if (!text.isEmpty()) {
                items.add(new ChecklistItem(text));
                adapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                rv.scrollToPosition(items.size() - 1);
            } else {
                Toast.makeText(this, "Enter an item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.VH> {
        ArrayList<ChecklistItem> list;
        ChecklistAdapter(ArrayList<ChecklistItem> list) { this.list = list; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checklist, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            ChecklistItem item = list.get(position);
            holder.cb.setOnCheckedChangeListener(null);
            holder.cb.setText(item.name);
            holder.cb.setChecked(item.checked);
            applyStyle(holder.cb, item.checked);

            holder.cb.setOnCheckedChangeListener((btn, isChecked) -> {
                item.checked = isChecked;
                applyStyle(holder.cb, isChecked);
            });
        }

        private void applyStyle(CheckBox cb, boolean isChecked) {
            if (isChecked) {
                cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                cb.setAlpha(0.6f);
            } else {
                cb.setPaintFlags(cb.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                cb.setAlpha(1.0f);
            }
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            CheckBox cb;
            VH(View v) { super(v); cb = v.findViewById(R.id.cbItem); }
        }
    }
}