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

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private fun checkIfProfileAvailable(name: String, username: String): Profile? {
        var profile: Profile? = null
        if (name != "") {
            profile = profileDao.checkProfile(name, username)
        }
        return profile

    }
    fun inputProfile(name: String, list: ArrayList<Boolean>, username: String, viewModel : ProfileViewModel) {
        coroutineScope.launch {
            if(!name.isEmpty()){
            if (checkIfProfileAvailable(name, username) == null) {
                profileDao.insertProfile(
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
                    viewModel.navigateToProfileFragment()
                }
            }else{
                withContext(Dispatchers.Main){
                    viewModel.popupView.error_text.text = "Profile already exists"
                    viewModel.popupWindow.showAtLocation(viewModel.rootLayout, Gravity.CENTER, 0,0)
                }
            }

        }else{
                withContext(Dispatchers.Main){
                    viewModel.popupView.error_text.text = "Profile name mustn't be empty"
                    viewModel.popupWindow.showAtLocation(viewModel.rootLayout, Gravity.CENTER, 0,0)
                }
            }
        }
    }
    private fun getProfiles(username: String) = profileDao.getAllProfiles(username)
    fun submitNewList(username: String, adapter: ProfileRecyclerAdapter) {
        coroutineScope.launch {
            val list = getProfiles(username)
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }

    fun deleteProfile(profileId : Long) {
        coroutineScope.launch {
            profileDao.deleteProfile(profileId)

        }
    }
    fun getProfileFromDatabase(profileId: Long, viewModel: DetailProfileViewModel) {
        coroutineScope.launch {
            val profile = profileDao.getProfile(profileId)
            withContext(Dispatchers.Main) {
                viewModel.currentProfile.value = profile
            }
        }
    }

    override fun hideKeyboard(fragment: Fragment) {
            val imm =
                fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }


}