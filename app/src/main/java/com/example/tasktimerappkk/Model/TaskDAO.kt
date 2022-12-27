package com.example.tasktimerappkk.Model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tasktimerappkk.MainActivity

@Dao
interface TaskDAO {

    @Query("select * from tasks order by pk asc")
    fun getTasks(): LiveData<List<TaskL>>
//    @Query("select * from tasks WHERE user!='guest' order by pk asc")
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTasks(task:TaskL)
    @Update
    suspend fun updateTasks(task:TaskL)
    @Delete
    suspend fun deleteTasks(task:TaskL)
}