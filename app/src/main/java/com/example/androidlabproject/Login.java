package com.example.androidlabproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private EditText email,password;
    private CheckBox rememberMe;
    private Button loginButton;
    private SharedPrefManager sharedPrefManager;

    private AlertDialog.Builder builder;

    private AlertDialog errorDialog;
    private ImageView errorImage,imgErrorDialog;
    private TextView errorTextView;
    private DataBaseHelper dataBaseHelper;
    private String firstName,lastName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        defineViews();
        isRememberMeEnabled();
    }
    private void defineViews() {
        email=findViewById(R.id.username);
        password=findViewById(R.id.password);
        rememberMe=findViewById(R.id.rememberMe);
        loginButton=findViewById(R.id.loginButton);
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


    public void removeAllSharedPreferencesData(){
        sharedPrefManager.clear();
    }

    private void isRememberMeEnabled() {
        if(getIntent().getIntExtra("LogoutType",0)==1){
            email.setText("");
            password.setText("");
            sharedPrefManager.writeData("username","");
            sharedPrefManager.writeData("password","");
        }
        else {
            String usernameStr=sharedPrefManager.readUsername();
            String passwordStr=sharedPrefManager.readPassword();
            email.setText(usernameStr);
            password.setText(passwordStr);
        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void loginListener(View view) {
        if(email.getText().toString().trim().isEmpty()){
            errorTextView.setText("Fill in the email field  !!");
            errorDialog.show();
            email.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if (password.getText().toString().trim().isEmpty()){
            errorTextView.setText("Fill in the password field  !!");
            errorDialog.show();
            password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else if(!checkEmailAndPassword()){
            errorTextView.setText("Wrong email or password ..");
            errorDialog.show();
            email.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
            password.setBackground(getResources().getDrawable(R.drawable.error_edit_text));
        }
        else {
            if(rememberMe.isChecked() &&(!email.getText().toString().trim().isEmpty() || !password.getText().toString().trim().isEmpty())){
                sharedPrefManager.writeUsernameAndPassword(email.getText().toString().trim(),password.getText().toString().trim());
            }
            else {
                sharedPrefManager.writeUsernameAndPassword("","");
            }
            Intent intent=new Intent(Login.this,FirstActivity.class);
            intent.putExtra("email",email.getText().toString().trim().toLowerCase());
            intent.putExtra("password",password.getText().toString().trim());
            intent.putExtra("firstName",firstName);
            intent.putExtra("lastName",lastName);
            startActivity(intent);
        }
    }

    public boolean checkEmailAndPassword(){
        Cursor cursor=dataBaseHelper.getAllUsers();
        while(cursor.moveToNext()){
            if(cursor.getString(0).equalsIgnoreCase(email.getText().toString().trim()) && cursor.getString(3).equals(password.getText().toString().trim())){
                //sharedPrefManager.writeData("firstName",cursor.getString(1));
                //sharedPrefManager.writeData("lastName",cursor.getString(2));
                firstName=cursor.getString(1);
                lastName=cursor.getString(2);
                return true;
            }
        }
        return false ;
    }
    public void signUp(View view) {
        Intent intent=new Intent(Login.this,SignUp.class);
        startActivity(intent);
    }

    public void emailOnClick(View view) {
        email.setBackground(getResources().getDrawable(R.drawable.edit_text));
    }

    public void passwordOnClick(View view) {
        password.setBackground(getResources().getDrawable(R.drawable.edit_text));

    }
}