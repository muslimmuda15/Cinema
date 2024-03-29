package com.app.rachmad.movie.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.app.rachmad.movie.R
import android.provider.SyncStateContract.Helpers.update
import android.widget.Toast
import androidx.core.app.NotificationCompat.getExtras
import android.provider.SyncStateContract.Helpers.update

const val EXTRA_ITEM = "ExtraItem"
const val TOAST_ACTION = "ToastAction"
const val WIDGET_IDS_KEY = "WidgetIdsKey"
const val WIDGET_DATA_KEY = "WidgetDataKey"
class FavoriteWidget : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
            for (appWidgetId in ids) {
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

        val views = RemoteViews(context.packageName, R.layout.favorite_widget)
        views.setRemoteAdapter(R.id.stack_view, intent)
        views.setEmptyView(R.id.stack_view, R.id.no_view)

        val toastIntent = Intent(context, FavoriteWidget::class.java)
        toastIntent.action = TOAST_ACTION
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        val toastPendingIntent =
                PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
    }
}

