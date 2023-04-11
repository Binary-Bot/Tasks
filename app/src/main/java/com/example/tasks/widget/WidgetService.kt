package com.example.tasks.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.tasks.MainViewModel
import com.example.tasks.R
import com.example.tasks.ui.Shared
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WidgetService: RemoteViewsService(), ViewModelStoreOwner {
    private lateinit var mainViewModel: MainViewModel
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        mainViewModel = MainViewModel()
        mainViewModel.setContext(applicationContext)
        return ExampleWidgetItemFactory(applicationContext, intent, mainViewModel)
    }

    class ExampleWidgetItemFactory(context:Context, intent: Intent, viewModel: MainViewModel): RemoteViewsFactory {
        private val context: Context
        private val appWidgetId: Int
        private var viewModel: MainViewModel
        private val sharedPreferences:SharedPreferences
        private var selectedItemPosition: Int = -1



        init{
            this.context = context
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            this.viewModel = viewModel
            this.sharedPreferences = context.getSharedPreferences(Shared.TASKS_APP, Context.MODE_PRIVATE)
        }

        override fun onCreate() {
        }

        override fun onDataSetChanged() {
            Log.d("Shashwat", "onDataSetChanged")
            val json = sharedPreferences.getString(Shared.TASKS, null)
            val type = object : TypeToken<MutableList<String>>() {}.type
            viewModel.tasks.postValue(Gson().fromJson(json, type))
            Log.d("Shashwat", "newtask: ${            viewModel.tasks.postValue(Gson().fromJson(json, type))
            }")
            val json2 = sharedPreferences.getString(Shared.TASK_TIMES, null)
            val type2 = object : TypeToken<HashMap<String, Int>>() {}.type
            viewModel.taskTimes.postValue(Gson().fromJson(json2, type2))
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return viewModel.tasks.value!!.size
        }

        override fun getViewAt(index: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.widget_item)
            views.setTextViewText(R.id.widget_item_text, viewModel.tasks.value!![index])
            // Set the background color of the view
//            if (selectedItemPosition == index) {
//                views.setInt(R.id.widget_item_text, "setBackgroundColor", Color.GREEN)
//            } else {
//                views.setInt(R.id.widget_item_text, "setBackgroundColor", Color.TRANSPARENT)
//            }
            val fillIntent = Intent()
            fillIntent.putExtra(AppWidget.EXTRA_ITEM_POSITION, index)
            views.setOnClickFillInIntent(R.id.widget_item_text, fillIntent)
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

    override val viewModelStore: ViewModelStore
        get() = TODO("Not yet implemented")
}