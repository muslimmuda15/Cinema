package com.app.rachmad.movie.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieBaseData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.repository.MovieRepository
import com.app.rachmad.movie.ui.MainActivity
import com.app.rachmad.movie.webservice.MovieSite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService

const val NOTIF_TITLE = "NotifTitle"
const val NOTIF_MESSAGE = "NotifMessage"
const val NOTIF_TYPE = "NotifType"
class AlarmReceiver : BroadcastReceiver() {
    var releaseAlarm: AlarmManager? = null
    var dailyAlarm: AlarmManager? = null

    var releasePendingIntent: PendingIntent? = null
    var dailyPendingIntent: PendingIntent? = null
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(NOTIF_TITLE)
        val message = intent.getStringExtra(NOTIF_MESSAGE)
        val type = intent.getIntExtra(NOTIF_TYPE, 0)

        if(type == 1)
            showAlarmNotification(context, title ?: "", message ?: "")
        else if(type == 2)
            getMovieData(context)
    }

    fun getMovieData(c: Context){
        val df = SimpleDateFormat("yyyy-mm-dd", Locale.US)
        val now = df.format(Date())
        val service = MovieSite.connect()
        val call = service.movieAt(now, now, LanguageProvide.getLanguage(c))
        call.enqueue(object : Callback<MovieBaseData> {
            override fun onFailure(call: Call<MovieBaseData>, t: Throwable) {
                showAlarmNotification(c, "No movie can display this day", t.message ?: "")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MovieBaseData>, response: Response<MovieBaseData>) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        if (it.total_pages > 0) {
                            showAlarmNotification(c, it.results[0].title, it.results[0].overview)
                        } else {
                            showAlarmNotification(c, "No movie can display this day", "No data")
                        }
                    } ?: run {
                        showAlarmNotification(c, "No movie can display this day", "No data body")
                    }
                }
                else{
                    showAlarmNotification(c, "Check your connection", "No data")
                }
            }
        })
    }

    fun releaseRepeatingAlarm(c: Context, title: String, message: String){
        releaseAlarm = c.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(c, AlarmReceiver::class.java)
        intent.putExtra(NOTIF_TITLE, title)
        intent.putExtra(NOTIF_MESSAGE, message)
        intent.putExtra(NOTIF_TYPE, 1)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        releasePendingIntent = PendingIntent.getBroadcast(c, 1, intent, 0)
        releaseAlarm?.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, releasePendingIntent)
    }

    fun dailyRepeatingAlarm(c: Context, title: String, message: String){
        dailyAlarm = c.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(c, AlarmReceiver::class.java)
        intent.putExtra(NOTIF_TITLE, title)
        intent.putExtra(NOTIF_MESSAGE, message)
        intent.putExtra(NOTIF_TYPE, 2)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        dailyPendingIntent = PendingIntent.getBroadcast(c, 2, intent, 0)
        dailyAlarm?.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, dailyPendingIntent)
    }

    fun stopReleaseAlarm(c: Context){
        releaseAlarm?.cancel(releasePendingIntent)
    }

    fun stopDailyAlarm(c: Context){
        dailyAlarm?.cancel(dailyPendingIntent)
    }

    private fun showAlarmNotification(c: Context, title: String, message: String){
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Movie Channel"

        val notificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(c, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(c, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.lightColor = ContextCompat.getColor(c, R.color.colorPrimary)

            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(System.currentTimeMillis().toInt(),
                    NotificationCompat.Builder(c, CHANNEL_ID)
                            .setChannelId(CHANNEL_ID)
                            .setContentTitle(title)
                            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                            .setContentText(message)
                            .setSmallIcon(R.drawable.ic_live_tv_black_24dp)
                            .setLargeIcon(BitmapFactory.decodeResource(c.resources, R.drawable.ic_live_tv_black_24dp))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setVibrate(longArrayOf(100, 0, 100, 100, 100, 100, 0, 100, 0, 100))
                            .setColor(ContextCompat.getColor(c, R.color.colorPrimary))
                            .build())

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationManager.notify(System.currentTimeMillis().toInt(), NotificationCompat.Builder(c, System.currentTimeMillis().toString())
                    .setContentTitle(title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_live_tv_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(c.resources, R.drawable.ic_live_tv_black_24dp))
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setVibrate(longArrayOf(100, 0, 100, 100, 100, 100, 0, 100, 0, 100))
                    .setColor(ContextCompat.getColor(c, R.color.colorPrimary))
                    .build())
        } else {
            notificationManager.notify(System.currentTimeMillis().toInt(), NotificationCompat.Builder(c, "1")
                    .setContentTitle(title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(longArrayOf(100, 0, 100, 100, 100, 100, 0, 100, 0, 100))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .build())
        }
    }
}
