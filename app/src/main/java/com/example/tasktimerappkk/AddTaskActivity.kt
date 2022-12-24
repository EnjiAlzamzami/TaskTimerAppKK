package com.example.tasktimerappkk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this).get(MyViewModel::class.java)
        binding.apply {
            addTaskBtn.setOnClickListener {
                var title=titleEt.text.toString()
                var details=detailEt.text.toString()
                var timer =0.0f.toDouble()
                if (title.isNotEmpty()&&details.isNotEmpty()){
                    var newTask= Task("",title,details,timer)
                    viewModel.addTask(newTask)
                    Toast.makeText(this@AddTaskActivity, "The task added successfully", Toast.LENGTH_LONG).show()
                    titleEt.setText("")
                    detailEt.setText("")
                    val tasksActivityIntent = Intent(this@AddTaskActivity,TasksActivity::class.java)
                    startActivity(tasksActivityIntent)
                    finish()
                }else{
                    Toast.makeText(this@AddTaskActivity, "Please fill all fields", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}//End of the class
