package com.app.rachmad.movie.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.app.rachmad.movie.R
import com.app.rachmad.movie.alarm.AlarmReceiver

class CinemaService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmReceiver = AlarmReceiver()
        alarmReceiver.repeatingAlarm(this, getString(R.string.notif_title), getString(R.string.notif_description))
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
