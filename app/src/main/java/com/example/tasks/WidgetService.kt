package com.example.tasks

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.ViewModelProvider
import com.example.task.MainViewModel

class WidgetService: RemoteViewsService() {
    private lateinit var mainViewModel: MainViewModel

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)
        return ExampleWidgetItemFactory(applicationContext, intent, mainViewModel)
    }

    class ExampleWidgetItemFactory(context:Context, intent: Intent, viewModel: MainViewModel): RemoteViewsFactory {
        private val context: Context
        private val appWidgetId: Int
        private val viewModel: MainViewModel


        init{
            this.context = context
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            this.viewModel = viewModel
        }

        override fun onCreate() {
        }

        override fun onDataSetChanged() {
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return viewModel.tasks.value!!.size
        }

        override fun getViewAt(index: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.widget_item)
            views.setTextViewText(R.id.widget_item_text, viewModel.tasks.value!![index])
            return views
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}