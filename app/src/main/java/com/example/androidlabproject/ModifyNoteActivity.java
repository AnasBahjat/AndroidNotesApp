package com.example.androidlabproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyNoteActivity extends AppCompatActivity {
    private EditText newTitle;
    private EditText newBody;
    private Button saveChanges;
    private int noteID;
    private DataBaseHelper dataBaseHelper;
    private String titleStr,contentStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        newTitle=findViewById(R.id.newTitle);
        newBody=findViewById(R.id.newNoteBody);
        saveChanges=findViewById(R.id.saveChanges);
        dataBaseHelper=new DataBaseHelper(ModifyNoteActivity.this,"androidLabDB",null,1);

        noteID=getIntent().getIntExtra("noteID",-1);
        String oldNoteTitle=getNoteTitle();
        String oldNoteContent=getNoteContent();
        if(oldNoteTitle.equalsIgnoreCase("No Title") && !oldNoteContent.isEmpty()) {
            newTitle.setHint("New Title");
            newBody.setText(oldNoteContent);
        }

        if(oldNoteContent.isEmpty() && !oldNoteTitle.equalsIgnoreCase("No Title")){
            newBody.setHint("New Note Body");
            newTitle.setText(oldNoteTitle);
        }

        if(oldNoteContent.isEmpty() && oldNoteTitle.isEmpty()){
            newTitle.setHint("New Title");
            newBody.setHint("New Note Body");
        }

        if(!oldNoteTitle.equalsIgnoreCase("No Title") && !oldNoteContent.isEmpty()){
            newTitle.setText(getNoteTitle());
            newBody.setText(getNoteContent());
        }




        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNote();
    }

    public void updateNote(){
        if(newTitle.getText().toString().trim().isEmpty() && newBody.getText().toString().trim().isEmpty()){
            titleStr="No title";
            contentStr="";
        }
        else if(newBody.getText().toString().trim().isEmpty()){
            contentStr="";
            titleStr=newTitle.getText().toString().trim();
        }
        else if(newTitle.getText().toString().trim().isEmpty()){
            titleStr="No title";
            contentStr=newBody.getText().toString().trim();
        }
        else {
            titleStr=newTitle.getText().toString().trim();
            contentStr=newBody.getText().toString().trim();
        }
        dataBaseHelper.updateNote(titleStr,contentStr,noteID);
        Log.d("new title : "+titleStr+" new content : "+contentStr +"Note id : "+noteID ,"new title : "+titleStr+" new content : "+contentStr);
        Intent intent=new Intent(ModifyNoteActivity.this,FirstActivity.class);
        intent.putExtra("email",getIntent().getStringExtra("email"));
        startActivity(intent);
    }

    private String getNoteContent() {
        Cursor cursor = dataBaseHelper.getNoteContent(noteID);
        String content="";
        if(cursor.moveToNext()){
            content= cursor.getString(0);
        }
        return content;
    }

    public String getNoteTitle() {
        Cursor cursor = dataBaseHelper.getNoteTitle(noteID);
        String title="";
        if(cursor.moveToNext()){
            title=cursor.getString(0);
        }
        return title;
    }
}