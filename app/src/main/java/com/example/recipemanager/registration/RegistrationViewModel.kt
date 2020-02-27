package com.example.recipemanager.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao

import kotlinx.coroutines.*

class RegistrationViewModel(val userDatabaseDao: UserDatabaseDao) : ViewModel() {

    private var _navigateToLogIn = MutableLiveData<Boolean>()
    val navigateToLogIn: LiveData<Boolean>
        get() = _navigateToLogIn

    private val logInJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + logInJob)


    fun insertNewUser(name: String, password: String, confirmation: String) {
        if (verifyPassword(password, confirmation) && name != "") {

            uiScope.launch {
                var done = false
                withContext(Dispatchers.IO) {
                    if (checkUser(name) == null) {
                        userDatabaseDao.insert(
                            User(
                                name,
                                password
                            )
                        )
                        done = true
                        Log.d("translacija", "proslo")
                    }
                }
                if (done)
                    navigationToLogInWanted()
            }
        }
    }

    private fun checkUser(name: String): User? {
        var user: User? = null
        if (name != "") {
            user = userDatabaseDao.getUser(name)
        }
        Log.d("user", user.toString())
        return user
    }

    fun navigationToLogInWanted() {
        _navigateToLogIn.value = true
    }

    override fun onCleared() {
        super.onCleared()
        logInJob.cancel()
    }

    fun navigationToLogInDone() {
        _navigateToLogIn.value = null
    }

    fun verifyPassword(password: String, confirmation: String) = password == confirmation

}