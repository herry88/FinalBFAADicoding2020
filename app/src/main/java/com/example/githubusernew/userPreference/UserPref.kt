package com.example.githubusernew.userPreference

import android.content.Context

class UserPref(context: Context) {
    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val DAILY_REMINDER = "daily_reminder"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    //coming soon
}