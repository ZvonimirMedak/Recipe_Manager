package com.example.recipemanager.favouriteRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe

class FavouriteRecipeViewModel : ViewModel() {
    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes: LiveData<Boolean?>
        get() = _navigateToAllRecipes

    private val _navigateToMyIngredients = MutableLiveData<Boolean?>()
    val navigateToMyIngredients: LiveData<Boolean?>
        get() = _navigateToMyIngredients

    private val _navigateToRecommendedRecipes = MutableLiveData<Boolean?>()
    val navigateToRecommendedRecipes: LiveData<Boolean?>
        get() = _navigateToRecommendedRecipes

    private val _navigateToDetailedRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailRecipe: LiveData<Recipe?>
        get() = _navigateToDetailedRecipe

    fun navigateToAllRecipes() {
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigateToAllRecipes.value = null
    }

    fun navigateToMyIngredients() {
        _navigateToMyIngredients.value = true
    }

    fun navigationToMyIngredientsDone() {
        _navigateToMyIngredients.value = null
    }

    fun navigateToRecommendedRecipes() {
        _navigateToRecommendedRecipes.value = true
    }

    fun navigationToRecommendedRecipesDone() {
        _navigateToRecommendedRecipes.value = null
    }

    fun navigateToDetailedRecipe(recipe: Recipe) {
        _navigateToDetailedRecipe.value = recipe
    }

    fun navigationToDetailedRecipeDone() {
        _navigateToDetailedRecipe.value = null
    }
}