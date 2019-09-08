package com.app.rachmad.movie

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class App: Application() {

}

@GlideModule
class MyAppGlideModule : AppGlideModule()