package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Main8_3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8_3);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MHolder> {

        @NonNull
        @Override
        public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.
                    from(parent.getContext()).inflate(R.layout.item_layout, null));
        }

        @Override
        public void onBindViewHolder(@NonNull MHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    class MHolder extends RecyclerView.ViewHolder {

        public MHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
