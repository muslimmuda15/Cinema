package com.app.rachmad.movie.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.app.rachmad.movie.App
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.sqlite.model.FavoriteModel
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.transition.Transition
import android.content.ComponentName
import android.appwidget.AppWidgetManager
import android.net.Uri
import com.app.rachmad.movie.`object`.FilmWidgetData
import java.net.URL


class StackRemoteViewFactory: RemoteViewsService.RemoteViewsFactory, LifecycleOwner {
    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }

    private var mWidgetItems: ArrayList<FilmWidgetData> = ArrayList()
    private var mContext: Context

    constructor(context: Context){
        mContext = context
    }

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        val favoriteModel = FavoriteModel(mContext)
        val favorite = favoriteModel.getWidgetFavorite()
        favorite?.forEach {
            mWidgetItems.add(it)
            Log.d("favorite", it.name)
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.getPackageName(), R.layout.widget_item)

//        val appWidgetManager = AppWidgetManager.getInstance(mContext)
//        val appWidgetIds = appWidgetManager.getAppWidgetIds(
//                ComponentName(mContext, FavoriteWidget::class.java))
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.image_widget)
//        val image: AppWidgetTarget = object : AppWidgetTarget(mContext, R.id.image_widget, rv, *appWidgetIds) {
//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                super.onResourceReady(resource, transition)
//            }
//        }
//
//        GlideApp
//                .with(mContext)
//                .asBitmap()
//                .load(BuildConfig.IMAGE_URL + mWidgetItems[position].backdrop_path)
//                .centerCrop()
//                .into(image)

//        val url = URL(BuildConfig.IMAGE_URL + mWidgetItems[position].backdrop_path)
//        rv.setImageViewUri(R.id.image_widget, Uri.parse( url.toURI().toString()))

        if(mWidgetItems.size > 0) {
            val bitmap = GlideApp
                    .with(mContext)
                    .asBitmap()
                    .load(BuildConfig.IMAGE_URL + mWidgetItems[position].image)
                    .centerCrop()
                    .submit()
                    .get()

            rv.setImageViewBitmap(R.id.image_widget, bitmap)
            rv.setTextViewText(R.id.title_widget, mWidgetItems[position].name)

            val extras = Bundle()
            extras.putInt(EXTRA_ITEM, position)
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)

            rv.setOnClickFillInIntent(R.id.image_widget, fillInIntent)
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}