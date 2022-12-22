package com.example.tasktimerappkk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityTasksBinding
import com.example.tasktimerappkk.databinding.TasksRowBinding
import com.example.tasktimerappkk.service.TimerService
import kotlin.math.roundToInt

class TasksActivity : AppCompatActivity(), TasksAdapter.ClickListener {
    private lateinit var TasksAdapter: TasksAdapter
    lateinit var binding:ActivityTasksBinding
    lateinit var viewModel: MyViewModel

    private var time=0.0
    var timerStarted = false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel= ViewModelProvider(this).get(MyViewModel::class.java)
        //Read data from database
        viewModel.getTasks().observe(this,{
                userTasks -> TasksAdapter.UpdateList(userTasks)
        })

        TasksAdapter= TasksAdapter(this)
        binding.tasksRv.adapter=TasksAdapter

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

    }

    override fun startStopTimer(task:Task) {
        startStopTimer()
    }

    override fun resetTimer(task: Task) {
        resetTimer()

    }

    override fun deleteTimer(task: Task) {
       viewModel.deleteTask(task)
    }

    private fun resetTimer() {
        stopTimer()
        time=0.0
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
            binding.timerTv.text=getTimeStringFromDouble(time)
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