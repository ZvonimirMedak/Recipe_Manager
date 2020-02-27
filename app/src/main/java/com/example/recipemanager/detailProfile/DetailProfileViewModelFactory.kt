package com.example.recipemanager.detailProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipemanager.appDatabase.ProfileDao

class DetailProfileViewModelFactory(
    private val profileId: Long,
    private val dataSource: ProfileDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailProfileViewModel::class.java)) {
            return DetailProfileViewModel(profileId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}