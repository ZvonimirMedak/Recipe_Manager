package com.example.recipemanager.profiles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import kotlinx.coroutines.*

class ProfileViewModel : ViewModel() {
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

