package com.app.rachmad.movie

import android.app.Application
import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.facebook.stetho.Stetho

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Stetho.initializeWithDefaults(this)
    }
    companion object {
        lateinit var context: Context
    }
}

@GlideModule
class MyAppGlideModule : AppGlideModule()