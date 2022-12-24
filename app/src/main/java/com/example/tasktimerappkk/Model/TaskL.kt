package com.example.tasktimerappkk.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="tasks")
class TaskL (
    val user:String,
    @PrimaryKey(autoGenerate = true)
    val pk:Int,
    var title: String,var details:String,var timer:Double)