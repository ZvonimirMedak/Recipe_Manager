package com.example.recipemanager.editRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import kotlinx.coroutines.*

class EditRecipeViewModel(val recipeDao: RecipeDao) : ViewModel(){
    private val _navigateToDetailRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailRecipe : LiveData<Recipe?>
    get() = _navigateToDetailRecipe

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private fun navigateToDetailRecipe(recipe: Recipe){
        _navigateToDetailRecipe.value = recipe
    }

    fun navigationToDetailRecipeDone(){
        _navigateToDetailRecipe.value = null
    }

    fun updateRecipe(recipe : Recipe){
        coroutineScope.launch {
            recipeDao.updateRecipe(recipe.name, recipe.photoUrl, timeToMake = recipe.timeToMake, typeOfMeal = recipe.typeOfMeal,
                description = recipe.description, gluten = recipe.gluten, fructose = recipe.fructose, lactose = recipe.lactose, caffeine = recipe.caffeine
            ,recipeId = recipe.recipeId)
            withContext(Dispatchers.Main){
                navigateToDetailRecipe(recipe)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}