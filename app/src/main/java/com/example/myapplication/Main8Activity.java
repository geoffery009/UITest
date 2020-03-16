package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main8Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
    }

    public void s1(View view) {
        startActivity(new Intent(this, Main8_1Activity.class));
    }
    public void s2(View view) {
        startActivity(new Intent(this, Main8_2Activity.class));
    }
    public void s3(View view) {
        startActivity(new Intent(this, Main8_3Activity.class));
    }
    public void s4(View view) {
        startActivity(new Intent(this, Main8_4Activity.class));
    }
    public void s5(View view) {
        startActivity(new Intent(this, Main8_5Activity.class));
    }
}
