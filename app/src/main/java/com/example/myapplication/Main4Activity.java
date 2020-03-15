package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Main4Activity extends AppCompatActivity {
    Button btn;
    MyLinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        layout = findViewById(R.id.layout);

        btn = findViewById(R.id.button3);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMsg("Button.onClick");
            }
        });
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    layout.onTouchEvent(motionEvent);
                }
                return false;
            }
        });

        final NestedScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Constant.LOG("NestedScrollView onScrollChange");
                if (scrollY == 0) {
                    Constant.LOG("NestedScrollView top");
                    layout.requestDisallowInterceptTouchEvent(false);
                }
            }
        });
    }

    private void showMsg(String s) {
        Log.d("test------", s);
    }

    public void lose(View view) {
    }
}
