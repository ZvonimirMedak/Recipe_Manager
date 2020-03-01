package com.example.recipemanager.userlogin

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

class LogInViewModel(private val activity: Activity,private val databaseUserUtils: DatabaseUserUtils) : ViewModel() {
    private var _navigateToRegisterFragment = MutableLiveData<Boolean?>()
    val navigateToRegisterFragment: LiveData<Boolean?>
        get() = _navigateToRegisterFragment

    private var _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment

    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View

    init {
        setupPopup()
    }
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun onVerificationClicked(username: String, password: String, rootLayout : View) {
        coroutineScope.launch {
            val currentUser = databaseUserUtils.getUserByName(username)
            if (currentUser != null) {
                if (passwordCheck(currentUser, password)) {
                    withContext(Dispatchers.Main){
                        navigateToProfileFragment()
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        popupView.error_text.text = "Wrong password"
                        popupWindow.showAtLocation(
                            rootLayout,
                            Gravity.CENTER,
                            0,
                            0
                        )
                    }
                }
            }else{
                withContext(Dispatchers.Main) {
                    popupView.error_text.text = "User with that username doesn't exist or the fields are empty"
                    popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
                }
            }
        }
    }
    private fun passwordCheck(user: User?, password: String) = user!!.password == password

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