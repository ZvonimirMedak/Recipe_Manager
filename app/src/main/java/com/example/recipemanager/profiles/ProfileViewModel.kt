package com.example.recipemanager.profiles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import kotlinx.coroutines.*

class ProfileViewModel(val databaseDao: ProfileDao) : ViewModel() {
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

    private val profileJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + profileJob)

    lateinit var username: String


    fun onProfileClicked(profileId: Long) {
        _navigateToDetailedProfile.value = profileId
    }

    fun navigateToDetailProfileDone() {
        _navigateToDetailedProfile.value = null
    }


    override fun onCleared() {
        super.onCleared()
        profileJob.cancel()
    }

    fun navigateToNewProfileFragmentDone() {
        _navigateToNewProfileFragment.value = null
    }

    fun navigateToNewProfileFragmentWanted() {
        _navigateToNewProfileFragment.value = true
    }

    fun navigateToProfileFragmentWanted() {
        _navigateToProfileFragment.value = true
    }

    fun navigateToProfileFragmentDone() {
        _navigateToProfileFragment.value = null
    }

    fun inputProfile(name: String, list: ArrayList<Boolean>, username: String) {

        uiScope.launch {
            var done = false
            withContext(Dispatchers.IO) {
                if (checkIfProfileAvailable(name) == null) {
                    databaseDao.insertProfile(
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
                    done = true
                }

            }

            if (done)
                navigateToProfileFragmentWanted()
        }

    }

    private fun checkIfProfileAvailable(name: String): Profile? {
        var profile: Profile? = null
        if (name != "") {
            profile = databaseDao.checkProfile(name, username)
        }
        return profile

    }
}