package com.example.tasktimerappkk

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
import com.example.tasktimerappkk.databinding.ActivityTasksBinding
import com.example.tasktimerappkk.databinding.TasksRowBinding
import kotlin.math.roundToInt


class TasksAdapter(var clickListener: ClickListener):
    RecyclerView.Adapter<TasksAdapter.ItemViewHolder>() {
    var tasks= emptyList<Task>()
    var localTasks= emptyList<TaskL>()
    class ItemViewHolder(var binding: TasksRowBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //return view holder
        return ItemViewHolder(
            TasksRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val selectedItem=tasks[position]
        var time=0.0
        holder.binding.apply {
            taskTitleTv.text=selectedItem.title
            descriptionTv.text=selectedItem.details
            time=selectedItem.timer
            timerTv.setText(getTimeStringFromDouble(time))
            var timerStarted = true
            var working=true

            timerBtn.setOnClickListener {
                if (timerStarted) {
                    timerBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                    timerStarted=false
                    working=true
                    object : CountDownTimer(30000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                           if(working){
                               time++
                               TasksActivity.totalTime+=1

                               //.totalTimeTv.text=getTimeStringFromDouble(TasksActivity.totalTime)
                               clickListener.updateTotal(tasks)
                               timerTv.setText(getTimeStringFromDouble(time))
                           }
                        }
                        override fun onFinish() {

                        }
                    }.start()

                }
                else
                {
                    timerTv.setText(getTimeStringFromDouble(time))
                    Log.d("Timer", "onBindViewHolder: $time")
                    working=false
                    timerBtn.setImageResource(R.drawable.started_24)
                    timerStarted=true
                    clickListener.updateTimer(selectedItem,time)
                }

            }//End timerBtn.setOnClickListener

            deleteBtn.setOnClickListener {
                clickListener.deleteTimer(selectedItem)
            }
            resetBtn.setOnClickListener {
               time=0.0
               working=false
               timerStarted=true
              //  timerTv.setText(getTimeStringFromDouble(time))
                timerBtn.setImageResource(R.drawable.started_24)
                clickListener.updateTimer(selectedItem,time)
            }
        }
    }

    override fun getItemCount() = tasks.size

    fun UpdateList(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun deleteTimer(task: Task)
        fun updateTotal(tasks:List<Task>)
        fun updateTimer(task: Task,time:Double)
    }

    fun checkDigit(number: Double): String? {
        return  number.toString()
    }
    //****************************************************************
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    //****************************************************************
}

