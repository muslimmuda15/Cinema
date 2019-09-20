package com.app.rachmad.movie.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(i: Intent): RemoteViewsFactory {
        return StackRemoteViewFactory(this.getApplicationContext())
    }
}