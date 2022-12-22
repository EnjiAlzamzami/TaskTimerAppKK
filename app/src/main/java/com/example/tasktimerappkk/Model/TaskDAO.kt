package com.example.tasktimerappkk.Model

import androidx.room.*
import com.example.addtask.Model.Task

interface TaskDAO  {
    @Query("select * from tasks order by pk asc")
    suspend fun getNotes():List<Task>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(noteT:Task)
    @Update
    suspend fun updateNote(noteT:Task)
    @Delete
    suspend fun deleteNote(noteT:Task)
}