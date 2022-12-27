package com.example.tasktimerappkk.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tasktimerappkk.MainActivity
import com.example.tasktimerappkk.Model.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class MyViewModel(application: Application): AndroidViewModel(application)   {
    private lateinit var repository: Repository
    private lateinit var  localRepositry: LocalTaskRepositry
    private lateinit var tasks: LiveData<List<Task>>
    private lateinit var localTasks: LiveData<List<TaskL>>
 //   val db = Firebase.firestore

    init {

        val taskDao = LocalData.getDatabase(application).taskDao()
        localRepositry = LocalTaskRepositry(taskDao)
        //======================================================
        if (MainActivity.user!=null) {
            repository = Repository(MainActivity.userData.user!!.id)
               tasks = repository.getData()
               localTasks = localRepositry.getTasks((MainActivity.user!!.username))

        }
        //Else if this is a guest user:
        else {


            localTasks = localRepositry.getTasks("guest")
        }

    }

    fun getTasks(): LiveData<List<Task>>{
        return tasks
    }
    fun getLocalTasks():LiveData<List<TaskL>>{
        var userName:String
        if (MainActivity.user!=null){userName= MainActivity.user!!.username}
        else{userName="guest"}
        return localRepositry.getTasks(userName)
    }
    //=================================================================================

    fun addTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            repository.addTask(task)
        }
    }
    fun addLocalTask(taskL: TaskL,user:String){
        CoroutineScope(Dispatchers.IO).launch {
            localRepositry.addTask(taskL)}
    }
//=================================================================================
fun editLocalTask(taskL: TaskL, newTask: String){
    CoroutineScope(Dispatchers.IO).launch {
        localRepositry.updateTask(taskL)
    }
}
//=================================================================================

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
    fun deleteLocalTask(task:TaskL){
        CoroutineScope(Dispatchers.IO).launch {
            localRepositry.deleteTask(task)
        }
    }
//=================================================================================
}

