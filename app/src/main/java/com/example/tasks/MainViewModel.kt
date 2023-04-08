package com.example.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _tasks = MutableLiveData<MutableList<String>>()
    val tasks: MutableLiveData<MutableList<String>> = _tasks

    init {
        _tasks.value = loadTasks()
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
    }

    fun removeTask(task: String) {
        _tasks.value?.remove(task)
    }
}