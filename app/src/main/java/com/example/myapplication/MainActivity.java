package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.exoplayer.PlayerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void skip1(View view) {
        startActivity(new Intent(this, Main2Activity.class));
    }

    public void skip2(View view) {
        startActivity(new Intent(this, Main3Activity.class));
    }

    public void skip3(View view) {
        startActivity(new Intent(this, Main4Activity.class));
    }

    public void skip4(View view) {
        startActivity(new Intent(this, Main5Activity.class));
    }

    public void skip5(View view) {
        startActivity(new Intent(this, Main6Activity.class));
    }

    public void skip6(View view) {
        startActivity(new Intent(this, Main7Activity.class));
    }

    public void skip7(View view) {
        startActivity(new Intent(this, Main8Activity.class));
    }

    public void skip8(View view) {
        startActivity(new Intent(this, PlayerActivity.class));
    }
}
