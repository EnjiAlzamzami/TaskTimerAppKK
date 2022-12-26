package com.example.tasktimerappkk

import android.content.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityTasksBinding
import kotlin.math.roundToInt


    class TasksActivity : AppCompatActivity(), TasksAdapter.ClickListener {//End of the class
        private lateinit var TasksAdapter: TasksAdapter
        lateinit var binding:ActivityTasksBinding
        lateinit var viewModel: MyViewModel
        lateinit var context:Context

        companion object{
            var totalTime=0.0
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityTasksBinding.inflate(layoutInflater)
            setContentView(binding.root)
            viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
            context=this

            binding.apply {
                //If the user login then we will get data from firebase, else the data will retrive from room

                //**************************************************************
               // viewModel = ViewModelProvider(this@TasksActivity).get(MyViewModel::class.java)
                TasksAdapter = TasksAdapter(this@TasksActivity)
                binding.tasksRv.adapter = TasksAdapter
                if (MainActivity.userData.user!=null) {
                    //Read data from database
                    viewModel.getTasks().observe(this@TasksActivity) { userTasks ->
                        TasksAdapter.UpdateList(userTasks)
                        updateTotal(userTasks)
                    }


                }
                else{

                    viewModel.getLocalTasks().observe(this@TasksActivity) { localTasks ->
                        TasksAdapter.UpdateLocalList(localTasks)
                        updateLocalTotal(localTasks)

                    }
                }
                //****************************************************************
                addingBtn.setOnClickListener {
                    var intent = Intent(context, AddTaskActivity::class.java)
                    context.startActivity(intent)
                }

            }
        }


        override fun deleteTask(task: Task) {

            val alertDialog = AlertDialog.Builder(this)
            val title = TextView(this)
            title.setTextColor(Color.BLACK)
            title.setText(getString(R.string.deleteConf))
            alertDialog.setView(title)

            alertDialog.setPositiveButton(getString(R.string.delete)) { dialog, i ->
                viewModel.deleteTask(task)
                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_LONG).show()

            }//end delete option
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            alertDialog.show()
        }

        override fun updateTotal(tasks: List<Task>) {
            totalTime=0.0
                for(i in tasks){
                   totalTime+=i.timer
                }
            binding.totalTimeTv.text=getTimeStringFromDouble(totalTime)
        }
        //------------------------------------------------------------
        override fun updateLocalTotal(tasks: List<TaskL>) {
            totalTime=0.0
            for(i in tasks){
                totalTime+=i.timer
            }
            binding.totalTimeTv.text=getTimeStringFromDouble(totalTime)
        }
        override fun deleteLocalTask(task: TaskL) {

            val alertDialog = AlertDialog.Builder(this)
            val title = TextView(this)
            title.setTextColor(Color.BLACK)
            title.setText(getString(R.string.deleteConf))
            alertDialog.setView(title)

            alertDialog.setPositiveButton(getString(R.string.delete)) { dialog, i ->
                viewModel.deleteLocalTask(task)
                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_LONG).show()

            }//end delete option
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            alertDialog.show()
        }
        override fun updateLocalTimer(task: TaskL, time:Double){
            task.timer=time

            viewModel.editLocalTask(task,time.toString())

        }
        //------------------------------------------------------------

        override fun updateTimer(task: Task, time: Double) {
            viewModel.editTask(task,time.toString())
        }


            private fun getTimeStringFromDouble(time: Double): String {
                val resultInt = time.roundToInt()
                val hours = resultInt % 86400 / 3600
                val minutes = resultInt % 86400 % 3600 / 60
                val seconds = resultInt % 86400 % 3600 % 60

                return String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }


        }