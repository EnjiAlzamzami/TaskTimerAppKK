package com.example.tasktimerappkk.Model

import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterRepository : AppCompatActivity() {

    val db = Firebase.firestore

    fun addUser(user: User){
        CoroutineScope(Dispatchers.IO).launch {
            val newNote = hashMapOf(
                "id" to user.id,
                "username" to user.username,
                "email" to user.email,
                "password" to user.password,
            )
            db.collection("UsersCollection").add(newNote)
            getUsers()
        }
    }

    suspend fun getUser(user:User){
        db.collection("UsersCollection").document(user.id)
            .get()



    }
    suspend fun deleteUser(user: User){

        db.collection("UsersCollection").document(user.id).delete()
    }


    fun getUsers(): ArrayList<User>{
        var users:ArrayList<User>

        users= arrayListOf()
        users.clear()
        db.collection("UsersCollection")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {


                    var username=document.get("username").toString()
                    var emails=document.get("email").toString()
                    var password=document.get("password").toString()
                    println("User= $username")

                    users.add(User(document.id,username,emails,password))


                }

            }

            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
        return users
    }

    //========================================
}