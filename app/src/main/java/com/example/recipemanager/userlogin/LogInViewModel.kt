package com.example.recipemanager.userlogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao
import kotlinx.coroutines.*

class LogInViewModel : ViewModel() {
    private var _navigateToRegisterFragment = MutableLiveData<Boolean?>()
    val navigateToRegisterFragment: LiveData<Boolean?>
        get() = _navigateToRegisterFragment

    private var _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment



    fun registrationWanted() {
        _navigateToRegisterFragment.value = true
    }

    fun navigationToRegisterDone() {
        _navigateToRegisterFragment.value = null
    }

    fun navigateToProfileFragment(){
        _navigateToProfileFragment.value = true
    }

    fun navigationToProfileFragmentDone() {
        _navigateToProfileFragment.value = null
    }

}