package com.example.tasktimerappkk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.Model.User
import com.example.tasktimerappkk.ViewModel.UsersViewModel
import com.example.tasktimerappkk.databinding.ActivityMainBinding
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

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
        loadLocate()
        viewModel= ViewModelProvider(this).get(UsersViewModel::class.java)
        //Get users form the fierbase
        users= arrayListOf<User>()
        users=viewModel.getUser()
        binding.apply {
            langBtn.setOnClickListener{
                showChangeLang()
            }
            //---------------------------------------------------
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


            }//End loginBtn.setOnClickListener

            guestBtn.setOnClickListener {
                var intent = Intent(context, AddTaskActivity::class.java)
                context.startActivity(intent)
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
    //=============================================================
    fun showChangeLang(){
        var listL= arrayOf("English","عربي")
        val mBuilder=AlertDialog.Builder(context)
        mBuilder.setTitle("Choose your Language")
        mBuilder.setSingleChoiceItems(listL,-1){
          dialog,which->
            if (which==0) {
                setLocate("en")
                recreate()
            }
            else{
                setLocate("ar")
                recreate()
            }
            dialog.dismiss()
        }
        mBuilder.create().show()

    }
    //=============================================================
    fun setLocate(Lang:String){
        var locale= Locale(Lang)
        Locale.setDefault(locale)
        val config=Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor=getSharedPreferences("Settings",Context.MODE_PRIVATE).edit()
        editor.putString("MyLang",Lang)
        editor.apply()
    }
    //=============================================================
    fun loadLocate(){
        val sharPrefreances=getSharedPreferences("Settings",Activity.MODE_PRIVATE)
        var language=sharPrefreances.getString("MyLang","en")
        if (language != null) {
            setLocate(language)
        }
    }
    //=============================================================
    //This to save user who is register
    companion object userData{
        var user: User? =null
        var localStorage="tasks"
    }
}