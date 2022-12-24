package com.example.tasktimerappkk.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LocalTaskRepositry  (var taskDao:TaskDAO){
    suspend fun addTask(task: TaskL){

        taskDao.addTasks(task)
    }
    suspend fun updateTask(task:TaskL){
        taskDao.updateTasks(task)
    }
    suspend fun deleteTask(task:TaskL){
        taskDao.deleteTasks(task)
    }
    fun getTasks(user:String): LiveData<List<TaskL>> {

        var currentLiveTask:MutableLiveData<List<TaskL>> = MutableLiveData()
        var allTasks=taskDao.getTasks().value
        var currentTask:ArrayList<TaskL> = arrayListOf()//To store tasks belong to the current users
        if (allTasks != null) {
            for (task in allTasks){
                if (task.user==user){
                    currentTask.add(task)
                }
            }
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++
        currentLiveTask.postValue(currentTask)
        return currentLiveTask
    }


}


//fun getTasks(user:String): LiveData<List<TaskL>> {
//
//    var currentLiveTask:MutableLiveData<List<TaskL>> = MutableLiveData()
//    var allTasks=taskDao.getTasks().value
//    var currentTask:ArrayList<TaskL> = arrayListOf()//To store tasks belong to the current users
//    if (allTasks != null) {
//        for (task in allTasks){
//            if (task.user==user){
//                currentTask.add(task)
//            }
//        }
//    }
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    currentLiveTask.postValue(currentTask)
//    return currentLiveTask
//}