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
import com.example.tasktimerappkk.MainActivity.userData.user
import com.example.tasktimerappkk.Model.User
import com.example.tasktimerappkk.ViewModel.UsersViewModel
import com.example.tasktimerappkk.databinding.ActivityProfileBinding
import java.util.*

class ProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityProfileBinding
    lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadLocate()
        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        binding.apply {
            fullnameTv.text=user!!.username
            usernameTv.text=user!!.username
            emailTv.text=user!!.email

            logoutBtn.setOnClickListener {
                user=null
                val signOutIntent= Intent(this@ProfileActivity,MainActivity::class.java)
                startActivity(signOutIntent)
                finish()
            }
           langBtn.setOnClickListener {
               showChangeLang()
           }
            deleteBtn.setOnClickListener {
                if(user!=null){
                    DeletedDialog(user!!)
                }else{
                    Toast.makeText(this@ProfileActivity, getString(R.string.wrong),Toast.LENGTH_LONG).show()
                }

            }
            backBtn.setOnClickListener {
                val TasksActivityIntent= Intent(this@ProfileActivity,TasksActivity::class.java)
                startActivity(TasksActivityIntent)
                finish()
            }



        }
    }

    fun DeletedDialog(userDeleted: User) {
        val dialogBuilder = AlertDialog.Builder(this@ProfileActivity)

        dialogBuilder.setPositiveButton( getString(R.string.delete)) { dialog, id ->
            viewModel.deleteUser(userDeleted)
            val signOutIntent= Intent(this@ProfileActivity,MainActivity::class.java)
            startActivity(signOutIntent)
            finish()
        }
            .setNegativeButton( getString(R.string.cancel)) { dialog, id ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.deleteAccT))
        alert.setMessage(getString(R.string.deleteAcc))
        alert.show()
    }

    fun showChangeLang(){
        val listL= arrayOf("English","عربي")
        val mBuilder=AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.languChose))
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
        val locale= Locale(Lang)
        Locale.setDefault(locale)
        val config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor=getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("MyLang",Lang)
        editor.apply()
    }
    //=============================================================
    fun loadLocate(){
        val sharPrefreances=getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language=sharPrefreances.getString("MyLang","en")
        if (language != null) {
            setLocate(language)
        }
    }
    //=============================================================
}