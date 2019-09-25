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
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo



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

        try {
            val intent = Intent()
            val manufacturer = android.os.Build.MANUFACTURER
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
            } else if ("oneplus".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity")
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
            } else if ("huawei".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
            } else if ("asus".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity")
            } else {
                Log.e("other phone ", "===>")
            }
            val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    val viewModel: ListModel by lazy {
        ViewModelProviders.of(this).get(ListModel::class.java)
    }
}