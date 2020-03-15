package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

public class Main2Activity extends AppCompatActivity {
    Context mC;
    static final String url = "http://t8.baidu.com/it/u=3571592872,3353494284&fm=79&app=86&f=JPEG?w=1200&h=1290";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle("ScrollView(ListView)");

        mC = this;

        final ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter());
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                listView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewH vH;

            if (view == null) {
                view = LayoutInflater.from(mC).inflate(R.layout.item_iv, null);
                vH = new MyViewH();
                vH.iv = view.findViewById(R.id.imageView);
            } else {
                vH = (MyViewH) view.getTag();
            }

            view.setTag(vH);

            loadIv(vH.iv);

            return view;
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

    class MyViewH {
        ImageView iv;
    }
}
