package com.example.recipemanager.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.IngredientDao
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import kotlinx.coroutines.*

class AllRecipesViewModel : ViewModel(){
 // OVO PREBACIT U NOVI FRAGMENT ZA KREIRANJE RECEPTA A NE OVDJE


    private val _navigateToRecommendedRecipes = MutableLiveData<Boolean?>()
    val navigateToRecommendedRecipes : LiveData<Boolean?>
    get() = _navigateToRecommendedRecipes

    private val _navigateToMyIngredients = MutableLiveData<Boolean?>()
    val navigateToMyIngredients : LiveData<Boolean?>
    get() = _navigateToMyIngredients

    private val _navigateToDetailedRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailedRecipe : LiveData<Recipe?>
        get() = _navigateToDetailedRecipe

    private val _navigateToFavouriteRecipes = MutableLiveData<Boolean?>()
    val navigateToFavouriteRecipes : LiveData<Boolean?>
    get() = _navigateToFavouriteRecipes

    fun navigateToFavouriteRecipes(){
        _navigateToFavouriteRecipes.value = true
    }
    fun navigationToFavouriteRecipesDone(){
        _navigateToFavouriteRecipes.value = null
    }

    fun navigateToDetailedRecipe(recipe : Recipe){
        _navigateToDetailedRecipe.value = recipe
    }

    fun navigationToDetailedRecipeDone(){
        _navigateToDetailedRecipe.value = null
    }

    fun navigateToRecommendedRecipes(){
        _navigateToRecommendedRecipes.value = true
    }
    fun navigationToRecommendedRecipesDone(){
        _navigateToRecommendedRecipes.value = null
    }

    fun navigateToMyIngredients(){
        _navigateToMyIngredients.value = true
    }
    fun navigationToMyIngredientsDone(){
        _navigateToMyIngredients.value = null
    }
}