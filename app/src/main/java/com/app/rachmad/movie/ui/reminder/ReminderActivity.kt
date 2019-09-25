package com.app.rachmad.movie.ui.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.rachmad.movie.R
import com.app.rachmad.movie.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val alarmReceiver = AlarmReceiver()
        release_switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                alarmReceiver.releaseRepeatingAlarm(this, getString(R.string.notif_title), getString(R.string.notif_description))
            else
                alarmReceiver.stopReleaseAlarm(this)
        }

        daily_switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                alarmReceiver.dailyRepeatingAlarm(this, getString(R.string.notif_title), getString(R.string.notif_description))
            else
                alarmReceiver.stopDailyAlarm(this)
        }
    }
}
