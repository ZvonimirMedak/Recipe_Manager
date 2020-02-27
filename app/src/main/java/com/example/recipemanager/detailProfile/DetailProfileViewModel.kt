package com.example.recipemanager.detailProfile

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import kotlinx.coroutines.*

class DetailProfileViewModel(private val profileId: Long, private val databaseDao: ProfileDao) :
    ViewModel() {
    private val detailProfileJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + detailProfileJob)
    val currentProfile = MutableLiveData<Profile?>()
    var profile: Profile? = null

    private val _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment

    private val _navigateToRecipes = MutableLiveData<Boolean?>()
    val navigateToRecipes: LiveData<Boolean?>
        get() = _navigateToRecipes


    init {
        uiScope.launch {
            currentProfile.value = getProfileFromDatabase()
            Log.d("msg", currentProfile.value.toString())
        }

    }


    fun navigateToRecipesWanted() {
        _navigateToRecipes.value = true
    }

    fun navigateToRecipesDone() {
        _navigateToRecipes.value = null
    }

    private fun navigateToProfileFragmentWanted() {
        _navigateToProfileFragment.value = true
    }

    fun navigateToProfileFragmentDone() {
        _navigateToProfileFragment.value = null
    }

    fun deleteProfile() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                databaseDao.deleteProfile(profileId)
            }
            navigateToProfileFragmentWanted()
        }
    }

    override fun onCleared() {
        super.onCleared()
        detailProfileJob.cancel()
    }

    private suspend fun getProfileFromDatabase(): Profile? {
        return withContext(Dispatchers.IO) {
            val profile = databaseDao.getProfile(profileId)
            profile

        }
    }


}