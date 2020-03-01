package com.example.recipemanager.ingredients

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.IngredientDao
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class IngredientsViewModel(
    private val activity: Activity
) : ViewModel() {

    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View


    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes: LiveData<Boolean?>
        get() = _navigateToAllRecipes

    private val _navigateToRecommendedRecipes = MutableLiveData<Boolean?>()
    val navigateToRecommendedRecipes: LiveData<Boolean?>
        get() = _navigateToRecommendedRecipes

    private val _navigateToFavouriteRecipes = MutableLiveData<Boolean?>()
    val navigateToFavouriteRecipes: LiveData<Boolean?>
        get() = _navigateToFavouriteRecipes

    init {
        setupPopupWindow()
    }


    private fun setupPopupWindow(){

        val inflater = LayoutInflater.from(activity)

        popupWindow = PopupWindow(activity)
        popupView = inflater.inflate(R.layout.popup, null)
        popupWindow.contentView = popupView
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )


    }


    fun navigateToFavouriteRecipes() {
        _navigateToFavouriteRecipes.value = true
    }

    fun navigationToFavouriteRecipesDone() {
        _navigateToFavouriteRecipes.value = null
    }

    fun navigateToRecommendedRecipes() {
        _navigateToRecommendedRecipes.value = true
    }

    fun navigationToRecommendedRecipesDone() {
        _navigateToRecommendedRecipes.value = null
    }

    fun navigateToAllRecipes() {
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigateToAllRecipes.value = null
    }

}