package com.example.recipemanager.registration

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.User
import com.example.recipemanager.appDatabase.UserDatabaseDao
import kotlinx.android.synthetic.main.error_popup.view.*

import kotlinx.coroutines.*

class RegistrationViewModel(private val activity: Activity, val rootLayout : View) : ViewModel() {

    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View

    private var _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun navigateToLogin() {
        _navigateToLogin.value = true
    }

    fun navigationToLoginDone() {
        _navigateToLogin.value = null
    }

    init {
        setupPopup()
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

}