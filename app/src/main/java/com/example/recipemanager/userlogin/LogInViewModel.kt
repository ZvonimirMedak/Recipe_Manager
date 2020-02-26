package com.example.recipemanager.userlogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao
import kotlinx.coroutines.*

class LogInViewModel (val userDatabaseDao: UserDatabaseDao): ViewModel(){
    private var _navigateToRegisterFragment = MutableLiveData<Boolean?>()
    val navigateToRegisterFragment : LiveData<Boolean?>
        get() = _navigateToRegisterFragment

    private var _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment : LiveData<Boolean?>
        get() = _navigateToProfileFragment

    private val logInJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + logInJob)

    override fun onCleared() {
        super.onCleared()
        logInJob.cancel()
    }

    fun registrationWanted(){
        _navigateToRegisterFragment.value = true
    }

    fun navigationToRegisterDone(){
        _navigateToRegisterFragment.value  = null
    }

    private var currentUser : User? = null
    fun onVerificationClicked(username: String, password : String) {
        uiScope.launch {
            var done = false
            withContext(Dispatchers.IO){
                currentUser = userDatabaseDao.getUser(username)
                if(currentUser != null){
                    if(passwordCheck(currentUser, password)){
                        done = true
                    }

                }

            }
            if(done == true)
            _navigateToProfileFragment.value = true

        }


    }
    fun navigationToProfileFragmentDone(){
        _navigateToProfileFragment.value = null
    }
    private fun passwordCheck(user: User?, password: String): Boolean{
        return user!!.password == password
    }
}


