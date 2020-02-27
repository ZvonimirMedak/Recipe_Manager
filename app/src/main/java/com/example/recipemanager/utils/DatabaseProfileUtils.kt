package com.example.recipemanager.utils

import android.app.Application
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.profiles.ProfileRecyclerAdapter
import com.example.recipemanager.profiles.ProfileViewModel
import kotlinx.coroutines.*

class DatabaseProfileUtils(application: Application) {
    val database = AppDatabase.getInstance(application)
    val profileDao = database.profileDao

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
}