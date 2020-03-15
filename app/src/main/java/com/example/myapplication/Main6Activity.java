package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;

public class Main6Activity extends AppCompatActivity {
    Button btn;
    MyBtnScroller scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        btn = findViewById(R.id.button8);

        scroller = findViewById(R.id.myscroller);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scroller.skip();
            }
        });
    }
}

