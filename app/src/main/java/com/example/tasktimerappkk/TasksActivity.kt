package com.example.tasktimerappkk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityTasksBinding
import com.example.tasktimerappkk.databinding.TasksRowBinding
import com.example.tasktimerappkk.service.TimerService
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class TasksActivity : AppCompatActivity(), TasksAdapter.ClickListener {
    private lateinit var TasksAdapter: TasksAdapter
    lateinit var binding:ActivityTasksBinding
    lateinit var viewModel: MyViewModel

    private var time=0.0
    lateinit var currentTask:Task
    var timerStarted =false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


        viewModel= ViewModelProvider(this).get(MyViewModel::class.java)
        //Read data from database
        viewModel.getTasks().observe(this,{
                userTasks -> TasksAdapter.UpdateList(userTasks)
        })

        TasksAdapter= TasksAdapter(this)
        binding.tasksRv.adapter=TasksAdapter


        binding.addingBtn.setOnClickListener{
            val AddTaskIntent = Intent(applicationContext,AddTaskActivity :: class.java)
            startActivity(AddTaskIntent)
            finish()
        }


    }

    override fun taskUI(task: Task) {
        currentTask=task
        Log.d("TasksActivity", "taskUI: $currentTask")
        var binding = TasksRowBinding.inflate(layoutInflater)
        binding.apply {
            timerTv.text=getTimeStringFromDouble(task.timer)

        }

    }

    override fun startStopTimer(task:Task) {
        currentTask=task
        startStopTimer()
    }

    override fun resetTimer(task: Task) {
        var newTimer=resetTimer()
        viewModel.editTask(task,getTimeStringFromDouble(newTimer))
    }

    override fun deleteTimer(task: Task) {
       viewModel.deleteTask(task)
        Toast.makeText(this,"Deleted Task!",Toast.LENGTH_SHORT).show()
    }

    private fun resetTimer():Double {
        stopTimer()
        time=0.0
        return time
    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        var binding= TasksRowBinding.inflate(layoutInflater)
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        startService(serviceIntent)
        binding.timerBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        timerStarted=true

    }

    private fun stopTimer() {
        var binding= TasksRowBinding.inflate(layoutInflater)
        stopService(serviceIntent)
        binding.timerBtn.setImageResource(R.drawable.started_icon)
        timerStarted=false
    }


    private val  updateTime : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var binding= TasksRowBinding.inflate(layoutInflater)
            time=intent!!.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            var timeTxt=getTimeStringFromDouble(time)
            binding.timerTv.text=timeTxt
            viewModel.editTask(currentTask,timeTxt)

        }

    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}