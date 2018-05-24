package com.example.loginwithretrofit.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.loginwithretrofit.utils.Config;

public class UserPrefs {

	private static final String USER_PREFS = "USER_PREFS";
	private SharedPreferences userSharedPrefs;
	private SharedPreferences.Editor prefsEditor;

	public UserPrefs(Context context) {
		this.userSharedPrefs = context.getSharedPreferences(USER_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = userSharedPrefs.edit();
	}

	public int getUserId() {
		return userSharedPrefs.getInt(Config.KEY_ID, 0);
	}

	public void setUserId(int userId) {
		prefsEditor.putInt(Config.KEY_ID, userId).commit();
	}

	public String getUserName() {
		return userSharedPrefs.getString(Config.KEY_NAME, "");
	}

	public void setUserName(String userName) {
		prefsEditor.putString(Config.KEY_NAME, userName).commit();
	}

	public String getUserImage() {
		return userSharedPrefs.getString(Config.KEY_IMAGE, "");
	}

	public void setUserImage(String userImage) {
		prefsEditor.putString(Config.KEY_IMAGE, userImage).commit();
	}

	public String getUserEmail() {
		return userSharedPrefs.getString(Config.KEY_EMAIL, "");
	}

	public void setUserEmail(String userEmail) {
		prefsEditor.putString(Config.KEY_EMAIL, userEmail).commit();
	}

	public String getUserPassword() {
		return userSharedPrefs.getString(Config.KEY_PASSWORD, "");
	}

	public void setUserPassword(String userPassword) {
		prefsEditor.putString(Config.KEY_PASSWORD, userPassword).commit();
	}

}
