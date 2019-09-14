package com.app.rachmad.movie.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.viewmodel.ListModel
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
    val viewModel: ListModel by lazy {
        ViewModelProviders.of(this).get(ListModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}