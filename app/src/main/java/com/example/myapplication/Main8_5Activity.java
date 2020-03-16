package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Main8_5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8_5);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MHolder> {
        private int deleteShowIndex = -1;
        private MyDragToDeleteLayout tempOpenView;

        @NonNull
        @Override
        public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.
                    from(parent.getContext()).inflate(R.layout.item_layout_with_drag_del, null));
        }

        @Override
        public void onBindViewHolder(@NonNull final MHolder holder, final int position) {
            holder.dragToDeleteLayout.setOnDeleteListener(new MyDragToDeleteLayout.OnDeleteListener() {
                @Override
                public void onLayoutChange() {
                    if (!holder.dragToDeleteLayout.isCloseDelete()) {
                        deleteShowIndex = position;
                        tempOpenView = holder.dragToDeleteLayout;
                    }
                }

                @Override
                public void onSliding() {
                    if (deleteShowIndex != -1 && tempOpenView != null && deleteShowIndex != position) {
                        tempOpenView.closeDelete(true);
                        deleteShowIndex = -1;
                    }
                }
            });
            if (position == deleteShowIndex) {
                holder.dragToDeleteLayout.openDelete(false);
            } else {
                holder.dragToDeleteLayout.closeDelete(false);
            }
            Constant.LOG(" show index: " + deleteShowIndex);
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteShowIndex = -1;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    class MHolder extends RecyclerView.ViewHolder {
        MyDragToDeleteLayout dragToDeleteLayout;
        TextView tvDelete;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            dragToDeleteLayout = itemView.findViewById(R.id.item_del_layout);
            tvDelete = itemView.findViewById(R.id.item_delete);
        }
    }
}
