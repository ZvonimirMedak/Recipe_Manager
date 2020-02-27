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
                }else{
                    withContext(Dispatchers.Main) {
                        viewModel.popupView.error_text.text = "User with that username doesn't exist"
                        viewModel.popupWindow.showAtLocation(
                            viewModel.rootLayout,
                            Gravity.CENTER,
                            0,
                            0
                        )
                    }
                }
            }else{
                withContext(Dispatchers.Main) {
                    viewModel.popupView.error_text.text = "Login fields mustn't be empty"
                    viewModel.popupWindow.showAtLocation(viewModel.rootLayout, Gravity.CENTER, 0, 0)
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

                    }else{
                        withContext(Dispatchers.Main){
                            viewModel.popupView.error_text.text = "User with that username already exists"
                            viewModel.popupWindow.showAtLocation(viewModel.rootLayout, Gravity.CENTER,0,0)
                        }
                    }
                }
        }else{
            viewModel.popupView.error_text.text = "Username empty or wrong password"
            viewModel.popupWindow.showAtLocation(viewModel.rootLayout, Gravity.CENTER, 0,0)
        }
    }

    private fun checkUser(name: String): User? {
        if (name != "") {
            return userDao.getUser(name)
        }
        return null
    }

    private fun verifyPassword(password: String, confirmation: String) = password == confirmation

    override fun hideKeyboard(fragment: Fragment) {
        val imm =
            fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }
}