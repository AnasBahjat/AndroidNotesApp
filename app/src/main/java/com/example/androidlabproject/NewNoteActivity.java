package com.example.androidlabproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewNoteActivity extends AppCompatActivity {
    private EditText noteTitle,noteContent;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DataBaseHelper dataBaseHelper;
    private String noteTitleStr,noteContentStr,actualTitle,actualContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        noteTitle=findViewById(R.id.noteTitle);
        noteContent=findViewById(R.id.noteContent);
        dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);
    }

    public void saveNoteOnClick(View view) {
        saveNote();
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    private void saveNote() {
        noteTitleStr=noteTitle.getText().toString().trim();
        noteContentStr=noteContent.getText().toString().trim();
        if(noteTitleStr.isEmpty() && noteContentStr.isEmpty()){
            actualTitle="No title";
            actualContent="";
            Log.d("No title , no content","No title , no content");
        }
        else if (noteContentStr.isEmpty()){
            actualContent="";
            actualTitle=noteTitleStr;
            Log.d("no content","no content");
        }
        else if (noteTitleStr.isEmpty()){
            actualTitle="No title";
            actualContent=noteContentStr;
            Log.d("No title","No title");
        }
        else {
            actualTitle=noteTitleStr;
            actualContent=noteContentStr;
            Log.d("there is title , there is content","there is title , there is content");
        }
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String currentDate=simpleDateFormat.format(calendar.getTime());
        if(actualTitle==null){
            actualTitle="No title";
        }
        Note note=new Note(actualTitle,actualContent,currentDate,getIntent().getStringExtra("email"),"0");
        dataBaseHelper.insertNote(note);
        Intent intent=new Intent(NewNoteActivity.this,FirstActivity.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        intent.putExtra("password",getIntent().getStringExtra("password"));
        intent.putExtra("firstName",getIntent().getStringExtra("firstName"));
        intent.putExtra("lastName",getIntent().getStringExtra("lastName"));
        startActivity(intent);
        //finish();
    }


}