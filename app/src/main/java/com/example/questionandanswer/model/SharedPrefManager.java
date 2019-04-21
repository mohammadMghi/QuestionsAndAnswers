package com.example.questionandanswer.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.questionandanswer.R;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {
    private Context context;
    private final String SHAREDPREF = "my_pref_m";
    public SharedPrefManager(Context context) {
        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHAREDPREF, MODE_PRIVATE);
    }

    public void write(String token){
        SharedPreferences userDetails = context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString("token","Bearer "+token);
        editor.commit();
    }
    public String token(){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
        String lanSettings = prefs.getString("token", null);
        return lanSettings;
    }

    public int getUserID(){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
        int id = prefs.getInt("id",0);
        return id;
    }


    public void clean(){
        SharedPreferences prefs = context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public void saveUserID(int idUser){
        SharedPreferences userDetails = context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putInt("id",idUser);
        editor.commit();
    }


}
