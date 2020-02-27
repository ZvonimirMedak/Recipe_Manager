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

class DetailProfileViewModel : ViewModel() {

    val currentProfile = MutableLiveData<Profile?>()

    private val _navigateToProfileFragment = MutableLiveData<Boolean?>()
    val navigateToProfileFragment: LiveData<Boolean?>
        get() = _navigateToProfileFragment

    private val _navigateToRecipes = MutableLiveData<Boolean?>()
    val navigateToRecipes: LiveData<Boolean?>
        get() = _navigateToRecipes


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

}