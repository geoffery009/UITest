package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import hugo.weaving.DebugLog;
import hugo.weaving.internal.Hugo;

public class Main8Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        testHugo("hello", "haha");
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



    //Main8Activity: ⇢ testHugo(s="hello", s1="haha")
    //Main8Activity: ⇠ testHugo [4002ms] = "hello , haha"
    @DebugLog
    public String testHugo(String s, String s1) {
//        SystemClock.sleep(4000);
        return s + " , " + s1;
    }
}
