package com.app.rachmad.movie.helper

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.util.*

object LanguageProvide {
    fun getLangIndex(c: Context?): Int {
        c?.let {
            val locale = ConfigurationCompat.getLocales(c.resources.configuration)[0]
            when (locale.toString()) {
                "en_US" -> {
                    return 0
                }
                "in_ID" -> {
                    return 1
                }
                else -> {
                    return 0
                }
            }
        } ?: run {
            return 0
        }
    }

    fun getLanguage(c: Context?): String {
        c?.let {
//            val locale = ConfigurationCompat.getLocales(c.resources.configuration)[0]
            val locale = Locale.getDefault().getCountry()
            when (locale.toString()) {
                "US" -> {
                    return "en-US"
                }
                "ID" -> {
                    return "id"
                }
                else -> {
                    return "en-US"
                }
            }
        } ?: run {
            return "en-US"
        }
    }
}