package com.example.recipemanager.favouriteRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.coroutines.*

class FavouriteRecipeViewModel(private val databaseRecipeUtils: DatabaseRecipeUtils) : ViewModel() {
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

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun submitNewFavouriteList(adapter: AllRecipeRecyclerAdapter, profileId : Long) {
        coroutineScope.launch {
            val list = databaseRecipeUtils.getFavourites(profileId)
            val recipeList = mutableListOf<Recipe>()
            if (list != null) {
                for (favourite in list) {
                    recipeList.add(databaseRecipeUtils.getRecipe(favourite.recipeId))
                }
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(recipeList)
            }

        }
    }


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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}