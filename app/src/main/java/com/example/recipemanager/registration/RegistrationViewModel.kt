package com.example.recipemanager.registration

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao
import com.example.recipemanager.utils.DatabaseUserUtils
import kotlinx.android.synthetic.main.error_popup.view.*

import kotlinx.coroutines.*

class RegistrationViewModel(private val activity: Activity, private val databaseUserUtils: DatabaseUserUtils) : ViewModel() {

    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View

    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)


    init {
        setupPopup()
    }

    fun insertNewUser(name: String, password: String, confirmation: String, rootLayout : View) {
        if (verifyPassword(password, confirmation) && name != "") {
            coroutineScope.launch {
                if (checkUser(name) == null) {
                    databaseUserUtils.insertUser(
                        User(
                            name,
                            password
                        )
                    )
                    withContext(Dispatchers.Main){
                        navigateToLogin()
                    }

                }else{
                    withContext(Dispatchers.Main){
                        popupView.error_text.text = "User with that username already exists"
                        popupWindow.showAtLocation(rootLayout, Gravity.CENTER,0,0)
                    }
                }
            }
        }else{
            popupView.error_text.text = "Username empty or wrong password"
            popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0,0)
        }
    }
    private fun verifyPassword(password: String, confirmation: String) = password == confirmation

    private fun navigateToLogin() {
        _navigateToLogin.value = true
    }

    fun navigationToLoginDone() {
        _navigateToLogin.value = null
    }
    private fun setupPopup(){
        val inflater = LayoutInflater.from(activity)
        popupWindow = PopupWindow(activity)
        popupView = inflater.inflate(R.layout.error_popup, null)
        popupWindow.isFocusable = true
        popupWindow.contentView = popupView
        popupView.ok_button.setOnClickListener {
            popupWindow.dismiss()
        }
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
    }

    private fun checkUser(name: String): User? {
        if (name != "") {
            return databaseUserUtils.getUserByName(name)
        }
        return null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}