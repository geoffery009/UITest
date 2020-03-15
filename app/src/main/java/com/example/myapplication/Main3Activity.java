package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Main3Activity extends AppCompatActivity {
    Context mC;
    static final String url = "http://t8.baidu.com/it/u=3571592872,3353494284&fm=79&app=86&f=JPEG?w=1200&h=1290";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setTitle("NestedScrollView(RecycleView)");
        mC = this;

        RecyclerView recyclerView = findViewById(R.id.listview);

        recyclerView.setLayoutManager(new LinearLayoutManager(mC));
        recyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MV> {

        @NonNull
        @Override
        public MV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MV(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iv, null));
        }

        @Override
        public void onBindViewHolder(@NonNull MV holder, int position) {
            loadIv(holder.iv);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }


    private void loadIv(ImageView iv) {
        Glide
                .with(mC)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(iv);
    }

    class MV extends RecyclerView.ViewHolder {
        ImageView iv;

        public MV(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.imageView);
        }
    }
}
