package com.example.tasktimerappkk.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.addtask.Model.Task
import com.example.tasktimerappkk.Model.RegisterRepository
import com.example.tasktimerappkk.Model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel (application: Application): AndroidViewModel(application) {
    private val repository:RegisterRepository
    private lateinit var users: ArrayList<User>
    val db = Firebase.firestore


    init {

        repository = RegisterRepository()
        CoroutineScope(Dispatchers.IO).launch {
            users = repository.getUsers()


        }
    }

    fun getUser(): ArrayList<User> {

        return repository.getUsers()

    }

    fun addUser(user:User) {
        CoroutineScope(Dispatchers.IO).launch {

            repository.addUser(user)
        }
    }



    fun deleteUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteUser(user)
        }
    }
}//End of the class