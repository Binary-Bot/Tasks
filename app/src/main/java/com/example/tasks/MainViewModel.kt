package com.example.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _tasks = MutableLiveData<MutableList<String>>()
    val tasks: MutableLiveData<MutableList<String>> = _tasks
    private val _taskTimes = MutableLiveData<HashMap<String, Int>>()
    val taskTimes: MutableLiveData<HashMap<String, Int>> = _taskTimes


    init {
        _tasks.value = loadTasks()
        for (tasks:String in _tasks.value!!) {
            _taskTimes.value?.set(tasks, 0)
        }

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
    }

    fun removeTask(task: String) {
        _tasks.value?.remove(task)
        _taskTimes.value!!.remove(task)
    }
}