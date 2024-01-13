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


public class SignUp extends AppCompatActivity {
    private EditText email,firstName,lastName,password,confirmPassword;
    private Button signUp;
    private DataBaseHelper dataBaseHelper;
    private String emailStr,firstNameStr,lastNameStr,passwordStr,confirmPasswordStr;

    private AlertDialog.Builder builder;
    private AlertDialog errorDialog;
    private ImageView errorImage,imgErrorDialog;
    private TextView errorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        defineViews();
        onClick();
    }

    private void defineViews() {
        email=findViewById(R.id.email);
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);
        signUp=findViewById(R.id.signUp);
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


    public void onClick(){
        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setBackground(getResources().getDrawable(R.drawable.edit_text));
            }
        });
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void signUpButtonListener(View view) {
        emailStr=email.getText().toString().trim();
        firstNameStr=firstName.getText().toString().trim();
        lastNameStr=lastName.getText().toString().trim();
        passwordStr=password.getText().toString().trim();

        email.setBackground(getResources().getDrawable(R.drawable.edit_text));
        firstName.setBackground(getResources().getDrawable(R.drawable.edit_text));
        lastName.setBackground(getResources().getDrawable(R.drawable.edit_text));
        password.setBackground(getResources().getDrawable(R.drawable.edit_text));
        confirmPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));


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


        confirmPasswordStr=confirmPassword.getText().toString().trim();
        if(emailStr.isEmpty() && firstNameStr.isEmpty() && lastNameStr.isEmpty()  && passwordStr.isEmpty() && confirmPasswordStr.isEmpty()){
            errorTextView.setText("Fill in all above fields !!");
            errorDialog.show();
            email.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            firstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            lastName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            confirmPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }

        else if(emailStr.isEmpty()){
            errorTextView.setText("Fill In Email field..");
            errorDialog.show();
            email.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if(firstNameStr.isEmpty()){
            errorTextView.setText("Fill In First Name field..");
            errorDialog.show();
            firstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));

        }
        else if(lastNameStr.isEmpty()){
            errorTextView.setText("Fill In Last Name field..");
            errorDialog.show();
            lastName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));

        }
        else if(passwordStr.isEmpty()){
            errorTextView.setText("Fill In Password field..");
            errorDialog.show();
            password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));

        }
        else if(confirmPasswordStr.isEmpty()){
            errorTextView.setText("Fill In Confirm Password field..");
            errorDialog.show();
            errorDialog.show();password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));

        }
        else {
            if(!isEmailValid(email.getText().toString().trim())){
                errorTextView.setText("Invalid Email format ..");
                errorDialog.show();
                email.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            }
            else if(firstNameStr.length() < 3 || firstNameStr.length() > 10){
                errorTextView.setText("First name should be at least 3 characters\nand at most 10 characters");
                errorDialog.show();
                firstName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            }
            else if(lastNameStr.length() < 3 || lastNameStr.length() > 10){
                errorTextView.setText("Last name should be at least 3 characters\nand at most 10 characters");
                errorDialog.show();
                lastName.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            }
            else if(!isPasswordValid(passwordStr) || passwordStr.length() < 6 || passwordStr.length() > 12){
                errorTextView.setText(" Incorrect Password format\n It should be at within 6 - 12 characters\n- It must contain One number. \n- One lowercase letter.\n- One uppercase letter.");
                errorDialog.show();
                password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            }
            else if(!passwordStr.equals(confirmPasswordStr)){
                errorTextView.setText("Passwords doesn't match ..");
                errorDialog.show();
                confirmPassword.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            }
            else if(checkEmailIfExist(emailStr)){
                errorTextView.setText("User is already exists ..");
                errorDialog.show();
            }
            else {
                builder = new AlertDialog.Builder(this);
                LayoutInflater inflater2 = LayoutInflater.from(this);
                View dialogView2 = getLayoutInflater().inflate(R.layout.done_dialog, null);
                imgErrorDialog = dialogView2.findViewById(R.id.errorImage);
                errorTextView = dialogView2.findViewById(R.id.errorDialogTxt);
                builder.setView(dialogView2).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataBaseHelper.insertUser(new User(emailStr.toLowerCase(), firstNameStr, lastNameStr, passwordStr));
                        finish();
                    }
                });
                errorDialog = builder.create();
                errorTextView.setText("Account creation Done ..");
                errorDialog.show();
            }
        }
    }

    public boolean checkEmailIfExist(String email){
        Cursor cursor=dataBaseHelper.getAllUsers();
        while(cursor.moveToNext()){
            if(cursor.getString(0).equalsIgnoreCase(email)){
                return true;
            }
        }
        return false ;
    }

    public boolean isEmailValid(String email) {
        String emailFormat = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailFormat);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isPasswordValid(String passwordStr){
        Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)");
        Matcher matcher=pattern.matcher(passwordStr);
        return matcher.find();
    }

    public void emailOnClick(View view) {
        email.setBackground(getResources().getDrawable(R.drawable.edit_text));
    }


        /*public void firstNameOnClick(View view) {
            firstName.setBackground(getResources().getDrawable(R.drawable.edit_text));
        }*/

    public void lastNameOnClick(View view) {
        lastName.setBackground(getResources().getDrawable(R.drawable.edit_text));
    }

    public void passwordOnClick(View view) {
        password.setBackground(getResources().getDrawable(R.drawable.edit_text));
    }

    public void confirmPasswordOnClick(View view) {
        confirmPassword.setBackground(getResources().getDrawable(R.drawable.edit_text));
    }
}