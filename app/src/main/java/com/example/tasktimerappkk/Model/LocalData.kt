package com.example.tasktimerappkk.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskL::class], version = 1, exportSchema = false)
abstract class LocalData : RoomDatabase(){
    abstract fun taskDao():TaskDAO
    companion object{
        @Volatile
        private var INSTANCE:LocalData?=null

        fun getDatabase(context: Context):LocalData{
            val tempInstance= INSTANCE
            if (tempInstance !=null){
                return tempInstance
            }
            synchronized(this){
                val  instance= Room.databaseBuilder(
                    context.applicationContext,
                    LocalData::class.java,
                    "Tasks"
                ).fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance
            }//End synchronized
        }

    }
}