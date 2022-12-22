package com.example.tasktimerappkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.User
import com.example.tasktimerappkk.ViewModel.UsersViewModel
import com.example.tasktimerappkk.databinding.ActivityMainBinding
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var context: Context
    lateinit var viewModel: UsersViewModel
    lateinit var users:ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        context=this
        viewModel= ViewModelProvider(this).get(UsersViewModel::class.java)
        //Get users form the fierbase
        users= arrayListOf<User>()
        users=viewModel.getUser()
        binding.apply {
            signupBtn.setOnClickListener {
                var intentSignUp = Intent(context, SignUp::class.java)
               context.startActivity(intentSignUp)
            }
            loginBtn.setOnClickListener {
                var userName=nameEt.text.toString()
                var password=md5Hash(passEt.text.toString())
                for (user in users){
                    if (userName==user.username)
                    {
                        if (password==user.password){
                            userData.user=user

                            var intent1 = Intent(context, TasksActivity::class.java)
                            context.startActivity(intent1)

                        }
                        else{
                            Toast.makeText(this@MainActivity,"Please enter a correct password & user name",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }
        }
    }

    //===========================================================
    //This function to hash password before compare it with one in database
    fun md5Hash(str: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
    //This to save user who is register
    companion object userData{
        var user: User? =null
        var localStorage="tasks"
    }
}