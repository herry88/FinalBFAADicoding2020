package com.example.githubusernew.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusernew.R
import com.example.githubusernew.userPreference.UserPref
import com.example.githubusernew.util.dailyReminder.DailyReminder
import kotlinx.android.synthetic.main.activity_setting.*

/*
    author : Herry Prasetyo
    Summary : Dicoding
 */

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var userPreferences: UserPref
    private lateinit var dailyReminder: DailyReminder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        dailyReminder = DailyReminder()
        userPreferences =
            UserPref(
                this
            )
        switch_daily_reminder.isChecked= dailyReminder.isAlarmSet(this)
        switch_daily_reminder.setOnCheckedChangeListener{
            _, isChecked ->
            if (isChecked){
                dailyReminder.setRepeatingAlarm(this)
                userPreferences.setDailyReminder(true)
                Toast.makeText(this, getString(R.string.switch_reminderOn), Toast.LENGTH_SHORT).show()
            }else{
                dailyReminder.cancelRepeatingAlarm(this)
                userPreferences.setDailyReminder(false)
                Toast.makeText(this, getString(R.string.swithc_reminderOFF),Toast.LENGTH_SHORT).show()
            }
        }

        btn_test_notify.setOnClickListener(this)
    }

    override fun onClick( v : View) {
        when(v.id){
            R.id.btn_test_notify->{
                dailyReminder.showNotification(this)
                Toast.makeText(this, getString(R.string.testNotify), Toast.LENGTH_SHORT).show()
            }
        }
    }

}
