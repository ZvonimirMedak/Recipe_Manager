package com.example.recipemanager.profiles

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
import com.example.recipemanager.appDatabase.Profile

import com.example.recipemanager.utils.DatabaseProfileUtils
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.coroutines.*

class ProfileViewModel(private val activity: Activity,private val databaseProfileUtils: DatabaseProfileUtils) : ViewModel() {
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


    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView : View

    init {
        setupPopup()
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun inputProfile(name: String, list: ArrayList<Boolean>, username: String, rootLayout: View) {
        coroutineScope.launch {
            if(!name.isEmpty()){
                if (checkIfProfileAvailable(name, username) == null) {
                    databaseProfileUtils.insertProfile(
                        Profile(
                            0,
                            name,
                            list[0],
                            list[1],
                            list[2],
                            list[3],
                            username
                        )
                    )
                    withContext(Dispatchers.Main){
                        navigateToProfileFragment()
                    }
                }else{
                    withContext(Dispatchers.Main){
                        popupView.error_text.text = "Profile already exists"
                        popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0,0)
                    }
                }

            }else{
                withContext(Dispatchers.Main){
                    popupView.error_text.text = "Profile name mustn't be empty"
                    popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0,0)
                }
            }
        }
    }
    fun submitNewList(adapter: ProfileRecyclerAdapter, username: String) {
        coroutineScope.launch {
            val list = databaseProfileUtils.getAllProfiles(username)
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }



    private fun checkIfProfileAvailable(name: String, username: String): Profile? {
        var profile: Profile? = null
        if (name != "") {
            profile = databaseProfileUtils.checkProfile(name, username)
        }
        return profile

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

