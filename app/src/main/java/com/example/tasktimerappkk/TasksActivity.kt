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


    class TasksActivity : AppCompatActivity(), TasksAdapter.ClickListener {//End of the class
        private lateinit var TasksAdapter: TasksAdapter
        lateinit var binding:ActivityTasksBinding
        lateinit var viewModel: MyViewModel
        lateinit var context:Context

        private var time=0.0

        var timerStarted = false
        private lateinit var serviceIntent: Intent


        override fun onCreate(savedInstanceState: Bundle?) {
            viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
            super.onCreate(savedInstanceState)
            binding = ActivityTasksBinding.inflate(layoutInflater)
            setContentView(binding.root)
            context=this

            binding.apply {


                viewModel = ViewModelProvider(this@TasksActivity).get(MyViewModel::class.java)
                //Read data from database
                viewModel.getTasks().observe(this@TasksActivity, { userTasks ->
                    TasksAdapter.UpdateList(userTasks)
                })

                TasksAdapter = TasksAdapter(this@TasksActivity)
                binding.tasksRv.adapter = TasksAdapter
                addingBtn.setOnClickListener {
                    var intent = Intent(context, AddTaskActivity::class.java)
                    context.startActivity(intent)
                }

                serviceIntent = Intent(applicationContext, TimerService::class.java)
                registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

            }
        }

        override fun taskUI(task: Task) {
            TODO("Not yet implemented")
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

        override fun updateTotal() {
            binding.totalTimeTv.text=getTimeStringFromDouble(totalTime)
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
                binding.timerBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
                startService(serviceIntent)
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
        public companion object{
            var totalTime=0.0f.toDouble()
        }

        }