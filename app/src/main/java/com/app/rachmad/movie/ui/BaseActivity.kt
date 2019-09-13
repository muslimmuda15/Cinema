package com.app.rachmad.movie.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.rachmad.movie.viewmodel.ListModel

open class BaseActivity: AppCompatActivity() {
    val viewModel: ListModel by lazy {
        ViewModelProviders.of(this).get(ListModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}