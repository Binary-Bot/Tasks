package com.example.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _tasks = MutableLiveData<MutableList<String>>()
    private val _taskTimes = hashMapOf<String, Double>()
    val tasks: MutableLiveData<MutableList<String>> = _tasks

    init {
        _tasks.value = loadTasks()
        for (tasks:String in _tasks.value!!) {
            _taskTimes[tasks] = 0.00
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
        _taskTimes[task] = 0.00
    }

    fun removeTask(task: String) {
        _tasks.value?.remove(task)
        _taskTimes.remove(task)
    }
}