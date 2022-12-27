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
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: UsersViewModel
    private lateinit var users:ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadLocate()
        viewModel= ViewModelProvider(this)[UsersViewModel::class.java]
        //Get users form the fierbase
        users= arrayListOf<User>()
        users=viewModel.getUser()
        binding.apply {
            langBtn.setOnClickListener{
                showChangeLang()
            }
            //---------------------------------------------------
            signupBtn.setOnClickListener {
                val intentSignUp = Intent(this@MainActivity, SignUp::class.java)
               startActivity(intentSignUp)
                finish()
            }
            loginBtn.setOnClickListener {
                val userName=nameEt.text
                val password=md5Hash(passEt.text.toString())
                if(userName.toString().isNotEmpty()&&passEt.text.isNotEmpty()){
                    for (user in users){
                         if(true) {
                             if (userName.toString() == user.username) {
                                 if (password == user.password) {
                                     userData.user = user
                                     nameEt.setText("")
                                     passEt.setText("")

                                     val TasksActivityIntent =
                                         Intent(this@MainActivity, TasksActivity::class.java)
                                     startActivity(TasksActivityIntent)
                                     finish()


                                 } else {
                                     Toast.makeText(
                                         this@MainActivity, getString(R.string.loginWrnning),
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 }
                             }
                         }else{
                             Toast.makeText(this@MainActivity,"The user doesn't found!",
                                 Toast.LENGTH_SHORT).show()

                         }
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Please Enter the required data!",
                        Toast.LENGTH_SHORT).show()
                }



            }//End loginBtn.setOnClickListener

            guestBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, TasksActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    //===========================================================
    //This function to hash password before compare it with one in database
    private fun md5Hash(str: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
    //=============================================================
    private fun showChangeLang(){
        val listL= arrayOf("English","عربي")
        val mBuilder=AlertDialog.Builder(this)
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
    private fun setLocate(Lang:String){
        val locale= Locale(Lang)
        Locale.setDefault(locale)
        val config=Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor=getSharedPreferences("Settings",Context.MODE_PRIVATE).edit()
        editor.putString("MyLang",Lang)
        editor.apply()
    }
    //=============================================================
    private fun loadLocate(){
        val sharPrefreances=getSharedPreferences("Settings",Activity.MODE_PRIVATE)
        val language=sharPrefreances.getString("MyLang","en")
        if (language != null) {
            setLocate(language)
        }
    }
    //=============================================================
    //This to save user who is register
    companion object userData{
        var user: User? =null
    }
}