package com.example.recipemanager.detailRecipe

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.*
import kotlinx.coroutines.*

class DetailRecipeViewModel(private val activity: Activity): ViewModel() {

    val favouriteChecker = MutableLiveData<Boolean?>()

    lateinit var popupView : View
    lateinit var popupWindow : PopupWindow


    init {
        setupPopupWindows()
    }
    private val _navigationToAllRecipes = MutableLiveData<Boolean?>()
    val navigationToAllRecipes: LiveData<Boolean?>
        get() = _navigationToAllRecipes

    private val _navigateToEditRecipe = MutableLiveData<Boolean?>()
    val navigateToEditRecipe : LiveData<Boolean?>
    get() = _navigateToEditRecipe



    fun navigateToEditRecipe(){
        _navigateToEditRecipe.value = true
    }

    fun navigationToEditRecipeDone(){
        _navigateToEditRecipe.value = null
    }

    fun navigateToAllRecipes() {
        _navigationToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigationToAllRecipes.value = null
    }

    private fun setupPopupWindows(){

        val inflater = LayoutInflater.from(activity)

        popupWindow = PopupWindow(activity)
        popupView = inflater.inflate(R.layout.popup, null)
        popupWindow.contentView = popupView
        popupWindow.isFocusable = true
    }
}