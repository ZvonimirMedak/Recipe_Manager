package com.example.recipemanager.utils

import android.app.Application
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.registration.RegistrationViewModel
import com.example.recipemanager.userlogin.LogInViewModel
import kotlinx.coroutines.*

class DatabaseUserUtils(application: Application){
    private val database = AppDatabase.getInstance(application)
    private val userDao = database.userDatabaseDao

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun onVerificationClicked(username: String, password: String, viewModel: LogInViewModel) {
        coroutineScope.launch {
            val currentUser = userDao.getUser(username)
            if (currentUser != null) {
                if (passwordCheck(currentUser, password)) {
                    withContext(Dispatchers.Main){
                        viewModel.navigateToProfileFragment()
                    }
                }
            }
        }
    }
    private fun passwordCheck(user: User?, password: String): Boolean {
        return user!!.password == password
    }

    fun insertNewUser(name: String, password: String, confirmation: String, viewModel : RegistrationViewModel) {
        if (verifyPassword(password, confirmation) && name != "") {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    if (checkUser(name) == null) {
                        userDao.insert(
                            User(
                                name,
                                password
                            )
                        )
                        withContext(Dispatchers.Main){
                            viewModel.navigateToLogin()
                        }

                    }
                }
            }
        }
    }

    private fun checkUser(name: String): User? {
        if (name != "") {
            return userDao.getUser(name)
        }
        return null
    }

    private fun verifyPassword(password: String, confirmation: String) = password == confirmation
}