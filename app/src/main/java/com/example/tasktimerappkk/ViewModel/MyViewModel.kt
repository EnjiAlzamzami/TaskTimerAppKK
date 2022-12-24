package com.example.tasktimerappkk.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tasktimerappkk.Model.Repository
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class MyViewModel(application: Application): AndroidViewModel(application)   {
    private val repository: Repository
    private lateinit var tasks: LiveData<List<Task>>
 //   val db = Firebase.firestore


    init {
        repository = Repository(MainActivity.user!!.id)
            tasks = repository.getData()

    }

    fun getTasks(): LiveData<List<Task>>{
        return tasks
    }

    fun addTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.addTask(task)
        }
    }

    fun editTask(task: Task, newTask:String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateTask(task,newTask)
        }
    }

    fun deleteTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteTask(task)
        }
    }
}

