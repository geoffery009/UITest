package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;

public class Main7_1Activity extends AppCompatActivity {
    NestedScrollView scrollView;
    MyZuNiFrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7_1);
        layout = findViewById(R.id.content);

        scrollView = findViewById(R.id.scrollView);
    }
}
