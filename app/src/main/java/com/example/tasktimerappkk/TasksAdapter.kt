package com.example.tasktimerappkk

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
import com.example.tasktimerappkk.databinding.TasksRowBinding
import com.example.tasktimerappkk.service.TimerService
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
        var selectedItem=tasks[position]
        holder.binding.apply {
            taskTitleTv.text=selectedItem.title
            descriptionTv.text=selectedItem.details
            var timerStarted = true
            var working=true
            var time=0.0

            timerBtn.setOnClickListener {
                //clickListener.startStopTimer(selectedItem)
                if (timerStarted) {


                    timerBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                    timerStarted=false
                    working=true
                    object : CountDownTimer(30000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            timerTv.setText("0:" +  getTimeStringFromDouble(time))
                           if(working){
                               TasksActivity.totalTime+=1
                               clickListener.updateTotal()
                               time++

                               }
                        }


                        override fun onFinish() {

                        }
                    }.start()

                }
                else
                {
                    working=false
                    timerBtn.setImageResource(R.drawable.started_icon)
                    timerStarted=true
                }

            }//End timerBtn.setOnClickListener

            deleteBtn.setOnClickListener {
                clickListener.deleteTimer(selectedItem)
            }
            resetBtn.setOnClickListener {
                clickListener.resetTimer(selectedItem)
            }
        }
    }

    override fun getItemCount() = tasks.size

    fun UpdateList(tasks: List<Task>) {

        this.tasks = tasks

        notifyDataSetChanged()
    }

    interface ClickListener {
        fun taskUI(task: Task)
        fun startStopTimer(task: Task)
        fun resetTimer(task: Task)
        fun deleteTimer(task: Task)
        fun updateTotal()
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

