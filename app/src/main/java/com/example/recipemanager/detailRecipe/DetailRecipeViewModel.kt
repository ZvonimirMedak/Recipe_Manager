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
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.coroutines.*

class DetailRecipeViewModel(private val activity: Activity, private val databaseRecipeUtils: DatabaseRecipeUtils, private val databaseIngredientsUtils: DatabaseIngredientsUtils): ViewModel() {
    private val _favouriteChecker = MutableLiveData<Boolean?>()
    val favouriteChecker : LiveData<Boolean?>
    get() = _favouriteChecker

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

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)


    fun checkFavourite(recipeId: Long, profileId: Long) {
        coroutineScope.launch {
            val list = databaseRecipeUtils.getFavourites(profileId)
            if (list != null) {
                for (favourite in list) {
                    if (favourite.recipeId == recipeId)
                        withContext(Dispatchers.Main) {
                            _favouriteChecker.value = true
                        }
                }
            }
        }

    }

    fun deleteRecipe(recipe: Recipe, profileId: Long) {
        coroutineScope.launch {
            val favourite = databaseRecipeUtils.getFavouriteRecipe(profileId, recipe.recipeId)
            if (favourite != null) {
                databaseRecipeUtils.deleteFavourite(favourite.favouriteId)
            }
            databaseRecipeUtils.deleteRecipe(recipe.recipeId)
            val list = databaseIngredientsUtils.getRecipeIngredients(recipe.recipeId)
            for (ingredient in list!!) {
                databaseIngredientsUtils.deleteIngredient(ingredient.ingredientId)
            }
        }
        navigateToAllRecipes()
    }

    fun createFavouriteRecipe(recipeId: Long, profileId: Long) {
        coroutineScope.launch {
            if (favouriteChecker.value == null) {
                databaseRecipeUtils.insertFavourite(Favourite(profileId = profileId, recipeId = recipeId))
                checkFavourite(recipeId, profileId)
            } else {
                withContext(Dispatchers.Main) {
                    _favouriteChecker.value = null
                }
                val favourite = databaseRecipeUtils.getFavouriteRecipe(profileId, recipeId)
                databaseRecipeUtils.deleteFavourite(favourite!!.favouriteId)
            }

        }
    }
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