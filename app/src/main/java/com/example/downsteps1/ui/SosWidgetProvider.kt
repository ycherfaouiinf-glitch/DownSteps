package com.example.downsteps1.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.example.downsteps1.R

class SosWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_REFRESH_WIDGETS) {
            refreshAll(context)
        }
    }

    companion object {
        private const val ACTION_REFRESH_WIDGETS =
            "com.example.downsteps1.REFRESH_SOS_WIDGETS"

        fun refreshAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, SosWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(component)

            ids.forEach { appWidgetId ->
                updateWidget(context, manager, appWidgetId)
            }
        }

        fun requestRefresh(context: Context) {
            val intent = Intent(context, SosWidgetProvider::class.java).apply {
                action = ACTION_REFRESH_WIDGETS
            }

            context.sendBroadcast(intent)
        }

        private fun updateWidget(
            context: Context,
            manager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val prefs = context.getSharedPreferences(
                "sos_prefs",
                Context.MODE_PRIVATE
            )

            val primaryNumber = prefs
                .getString("primary_number", "")
                ?.trim()
                .orEmpty()

            val contactName = prefs
                .getString("contact_name", "")
                ?.trim()
                .orEmpty()

            val title = "SOS"

            val subtitle = if (primaryNumber.isNotEmpty()) {
                if (contactName.isNotEmpty()) {
                    "Call $contactName quickly"
                } else {
                    "Tap to call emergency"
                }
            } else {
                "Set emergency number first"
            }

            val openIntent = if (primaryNumber.isNotEmpty()) {
                Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$primaryNumber")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            } else {
                Intent(context, SosActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(
                context.packageName,
                R.layout.widget_sos
            )

            views.setTextViewText(R.id.widgetSosTitle, title)

            views.setOnClickPendingIntent(
                R.id.widgetSosRoot,
                pendingIntent
            )

            views.setOnClickPendingIntent(
                R.id.widgetSosButton,
                pendingIntent
            )

            manager.updateAppWidget(appWidgetId, views)
        }
    }
}