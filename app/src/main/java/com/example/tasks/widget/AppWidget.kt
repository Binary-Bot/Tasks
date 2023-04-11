package com.example.tasks.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.example.tasks.MainActivity
import com.example.tasks.MainViewModel
import com.example.tasks.R

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {
    private lateinit var viewModel: MainViewModel

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        Log.d("Shashwat", "$appWidgetIds")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_TOAST.equals(intent.action)){
            val clickedPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0)
            Log.d("Shashwat", "Clicked Position: $clickedPosition")
        }
        if (ACTION_UPDATE_WIDGET.equals(intent.action)){
            Log.d("Shashwat", "Received Update Widget")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            // Get the widget ids for all the widgets of this type
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, AppWidget::class.java))

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }

        }
        super.onReceive(context, intent)
    }

    companion object {
        const val ACTION_TOAST = "action"
        const val EXTRA_ITEM_POSITION = "itemPosition"
        const val ACTION_UPDATE_WIDGET = "UPDATE_WIDGET"

    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(context, 0, intent, 0)
    }

    val serviceIntent = Intent(context, WidgetService::class.java)
    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)))

    val clickIntent = Intent(context, AppWidget::class.java)
    clickIntent.setAction(AppWidget.ACTION_TOAST)
    val clickPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getBroadcast(context, 0, clickIntent, 0)
    }

    val widgetText = "Open Tasks"
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.app_widget)
    views.setTextViewText(R.id.widget_button, widgetText)
    views.setOnClickPendingIntent(R.id.widget_button, pendingIntent)
    views.setRemoteAdapter(R.id.widget_stack_view, serviceIntent)
    views.setEmptyView(R.id.widget_stack_view, R.id.widget_empty_view)
    views.setPendingIntentTemplate(R.id.widget_stack_view, clickPendingIntent)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stack_view)
}