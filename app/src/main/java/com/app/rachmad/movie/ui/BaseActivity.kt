package com.app.rachmad.movie.ui

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.viewmodel.ListModel
import com.app.rachmad.movie.widget.FavoriteWidget
import java.util.*

open class BaseActivity: AppCompatActivity() {
    protected val langReceiver by lazy {
        object: BroadcastReceiver(){
            override fun onReceive(c: Context, i: Intent) {
                Log.d("language", "LANGUAGE CHANGED TO " + Locale.getDefault().getCountry())

                viewModel.refreshMovie()
                viewModel.movie(LanguageProvide.getLanguage(c))

                viewModel.refreshTv()
                viewModel.tv(LanguageProvide.getLanguage(c))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetLive = viewModel.getFilmWidgetLive()
        widgetLive.observe(this, androidx.lifecycle.Observer<List<FilmWidgetData>> {
            val intent = Intent(this, FavoriteWidget::class.java)
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE")
            val ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(ComponentName(getApplication(), FavoriteWidget::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS ,ids)
            sendBroadcast(intent)
        })
    }

    val viewModel: ListModel by lazy {
        ViewModelProviders.of(this).get(ListModel::class.java)
    }
}