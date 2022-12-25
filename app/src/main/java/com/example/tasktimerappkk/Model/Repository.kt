package com.example.tasktimerappkk.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository (val user:String){


    private val currentTasks: MutableLiveData<List<Task>> = MutableLiveData()



    val db = Firebase.firestore

    fun addTask(task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            val newNote = hashMapOf(
                "id" to task.id,
                "title" to task.title,
                "details" to task.details,
                "timer" to task.timer,
            )
            db.collection("$user").add(newNote)
            getData()
        }
    }
    suspend fun updateTask(task: Task,newText:String){

        db.collection("$user").document(task.id).update("timer", newText)
        getData()

    }
    suspend fun getTask(task: Task){
        db.collection("$user").document(task.id)
            .get()



    }
    suspend fun deleteTask(task: Task){

        db.collection("$user").document(task.id).delete()
        getData()
    }


    fun getData(): LiveData<List<Task>> {
        var tasks= arrayListOf<Task>()
        tasks.clear()
        db.collection("$user")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {


                    var title=document.get("title").toString()
                    var details=document.get("details").toString()
                    var timer=document.get("timer").toString().toDouble()


                    tasks.add(Task(document.id,title,details,timer))
                }
                currentTasks.postValue(tasks)
                Log.d("MainActivity", "getData: $currentTasks/ [$tasks] ")

            }



            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
        return currentTasks
    }

}