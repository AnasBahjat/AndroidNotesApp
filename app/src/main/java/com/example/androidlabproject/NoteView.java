package com.example.androidlabproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NoteView extends AppCompatActivity{
    private TextView titleTextView ;
    private TextView bodyTextView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        NoteFragment fragment=new NoteFragment();
        Bundle args=new Bundle();
        args.putString("title",getIntent().getStringExtra("title"));
        args.putString("content",getIntent().getStringExtra("content"));
        args.putString("noteID",getIntent().getStringExtra("noteID"));
        args.putString("email", getIntent().getStringExtra("email"));
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.noteFragment,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(NoteView.this,FirstActivity.class);
        intent.putExtra("email",getIntent().getStringExtra("email").toLowerCase());
        startActivity(intent);
    }
}
