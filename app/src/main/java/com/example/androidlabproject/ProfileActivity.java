package com.example.androidlabproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class ProfileActivity extends AppCompatActivity {

    private CardView editFirstName,editLastName,editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeViews();

    }


    private void initializeViews() {
        editFirstName=findViewById(R.id.editFirstName);
        editLastName=findViewById(R.id.editLastName);
        editPassword=findViewById(R.id.editPassword);
    }

    public void changeFirstNameClicked(View view) {
        Intent intent=new Intent(ProfileActivity.this,ChangeFirstName.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        intent.putExtra("password",getIntent().getStringExtra("password"));
        intent.putExtra("firstName",getIntent().getStringExtra("firstName"));
        intent.putExtra("lastName",getIntent().getStringExtra("lastName"));
        startActivity(intent);
    }

    public void changePasswordClicked(View view) {
        Intent intent=new Intent(ProfileActivity.this,ChangePassword.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        intent.putExtra("password",getIntent().getStringExtra("password"));
        intent.putExtra("firstName",getIntent().getStringExtra("firstName"));
        intent.putExtra("lastName",getIntent().getStringExtra("lastName"));
        startActivity(intent);
    }

    public void changeLastNameClicked(View view) {
        Intent intent=new Intent(ProfileActivity.this,ChangeLastName.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        intent.putExtra("password",getIntent().getStringExtra("password"));
        intent.putExtra("firstName",getIntent().getStringExtra("firstName"));
        intent.putExtra("lastName",getIntent().getStringExtra("lastName"));
        startActivity(intent);
    }
}