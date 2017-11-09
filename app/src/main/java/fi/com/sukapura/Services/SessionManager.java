package fi.com.sukapura.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import fi.com.sukapura.Auth;

/**
 * Created by _fi on 10/13/2017.
 */

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "LoginSeason";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String ID = "ID";
    public static final String ACCESS = "access";
    public static final String DEVICE_ID = "device_id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSeason(String id, String access, String device_id){
        Log.d("SessionManager", "Save ID: " + id);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(ID, id);
        editor.putString(ACCESS, access);
        editor.putString(DEVICE_ID, device_id);
        editor.commit();
    }

    public boolean checkUserLogin(){
        if(!this.isUserLoggedIn()){
            logoutPage(_context);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ID, pref.getString(ID, null));
        user.put(ACCESS, pref.getString(ACCESS, null));
        user.put(DEVICE_ID, pref.getString(DEVICE_ID, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        logoutPage(_context);
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    private void logoutPage(Context _context){
        Intent i = new Intent(_context, Auth.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
