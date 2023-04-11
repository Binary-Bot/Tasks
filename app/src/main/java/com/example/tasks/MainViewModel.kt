package com.example.tasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.ui.Shared
import com.example.tasks.widget.AppWidget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainViewModel: ViewModel() {

    private lateinit var context:Context
    private val _tasks = MutableLiveData<MutableList<String>>()
    val tasks: MutableLiveData<MutableList<String>> = _tasks
    private val _taskTimes = MutableLiveData<HashMap<String, Int>>()
    val taskTimes: MutableLiveData<HashMap<String, Int>> = _taskTimes

    private lateinit var sharedPreferences:SharedPreferences

    private fun loadTasks(): MutableList<String> {
        return mutableListOf<String>(
            "Work Out",
            "Assignments",
            "Cooking",
            "Netflix",
            "Gaming",
        )
    }

    fun addTask(task: String) {
        _tasks.value?.add(task)
        _taskTimes.value!![task] = 0
        storeData(_tasks.value!!)
        storeTimeData(Shared.TASK_TIMES, _taskTimes.value!!)
    }

    fun removeTask(task: String) {
        _tasks.value?.remove(task)
        _taskTimes.value!!.remove(task)
        storeData(_tasks.value!!)
        storeTimeData(Shared.TASK_TIMES, _taskTimes.value!!)
    }

    fun addTaskTime(task: String, time:Int) {
        _taskTimes.value!![task] = _taskTimes.value!![task]!!.plus(time)
        storeTimeData(Shared.TASK_TIMES, _taskTimes.value!!)
    }

    fun setContext(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(Shared.TASKS_APP, Context.MODE_PRIVATE)
        if (checkIfDataStored(Shared.TASKS)) {
            _tasks.value = retrieveData(Shared.TASKS)
        } else {
            _tasks.value = loadTasks()
            storeData(_tasks.value!!)
        }
        if (checkIfDataStored(Shared.TASK_TIMES)) {
            _taskTimes.value = retrieveTimeData(Shared.TASK_TIMES)
        } else {
            _taskTimes.value = hashMapOf()
            for (tasks: String in _tasks.value!!) {
                _taskTimes.value!![tasks] = 0
            }
            storeTimeData(Shared.TASK_TIMES, _taskTimes.value!!)
        }
    }

    private fun storeData(value:List<String>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(value)
        editor.putString(Shared.TASKS, json)
        editor.apply()
        updateWidget()
    }

    private fun storeTimeData(key:String, value:HashMap<String, Int>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(value)
        editor.putString(key, json)
        editor.apply()
        updateWidget()
    }


    private fun retrieveData(key: String): MutableList<String> {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun retrieveTimeData(key: String): HashMap<String, Int> {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<HashMap<String, Int>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun checkIfDataStored(key: String): Boolean {
        val data = sharedPreferences.getString(key, null)
        return data != null
    }

    private fun updateWidget() {
        val intent = Intent(context, AppWidget::class.java)
        intent.action = AppWidget.ACTION_UPDATE_WIDGET
        context.sendBroadcast(intent)
    }
}