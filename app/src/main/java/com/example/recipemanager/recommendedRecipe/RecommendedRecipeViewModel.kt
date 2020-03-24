package com.example.recipemanager.recommendedRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.coroutines.*

class RecommendedRecipeViewModel(private val databaseRecipeUtils: DatabaseRecipeUtils) : ViewModel() {
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

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun submitNewRecommendedList(adapter: AllRecipeRecyclerAdapter, profileId: Long) {
        coroutineScope.launch {
            val recipes = databaseRecipeUtils.getRecipes()
            val profile = databaseRecipeUtils.getProfile(profileId)
            val recommendedRecipes = mutableListOf<Recipe>()
            for (recipe in recipes!!) {
                if (!checkCaffeine(recipe, profile!!) && !checkFructose(
                        recipe,
                        profile
                    ) && !checkGluten(recipe, profile) && !checkLactose(recipe, profile)
                ) {
                    recommendedRecipes.add(recipe)
                }
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(recommendedRecipes)
            }

        }
    }

    fun checkLactose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.lactose) {
            return recipe.lactose == profile.lactose_intolerance
        }
        return false
    }

    fun checkCaffeine(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.caffeine) {
            return recipe.caffeine == profile.caffeine_intolerance
        }
        return false
    }

    fun checkGluten(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.gluten) {
            return recipe.gluten == profile.gluten_intolerance
        }
        return false
    }

    fun checkFructose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.fructose) {
            return recipe.fructose == profile.fructose_intolerance
        }
        return false
    }

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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}