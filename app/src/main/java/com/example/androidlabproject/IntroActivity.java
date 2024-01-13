package com.example.androidlabproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
    private Animation top, bottom,right;
    private ImageView img;
    private TextView txt1,txt2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        right=AnimationUtils.loadAnimation(this,R.anim.right_animation);
        img=findViewById(R.id.img);
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);

        img.setAnimation(top);
        txt1.setAnimation(bottom);
        txt2.setAnimation(right);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // i chang it to open my work screen so if i forget put this(LoginScreen.class) here.
                Intent intent = new Intent(IntroActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}