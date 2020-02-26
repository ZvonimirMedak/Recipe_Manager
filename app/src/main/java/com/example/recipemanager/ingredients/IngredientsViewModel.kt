package com.example.recipemanager.ingredients

import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.IngredientDao
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class IngredientsViewModel(val ingredientDao : IngredientDao, val deletePopup : PopupWindow, val deletePopupView : View, val rootLayout : View) : ViewModel(){
    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes : LiveData<Boolean?>
    get() = _navigateToAllRecipes

    private val _navigateToRecommendedRecipes = MutableLiveData<Boolean?>()
    val navigateToRecommendedRecipes : LiveData<Boolean?>
    get() = _navigateToRecommendedRecipes

    private val _navigateToFavouriteRecipes = MutableLiveData<Boolean?>()
    val navigateToFavouriteRecipes :LiveData<Boolean?>
    get() = _navigateToFavouriteRecipes

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun navigateToFavouriteRecipes(){
        _navigateToFavouriteRecipes.value = true
    }

    fun navigationToFavouriteRecipesDone(){
        _navigateToFavouriteRecipes.value = null
    }

    fun navigateToRecommendedRecipes(){
        _navigateToRecommendedRecipes.value = true
    }
    fun navigationToRecommendedRecipesDone(){
        _navigateToRecommendedRecipes.value = null
    }

    fun navigateToAllRecipes(){
        _navigateToAllRecipes.value = true
    }
    fun navigationToAllRecipesDone(){
        _navigateToAllRecipes.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.complete()
    }

}