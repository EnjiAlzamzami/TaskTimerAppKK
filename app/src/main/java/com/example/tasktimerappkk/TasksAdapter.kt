package com.example.tasktimerappkk

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
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
        //The binding will have tow direction, first if the user log in,
        //the selected item will be the one from tasks list
        //If the user is guest user and the data loaded from room,
        // then selected will item taken from localTasks
        var selectedItem: Any

            var time = 0.0

            holder.binding.apply {


                if (MainActivity.userData.user != null) {
                    selectedItem = tasks[position]
                    taskTitleTv.text = (selectedItem as Task).title
                    descriptionTv.text = (selectedItem as Task).details
                    time = (selectedItem as Task).timer
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    timerTv.setText(getTimeStringFromDouble(time))
                    var timerStarted = true
                    var working = true

                    time= (selectedItem as Task).timer


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
                            clickListener.updateTimer(selectedItem as Task,time)
                        }

                    }//End timerBtn.setOnClickListener

                    deleteBtn.setOnClickListener {
                        clickListener.deleteTask(selectedItem as Task)
                    }
                    resetBtn.setOnClickListener {
                        time=0.0
                        working=false
                        timerStarted=true
                        //  timerTv.setText(getTimeStringFromDouble(time))
                        timerBtn.setImageResource(R.drawable.started_24)
                        clickListener.updateTimer(selectedItem as Task,time)
                    }

                    }//end if user loged in

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


else{

    selectedItem=localTasks[position]
    taskTitleTv.text = (selectedItem as TaskL).title
    descriptionTv.text = (selectedItem as TaskL).details

    time = (selectedItem as TaskL).timer
    timerTv.setText(getTimeStringFromDouble(time))

    deleteBtn.setOnClickListener {

        clickListener.deleteLocalTask(selectedItem as TaskL)

    }
    //----------------------------------------------------------
                    var timerStarted = true
                    var working=true
                    time= (selectedItem as TaskL).timer

                    timerBtn.setOnClickListener {
                        if (timerStarted) {
                            timerBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                            timerStarted=false
                            working=true
                            object : CountDownTimer(800000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    if(working){
                                        time++
                                        TasksActivity.totalTime+=1

                                        //.totalTimeTv.text=getTimeStringFromDouble(TasksActivity.totalTime)
                                        clickListener.updateLocalTotal(localTasks)
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
                            clickListener.updateLocalTimer(selectedItem as TaskL,time)
                        }

                    }//End timerBtn.setOnClickListener


                    resetBtn.setOnClickListener {
                        time=0.0
                        working=false
                        timerStarted=true
                        //  timerTv.setText(getTimeStringFromDouble(time))
                        timerBtn.setImageResource(R.drawable.started_24)
                        clickListener.updateLocalTimer(selectedItem as TaskL,time)
                    }

                }//End guest user part







            }//End binding.apply

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }//End onBindViewHolder

    override fun getItemCount(): Int {
        if (MainActivity.userData.user != null){
          return  tasks.size}
        return localTasks.size
    }
    //this for online database
    fun UpdateList(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
    //this for off line data
    fun UpdateLocalList(tasksL: List<TaskL>) {
        this.localTasks = tasksL
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun deleteTask(task: Task)
        fun updateTotal(tasks:List<Task>)
        fun updateTimer(task: Task,time:Double)

        fun updateLocalTotal(tasks:List<TaskL>)
        fun deleteLocalTask(task: TaskL)
        fun updateLocalTimer(task: TaskL, time:Double)
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
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

