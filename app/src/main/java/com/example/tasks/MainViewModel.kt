package com.example.task

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainViewModel: ViewModel() {

    private val _tasks = MutableLiveData<MutableList<String>>()
    val tasks: MutableLiveData<MutableList<String>> = _tasks
    private val _taskTimes = MutableLiveData<HashMap<String, Int>>()
    val taskTimes: MutableLiveData<HashMap<String, Int>> = _taskTimes

    private lateinit var sharedPreferences:SharedPreferences

    init {

    }
    private fun loadTasks(): MutableList<String> {
        return mutableListOf<String>(
            "Work Out",
            "Assignments",
            "Meditation",
            "Cooking",
            "Netflix",
            "Gaming",
        )
    }

    fun addTask(task: String) {
        _tasks.value?.add(task)
        _taskTimes.value!![task] = 0
        storeData(TASKS, _tasks.value!!)
        storeTimeData(TASK_TIMES, _taskTimes.value!!)
    }

    fun removeTask(task: String) {
        _tasks.value?.remove(task)
        _taskTimes.value!!.remove(task)
        storeData(TASKS, _tasks.value!!)
        storeTimeData(TASK_TIMES, _taskTimes.value!!)
    }

    fun addTaskTime(task: String, time:Int) {
        _taskTimes.value!![task] = _taskTimes.value!![task]!!.plus(time)
        storeTimeData(TASK_TIMES, _taskTimes.value!!)
        Log.d("Shashwat", "${_taskTimes.value!![task]}")
    }

    fun setContext(context: Context) {
        sharedPreferences = context.getSharedPreferences(TASKS, Context.MODE_PRIVATE)
        if (checkIfDataStored(TASKS)) {
            _tasks.value = retrieveData(TASKS)
        } else {
            _tasks.value = loadTasks()
            storeData(TASKS, _tasks.value!!)
        }
        if (checkIfDataStored(TASK_TIMES)) {
            _taskTimes.value = retrieveTimeData(TASK_TIMES)
        } else {
            _taskTimes.value = hashMapOf()
            for (tasks: String in _tasks.value!!) {
                _taskTimes.value!![tasks] = 0
            }
            storeTimeData(TASKS, _taskTimes.value!!)
        }
    }

    private fun storeData(key:String, value:List<String>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(value)
        Log.d("Shashwat", "storing in json: $json")
        editor.putString(key, json)
        editor.apply()
    }

    private fun storeTimeData(key:String, value:HashMap<String, Int>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(value)
        Log.d("Shashwat", "storing in json: $json")
        editor.putString(key, json)
        editor.apply()
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

    fun checkIfDataStored(key: String): Boolean {
        val data = sharedPreferences.getString(key, null)
        return data != null
    }

    companion object{
        const val TASKS = "tasks"
        const val TASK_TIMES = "taskTimes"
    }
}