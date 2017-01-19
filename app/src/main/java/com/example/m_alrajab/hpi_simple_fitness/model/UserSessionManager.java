package com.example.m_alrajab.hpi_simple_fitness.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by m_alrajab on 12/31/16.
 */
public class UserSessionManager {
    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";
    // Users notfication preference (make variable public to access from outside)
    public static final String KEY_TIME_NOTIFY = "NOTIFY";
    // sharedpref current steps count
    public static final String KEY_CURRENT_USER_STEP_COUNT = "CURRENT_STEP_COUNT";
    // sharedpref current steps count
    public static final String KEY_CURRENT_USER_TARGET = "CURRENT_TARGET";
    public static final String KEY_CURRENT_USER_URI = "CURRENT_URI";
    // sharedpref checkbox motivate me
    public static final String KEY_NOTIFY = "MOTIVATE_ME";
    // sharedpref user current latitude
    public static final String KEY_CURRENT_LAT = "CURRENT_LAT";
    // sharedpref user current longitude
    public static final String KEY_CURRENT_LONG = "CURRENT_LONG";
    public static final String PREFER_NAME = "com.example.m_alrajab.hpifitnessapp.HPI_FITNESS_LOCAL_SESSION_FILE";


    // Current location address
    public static final String ADDRESS_LOC = "Not Known";

    // Shared pref mode
    private final int PRIVATE_MODE = 0;
    // Shared Preferences reference
    private SharedPreferences pref;
    // Editor reference for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context mContext;


    // Constructor
    public UserSessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String name) {
        editor.putBoolean(IS_USER_LOGIN, true)// Storing login value as TRUE
                .putString(KEY_NAME, name).commit();// commit changes
        setNotifyTime(60);
        setKeyCurrentUserTarget(3000);
        setCurrentUserStepCount(0);
        setNotifyMe(false);
    }

    // clear user data
    public void clearUserData() {
        pref.edit().clear().commit();
    }

    public void setCurrentUserStepCount(int b) {
        pref.edit().putInt(KEY_CURRENT_USER_STEP_COUNT, b).commit();
    }

    @NonNull
    public int getCurrentUserStepCount() {
        return pref.getInt(KEY_CURRENT_USER_STEP_COUNT, 0);
    }

    public void setCurrentUserLat(float b) {
        pref.edit().putFloat(KEY_CURRENT_LAT, b).commit();
    }

    @NonNull
    public float getCurrentUserLat() {
        return pref.getFloat(KEY_CURRENT_LAT, 40.6951341f);
    }

    public void setKeyCurrentUserLong(float b) {
        pref.edit().putFloat(KEY_CURRENT_LONG, b).commit();
    }

    @NonNull
    public float getKeyCurrentUserLong() {
        return pref.getFloat(KEY_CURRENT_LONG, -73.9914608f);
    }


    public void setKeyCurrentUserTarget(int b) {
        pref.edit().putInt(KEY_CURRENT_USER_TARGET, b).commit();
    }

    @NonNull
    public int getCurrentUserTarget() {
        return pref.getInt(KEY_CURRENT_USER_TARGET, 3000);
    }

    public void setNotifyMe(boolean b) {
        pref.edit().putBoolean(KEY_NOTIFY, b).commit();
    }

    @NonNull
    public boolean getNotifyMe() {
        return pref.getBoolean(KEY_NOTIFY, false);
    }

    public void setUserPhotoURI(Uri t) {
        pref.edit().putString(KEY_CURRENT_USER_URI, (t != null) ? t.toString() : "http://hpi.com/default.png").commit();

    }

    public String getAddressLoc() {
        return pref.getString(ADDRESS_LOC, "NOT known address yet");
    }

    public void setAddressLoc(String addr) {
        pref.edit().putString(ADDRESS_LOC, addr).commit();
    }

    public String getUserPhotoURI() {
        return pref.getString(KEY_CURRENT_USER_URI, "");
    }

    public void setNotifyTime(int t) {
        pref.edit().putInt(KEY_TIME_NOTIFY, t).commit();
    }

    public int getNotifyTime() {
        return pref.getInt(KEY_TIME_NOTIFY, 60);
    }

    // Clear session details
    @NonNull
    public void logoutUserFromSession() {
        clearUserData();
    }

    // Check for login
    @NonNull
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }


    @NonNull
    public String getUserName() {
        return pref.getString(KEY_NAME, "");
    }
}
