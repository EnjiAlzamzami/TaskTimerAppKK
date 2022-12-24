package com.example.tasktimerappkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.Task
import com.example.tasktimerappkk.Model.TaskL
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding
    lateinit var viewModel: MyViewModel
    lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel= ViewModelProvider(this).get(MyViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding= ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context=this
        binding.apply {
            addTaskBtn.setOnClickListener {

                var title = titleEt.text.toString()
                var details = detailEt.text.toString()
                var timer = 0.0f.toDouble()
                if (title.isNotEmpty() && details.isNotEmpty()) {

                    //First case if the user loged in, then it will stored in a table in database
                    if(MainActivity.user!=null) {
                        var newTask = Task("", title, details, timer)
                        viewModel.addTask(newTask)
                        var userName=MainActivity.user!!.username
                        var newTaskLocal= TaskL(userName,0,title,details,timer)
                        viewModel.addLocalTask(newTaskLocal, MainActivity.user!!.username)
                    }//End first case
                    //The second case when the user is guest, then the tasks will store in the database
                    else{
                        var newTask= TaskL("guest",0,title,details,timer)
                        viewModel.addLocalTask(newTask,"guest")
                    }

                    Toast.makeText(
                        this@AddTaskActivity,
                        "The task added successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    titleEt.setText("")
                    detailEt.setText("")
                    var intent1 = Intent(context, TasksActivity::class.java)
                    context.startActivity(intent1)
                } else {
                    Toast.makeText(
                        this@AddTaskActivity,
                        "Please fill all fields",
                        Toast.LENGTH_LONG
                    ).show()
                }


                //=======================================================


            }

        }
    }
}//End of the class
