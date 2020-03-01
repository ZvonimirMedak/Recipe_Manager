package com.example.recipemanager.detailProfile

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import com.example.recipemanager.utils.DatabaseProfileUtils
import kotlinx.coroutines.*

class DetailProfileViewModel(private val databaseProfileUtils: DatabaseProfileUtils) : ViewModel() {
    private val _currentProfile = MutableLiveData<Profile?>()
    val currentProfile : LiveData<Profile?>
    get() = _currentProfile

    private val _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment

    private val _navigateToRecipes = MutableLiveData<Boolean?>()
    val navigateToRecipes: LiveData<Boolean?>
        get() = _navigateToRecipes

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun deleteProfile(profileId : Long) {
        coroutineScope.launch {
            databaseProfileUtils.deleteProfile(profileId)

        }
    }
    fun getProfileFromDatabase(profileId: Long) {
        coroutineScope.launch {
            val profile = databaseProfileUtils.getProfile(profileId)
            withContext(Dispatchers.Main) {
                _currentProfile.value = profile
            }
        }
    }
    fun navigateToRecipes() {
        _navigateToRecipes.value = true
    }

    fun navigateToRecipesDone() {
        _navigateToRecipes.value = null
    }

    fun navigateToProfileFragment() {
        _navigateToProfileFragment.value = true
    }

    fun navigationToProfileFragmentDone() {
        _navigateToProfileFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}