package com.example.addtask.ViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.addtask.Model.Repository
import com.example.addtask.Model.Task
import com.example.tasktimerappkk.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class MyViewModel(application: Application): AndroidViewModel(application)   {
    private val repository: Repository
    private lateinit var tasks: LiveData<List<Task>>
    val db = Firebase.firestore




    init {

        repository = Repository(MainActivity.userData.user!!.id)
        CoroutineScope(Dispatchers.IO).launch {
            tasks = repository.getData()

        }
    }

    fun getTasks(): LiveData<List<Task>>{

           return repository.getData()

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

