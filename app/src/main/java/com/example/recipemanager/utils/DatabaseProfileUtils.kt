package com.example.recipemanager.utils

import android.app.Application
import android.content.Context
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.detailProfile.DetailProfileViewModel
import com.example.recipemanager.profiles.ProfileRecyclerAdapter
import com.example.recipemanager.profiles.ProfileViewModel
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.coroutines.*

class DatabaseProfileUtils(application: Application) : HideKeyboardUtil {
    private val database = AppDatabase.getInstance(application)
    private val profileDao = database.profileDao


    fun checkProfile(name : String, username: String) = profileDao.checkProfile(name, username)

    fun getAllProfiles(username: String) = profileDao.getAllProfiles(username)

    fun deleteProfile(profileId : Long) = profileDao.deleteProfile(profileId)

    fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)

    fun getProfile(profileId: Long) = profileDao.getProfile(profileId)

    override fun hideKeyboard(fragment: Fragment) {
            val imm =
                fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }


}