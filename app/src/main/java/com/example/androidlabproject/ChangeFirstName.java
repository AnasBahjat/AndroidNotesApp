package com.example.androidlabproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangeFirstName extends AppCompatActivity {
    private EditText newFirstName,currentPassword;
    private Button  changePasswordBtn;
    private String newUsernameStr,currentPasswordStr;
    private AlertDialog.Builder builder;
    private AlertDialog errorDialog;
    private ImageView errorImage,imgErrorDialog;
    private TextView errorTextView;
    private DataBaseHelper dataBaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_first_name);
        newFirstName=findViewById(R.id.newPassword);
        currentPassword=findViewById(R.id.currentPassword);
        changePasswordBtn=findViewById(R.id.changePasswordBtn);
        dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);



        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        imgErrorDialog=dialogView.findViewById(R.id.errorImage);
        errorTextView=dialogView.findViewById(R.id.errorDialogTxt);
        builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        errorDialog = builder.create();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void changeFirstName(View view) {
        newUsernameStr=newFirstName.getText().toString().trim();
        currentPasswordStr=currentPassword.getText().toString().trim();
        newFirstName.setBackground(getResources().getDrawable(R.drawable.edit_text));
        currentPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));

        if (newUsernameStr.isEmpty() && currentPasswordStr.isEmpty()){
            newFirstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            currentPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        if(newUsernameStr.isEmpty()){
            errorTextView.setText("Enter Valid Username !!");
            errorDialog.show();
            newFirstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if (newUsernameStr.equalsIgnoreCase(getFirstName())){
            errorTextView.setText("Unable to change the name,since it's the same ");
            errorDialog.show();
            newFirstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if (currentPasswordStr.isEmpty() || !currentPasswordStr.equals(getPassword())){
            errorTextView.setText("Wrong current password !!");
            errorDialog.show();
            currentPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }

        else {
            builder = new AlertDialog.Builder(this);
            LayoutInflater inflater2 = LayoutInflater.from(this);
            View dialogView2 = getLayoutInflater().inflate(R.layout.done_dialog, null);
            imgErrorDialog = dialogView2.findViewById(R.id.errorImage);
            errorTextView = dialogView2.findViewById(R.id.errorDialogTxt);
            dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);
            builder.setView(dialogView2).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dataBaseHelper.updateFirstname(new User(getIntent().getStringExtra("email"),newUsernameStr,getLastName(),getPassword()));
                    finish();
                }
            });
            errorDialog = builder.create();
            errorTextView.setText("First Name changed ..");
            errorDialog.show();
        }
    }
    public void currentPassowrdOnClick(View view) {
        currentPassword.setBackground(getDrawable(R.drawable.edit_text));
    }

    public void newFirstNameOnClick(View view) {
        newFirstName.setBackground(getDrawable(R.drawable.edit_text));
    }
    public String getPassword(){
        Cursor cursor=dataBaseHelper.getPassword(getIntent().getStringExtra("email"));
        String password="";
        if(cursor.moveToNext()){
            password=cursor.getString(0);
        }
        return password;
    }
    public String getFirstName() {
        Cursor cursor=dataBaseHelper.getFirstName(getIntent().getStringExtra("email"));
        String firstName="";
        if(cursor.moveToNext()){
            firstName=cursor.getString(0);
        }
        return firstName;
    }

    public String getLastName() {
        Cursor cursor=dataBaseHelper.getLastName(getIntent().getStringExtra("email"));
        String lastName="";
        if(cursor.moveToNext()){
            lastName=cursor.getString(0);
        }
        return lastName;
    }

}