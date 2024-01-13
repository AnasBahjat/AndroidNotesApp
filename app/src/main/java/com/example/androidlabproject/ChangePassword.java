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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    private EditText newPassword,confirmNewPassword,currentPassword;
    private Button changePasswordBtn;
    private String newPasswordStr,currentPasswordStr;
    private AlertDialog.Builder builder;
    private AlertDialog errorDialog;
    private ImageView errorImage,imgErrorDialog;
    private TextView errorTextView;
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword=findViewById(R.id.newPassword);
        currentPassword=findViewById(R.id.currentPassword);
        confirmNewPassword=findViewById(R.id.confirmNewPassword);
        changePasswordBtn=findViewById(R.id.changePasswordBtn);
        dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);
        sharedPrefManager=SharedPrefManager.getInstance(this);


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
    public void changePassword(View view) {
        newPasswordStr =newPassword.getText().toString().trim();
        currentPasswordStr=currentPassword.getText().toString().trim();
        newPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));
        currentPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));
        confirmNewPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));

        if (newPasswordStr.isEmpty() && currentPasswordStr.isEmpty()){
            newPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            currentPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        if(newPasswordStr.isEmpty()){
            errorTextView.setText("Enter Valid Password !!");
            errorDialog.show();
            newPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if (newPasswordStr.equalsIgnoreCase(getPassword())){
            errorTextView.setText("Unable to change the name,since it's the same ");
            errorDialog.show();
            newPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }

        else if(!isPasswordValid(newPasswordStr) || newPassword.length() < 6 || newPassword.length() > 12){
            errorTextView.setText(" Incorrect Password format\n It should be within 6 - 12 characters\n- It must contain One number. \n- One lowercase letter.\n- One uppercase letter.");
            errorDialog.show();
            newPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }

        else if(!newPasswordStr.equals(confirmNewPassword.getText().toString().trim())){
            errorTextView.setText("Passwords Doesn't match !!");
            errorDialog.show();
            newPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            confirmNewPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
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
                    dataBaseHelper.updatePassword(new User(getIntent().getStringExtra("email"),getFirstName(),getLastName(),newPasswordStr));
                    sharedPrefManager.writeData("password",newPasswordStr);
                    finish();
                }
            });
            errorDialog = builder.create();
            errorTextView.setText("Password changed ..");
            errorDialog.show();
        }
    }

    public boolean isPasswordValid(String passwordStr){
        Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)");
        Matcher matcher=pattern.matcher(passwordStr);
        return matcher.find();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void confirmNewPasswordOnClick(View view) {
        confirmNewPassword.setBackground(getDrawable(R.drawable.edit_text));

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void currentPassowrdOnClick(View view) {
        currentPassword.setBackground(getDrawable(R.drawable.edit_text));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void newPasswordOnClick(View view) {
        newPassword.setBackground(getDrawable(R.drawable.edit_text));
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