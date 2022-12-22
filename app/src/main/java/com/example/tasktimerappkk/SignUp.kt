package com.example.tasktimerappkk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimerappkk.ViewModel.MyViewModel
import com.example.tasktimerappkk.Model.User
import com.example.tasktimerappkk.ViewModel.UsersViewModel
import com.example.tasktimerappkk.databinding.ActivitySignUpBinding
import java.math.BigInteger
import java.security.MessageDigest
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var users:ArrayList<User>
    lateinit var viewModel: UsersViewModel
    var new=true//this to check if the user is new user
    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel= ViewModelProvider(this).get(UsersViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        users= arrayListOf()
        users=viewModel.getUser()
        binding.apply {

            signUpBt.setOnClickListener {
                new =true//change the sate to true each time user click the button
                var name=name.text.toString()
                var email=emailET.text.toString()
                var password=passwordET.text.toString()
                var confirm=confirmET.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()){
                    for (user in users){
                        println(" for (user in users), ${user.id}")

                        if (name==user.username)
                        {
                            new=false
                            Toast.makeText(this@SignUp, "This user already exist Please change user name", Toast.LENGTH_LONG).show()
                        }
                        else if ( email==user.email){
                            new=false
                            Toast.makeText(this@SignUp, "This user already exist Please change user name", Toast.LENGTH_LONG).show()
                        }
                    }//end for
                    if (new&&checkEmailConstrain(email)&&correctPassword(password,confirm)){

                        //Create new user object then added in the database:
                        var user=User("", name,email,md5Hash(password))
                        viewModel.addUser(user)
                        Toast.makeText(this@SignUp, "User Added Successfully", Toast.LENGTH_LONG).show()
                        var intent = Intent(this@SignUp, MainActivity::class.java)
                        this@SignUp.startActivity(intent)
                    }


                }
                else{
                    Toast.makeText(this@SignUp, "please fill all fields", Toast.LENGTH_LONG).show()
                }
            }//End signUpBt.setOnClickListener
        }
    }//End onCreate function
    //========================================================================
    private fun checkEmailConstrain(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")
        if(EMAIL_ADDRESS_PATTERN.matcher(email).matches()) return true
        else  Toast.makeText(this,"The email must follow the pattern example@organization.extention",Toast.LENGTH_SHORT).show()
        return false
        //=====================================================================

    }//End of checkEmailConstrain
    //========================================================================
    fun correctPassword(p1:String,p2:String):Boolean{
        return hasLower(p1)&&hasNumber(p1)&&hasUpper(p1)&&match(p1,p2)
    }
    private fun hasUpper(text: String): Boolean{
        for (i in 0..text.length-1){
            if (text[i].isUpperCase())
            {
                return true
            }
        }
        Toast.makeText(this,"The password must include Upper case, lower case letter, and number",Toast.LENGTH_SHORT).show()
        return false
    }

    private fun hasLower(text: String): Boolean{
        for (i in 0..text.length-1){
            if (text[i].isLowerCase())
            {
                return true
            }
        }
        Toast.makeText(this,"The password must include Upper case, lower case letter, and number",Toast.LENGTH_SHORT).show()
        return false
    }

    private fun hasNumber(text: String): Boolean{
        for(i in 0..9){
            if(text.contains(i.toString())){
                return true
            }
        }
        Toast.makeText(this,"The password must include Upper case, lower case letter, and number",Toast.LENGTH_SHORT).show()
        return false
    }

    fun match(password: String,password2: String):Boolean
    {
        if(password==password2){
            return true
        }
        else
        {
            Toast.makeText(this,"The password must match its confirm",Toast.LENGTH_SHORT).show()

        }
        return false
    }
    //===========================================================
    //===========================================================
    //This function to hash password before save it in database
    fun md5Hash(str: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
}