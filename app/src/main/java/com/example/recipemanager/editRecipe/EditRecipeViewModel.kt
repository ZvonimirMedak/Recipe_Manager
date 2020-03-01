package com.example.recipemanager.editRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.coroutines.*

class EditRecipeViewModel(private val databaseRecipeUtils: DatabaseRecipeUtils) : ViewModel(){
    private val _navigateToDetailRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailRecipe : LiveData<Recipe?>
    get() = _navigateToDetailRecipe

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun updateRecipe(recipe : Recipe){
        coroutineScope.launch {
            databaseRecipeUtils.updateRecipe(recipe.name, recipe.photoUrl, timeToMake = recipe.timeToMake, typeOfMeal = recipe.typeOfMeal,
                description = recipe.description, gluten = recipe.gluten, fructose = recipe.fructose, lactose = recipe.lactose, caffeine = recipe.caffeine
                ,recipeId = recipe.recipeId)
            withContext(Dispatchers.Main){
                navigateToDetailRecipe(recipe)
            }
        }

    }
    private fun navigateToDetailRecipe(recipe: Recipe){
        _navigateToDetailRecipe.value = recipe
    }

    fun navigationToDetailRecipeDone(){
        _navigateToDetailRecipe.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}