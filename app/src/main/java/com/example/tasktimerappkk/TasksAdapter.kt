package com.example.tasktimerappkk

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.databinding.TasksRowBinding

class TasksAdapter(var clickListener: ClickListener):
    RecyclerView.Adapter<TasksAdapter.ItemViewHolder>() {
    var tasks= emptyList<Task>()
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

            timerBtn.setOnClickListener {
                clickListener.startStopTimer(selectedItem)

            }
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
    }

}

