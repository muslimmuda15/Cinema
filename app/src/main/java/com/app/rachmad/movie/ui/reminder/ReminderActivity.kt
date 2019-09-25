package com.app.rachmad.movie.ui.reminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.app.rachmad.movie.R
import com.app.rachmad.movie.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setDisplayShowTitleEnabled(true)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)

        menu.findItem(R.id.favorite_list).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.change_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
