package com.example.tasktimerappkk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.addtask.Model.Task
import com.example.addtask.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityAddTaskBinding

class TasksActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddTaskBinding
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel= ViewModelProvider(this).get(MyViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding=ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            addTaskBtn.setOnClickListener {
                var title=titleEt.text.toString()
                var details=detailEt.text.toString()
                var timer =0.0f.toDouble()
                if (title.isNotEmpty()&&details.isNotEmpty()){
                    var newTask= Task("",title,details,timer)
                    viewModel.addTask(newTask)
                    Toast.makeText(this@TasksActivity, "The task added successfully", Toast.LENGTH_LONG).show()
                    titleEt.setText("")
                    detailEt.setText("")

                }
                else
                {
                    Toast.makeText(this@TasksActivity, "Please fill all fields", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}