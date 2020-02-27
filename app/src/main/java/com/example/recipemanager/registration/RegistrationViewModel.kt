package com.example.recipemanager.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao

import kotlinx.coroutines.*

class RegistrationViewModel() : ViewModel() {

    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun navigateToLogin() {
        _navigateToLogin.value = true
    }

    fun navigationToLoginDone() {
        _navigateToLogin.value = null
    }



}