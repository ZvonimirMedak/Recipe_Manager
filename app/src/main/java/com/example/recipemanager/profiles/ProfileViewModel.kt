package com.example.recipemanager.profiles

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
import com.example.recipemanager.MainActivity
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.coroutines.*

class ProfileViewModel(private val activity: Activity, val rootLayout : View) : ViewModel() {
    private val _navigateToNewProfileFragment = MutableLiveData<Boolean?>()
    val navigateToNewProfileFragment: LiveData<Boolean?>
        get() = _navigateToNewProfileFragment

    private val _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment

    private val _profiles: List<Profile>? = ArrayList()
    val profiles: List<Profile>?
        get() = _profiles


    private val _navigateToDetailedProfile = MutableLiveData<Long?>()
    val navigateToDetailedProfile: LiveData<Long?>
        get() = _navigateToDetailedProfile


    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View

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

    fun navigateToDetailProfile(profileId: Long) {
        _navigateToDetailedProfile.value = profileId
    }

    fun navigationToDetailProfileDone() {
        _navigateToDetailedProfile.value = null
    }


    fun navigationToNewProfileFragmentDone() {
        _navigateToNewProfileFragment.value = null
    }

    fun navigateToNewProfileFragment() {
        _navigateToNewProfileFragment.value = true
    }

    fun navigateToProfileFragment() {
        _navigateToProfileFragment.value = true
    }

    fun navigationToProfileFragmentDone() {
        _navigateToProfileFragment.value = null
    }



    }

