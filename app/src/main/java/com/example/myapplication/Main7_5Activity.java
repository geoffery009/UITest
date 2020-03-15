package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Main7_5Activity extends AppCompatActivity {
    MyZuNiRecycleViewWithLongDel recycleViewWithLongDel;
    MyZuNiFrameLayout frameLayout;
    MyAdapter adapter;


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7_5);
        frameLayout = findViewById(R.id.layout);

        recycleViewWithLongDel = findViewById(R.id.list);
        recycleViewWithLongDel.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        recycleViewWithLongDel.setAdapter(adapter);


        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MHolder> {
        private int indexDeleteShow = -1;

        @NonNull
        @Override
        public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.
                    from(parent.getContext()).inflate(R.layout.item_layout_with_del, null));
        }


        @Override
        public void onBindViewHolder(final @NonNull MHolder holder, final int position) {
            holder.deleteFrameLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        Constant.LOG("long click-------" + holder.deleteFrameLayout.isDeleteShow());
                        if (holder.deleteFrameLayout.isDeleteShow()) {
                            indexDeleteShow = position;
                        } else {
                            recycleViewWithLongDel.setLongClick(false);
                            holder.deleteFrameLayout.setLongClick(false);
                            indexDeleteShow = -1;
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                            motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                        if (indexDeleteShow != -1 && position != indexDeleteShow) {
                            indexDeleteShow = -1;
                            recycleViewWithLongDel.setLongClick(false);
                            notifyDataSetChanged();
                        }
                    }
                    return false;
                }
            });
            holder.deleteFrameLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Constant.LOG("long click-------");
                    if (frameLayout.getZuNiTotalY() != 0 || recycleViewWithLongDel.isLongClick()) {
                        return false;
                    }
                    longClickVibrator();
                    recycleViewWithLongDel.setLongClick(true);
                    recycleViewWithLongDel.setShowDelete(indexDeleteShow != -1);
                    holder.deleteFrameLayout.setLongClick(true);
                    return false;
                }
            });

            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recycleViewWithLongDel.setLongClick(false);
                    holder.deleteFrameLayout.setLongClick(false);
                    indexDeleteShow = -1;

                    holder.deleteFrameLayout.scrollTo(0, 0);
                }
            });
            if (position == indexDeleteShow) {
                holder.deleteFrameLayout.scrollTo((int) holder.deleteFrameLayout.getDeleteWidth(), 0);
            } else {
                holder.deleteFrameLayout.scrollTo(0, 0);
            }
        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    class MHolder extends RecyclerView.ViewHolder {
        TextView tvDelete;
        MyLeftScrollToDeleteLinearLayout deleteFrameLayout;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            tvDelete = itemView.findViewById(R.id.item_delete);
            deleteFrameLayout = itemView.findViewById(R.id.item_del_layout);
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) tvDelete.getLayoutParams();
            deleteFrameLayout.setDeleteWidth(getResources().getDimensionPixelSize(R.dimen.delete_width) + p.leftMargin + p.rightMargin);
        }
    }

    private void longClickVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}
