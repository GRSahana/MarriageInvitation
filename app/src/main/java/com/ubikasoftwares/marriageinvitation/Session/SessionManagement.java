package com.ubikasoftwares.marriageinvitation.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {
    private SharedPreferences pref;

    // Editor for Shared preferences
    private Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "My_Login_Status";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String EMP_ID = "EmpId";

    public static final String USERNAME = "UserName";

    public static final String TOKEN = "Token";


    // Email address (make variable public to access from outside)

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String id, String level, String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(EMP_ID, id);

        editor.putString(TOKEN, level);

        editor.putString(USERNAME, name);

        // commit changes
        editor.commit();
    }


    public boolean isUserLoggedIn()
    {
        return this.pref.getBoolean(IS_LOGIN, false);
    }


    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    public String getData() {
        return pref.getString(EMP_ID,"0");
    }

    public String getUserLevel() {
        return pref.getString(TOKEN,"0");

    }

    public String getUSERNAME() {
        return pref.getString(USERNAME,"0");
    }


}
