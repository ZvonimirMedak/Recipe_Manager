package com.example.recipemanager.utils

import android.app.Application
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.registration.RegistrationViewModel
import com.example.recipemanager.userlogin.LogInViewModel
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.coroutines.*

class DatabaseUserUtils(application: Application) : HideKeyboardUtil{
    private val database = AppDatabase.getInstance(application)
    private val userDao = database.userDatabaseDao

    fun insertUser(user: User) = userDao.insert(user)

    fun getUserByName(name : String) = userDao.getUser(name)


    override fun hideKeyboard(fragment: Fragment) {
        val imm =
            fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }
}