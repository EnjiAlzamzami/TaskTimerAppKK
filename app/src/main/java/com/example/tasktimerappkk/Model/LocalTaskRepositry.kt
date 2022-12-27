package com.example.tasktimerappkk.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.checkerframework.checker.units.qual.s

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
    fun getTasks(userName:String): LiveData<List<TaskL>> {

        return Transformations.map(taskDao.getTasks()) { taskList ->
            taskList.filter {task ->
                task.user.contains(userName)
            }
        }
    }


}


