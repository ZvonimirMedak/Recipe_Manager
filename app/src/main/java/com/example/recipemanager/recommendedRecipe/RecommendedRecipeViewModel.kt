package com.example.recipemanager.recommendedRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe

class RecommendedRecipeViewModel : ViewModel() {
    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes: LiveData<Boolean?>
        get() = _navigateToAllRecipes

    private val _navigateToFavouriteRecipes = MutableLiveData<Boolean?>()
    val navigateToFavouriteRecipes: LiveData<Boolean?>
        get() = _navigateToFavouriteRecipes

    private val _navigateToMyIngredients = MutableLiveData<Boolean?>()
    val navigateToMyIngredients: LiveData<Boolean?>
        get() = _navigateToMyIngredients

    private val _navigateToDetailedRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailedRecipe: LiveData<Recipe?>
        get() = _navigateToDetailedRecipe


    fun navigateToFavouriteRecipes() {
        _navigateToFavouriteRecipes.value = true
    }

    fun navigationToFavouriteRecipesDone() {
        _navigateToFavouriteRecipes.value = null
    }

    fun navigateToDetailedRecipe(recipe: Recipe) {
        _navigateToDetailedRecipe.value = recipe
    }

    fun navigationToDetailedRecipeDone() {
        _navigateToDetailedRecipe.value = null
    }

    fun navigateToMyIngredients() {
        _navigateToMyIngredients.value = true
    }

    fun navigationToMyIngredientsDone() {
        _navigateToMyIngredients.value = null
    }

    fun navigateToAllRecipes() {
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigateToAllRecipes.value = null
    }
}