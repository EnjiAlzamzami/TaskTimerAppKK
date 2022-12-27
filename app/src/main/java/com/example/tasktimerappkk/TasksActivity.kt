package com.example.tasktimerappkk

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityTasksBinding
import kotlin.math.roundToInt


class TasksActivity : AppCompatActivity(), TasksAdapter.ClickListener {
    private lateinit var TasksAdapter: TasksAdapter
    private lateinit var binding:ActivityTasksBinding
    private lateinit var viewModel: MyViewModel
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

        //If the user login then we will get data from firebase, else the data will retrieve from room

        //**************************************************************
        // viewModel = ViewModelProvider(this@TasksActivity).get(MyViewModel::class.java)
        TasksAdapter = TasksAdapter(this@TasksActivity)
        binding.tasksRv.adapter = TasksAdapter
        if (MainActivity.user!=null) {
            //Read data from database
            viewModel.getTasks().observe(this@TasksActivity) { userTasks ->
                TasksAdapter.UpdateList(userTasks)
                updateTotal(userTasks)
            }

        }
        else{
            binding.profileCard.visibility= View.GONE
            viewModel.getLocalTasks().observe(this@TasksActivity) { localTasks ->
                TasksAdapter.UpdateLocalList(localTasks)
                updateLocalTotal(localTasks)

                binding.logoKK.setOnClickListener {
                    val signInIntent = Intent(context, MainActivity::class.java)
                    context.startActivity(signInIntent)
                    finish()
                }
            }
        }
        //****************************************************************
        binding.addingBtn.setOnClickListener {
            val AddTaskIntent = Intent(context, AddTaskActivity::class.java)
            context.startActivity(AddTaskIntent)
            finish()
        }
        binding.profileBtn.setOnClickListener {
            val profileIntent = Intent(context, ProfileActivity::class.java)
            context.startActivity(profileIntent)
            finish()
        }



    }


        override fun deleteTask(task: Task) {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage(getString(R.string.deleteConf))


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
    alertDialog.setMessage(getString(R.string.deleteConf))

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