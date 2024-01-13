package com.example.androidlabproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String sharedPreferenceName="My shared preference";
    private static final int sharedPreferenceMode= Context.MODE_PRIVATE;
    private static SharedPrefManager ourInstance=null;
    private static SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor editor=null;
    private SharedPrefManager(Context context){
        sharedPreferences= context.getSharedPreferences(sharedPreferenceName,sharedPreferenceMode);
        editor= sharedPreferences.edit();
    }

    static SharedPrefManager getInstance(Context context){
        if(ourInstance != null){
            return ourInstance;
        }
        ourInstance=new SharedPrefManager(context);
        return ourInstance;
    }

    public boolean writeUsernameAndPassword(String username, String password){
        editor.putString("username",username);
        editor.putString("password",password);
        return editor.commit();
    }

    public boolean writeData(String key,String value){
        editor.putString(key,value);
        return editor.commit();
    }

    public String readData(String key){
        return sharedPreferences.getString(key,null);
    }
    public String readUsername(){
        return sharedPreferences.getString("username",null);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
    public String readPassword(){
        return sharedPreferences.getString("password",null);
    }
}