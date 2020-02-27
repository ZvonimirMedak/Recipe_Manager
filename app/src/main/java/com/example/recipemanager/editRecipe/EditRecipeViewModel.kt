package com.example.recipemanager.editRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import kotlinx.coroutines.*

class EditRecipeViewModel : ViewModel(){
    private val _navigateToDetailRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailRecipe : LiveData<Recipe?>
    get() = _navigateToDetailRecipe


    fun navigateToDetailRecipe(recipe: Recipe){
        _navigateToDetailRecipe.value = recipe
    }

    fun navigationToDetailRecipeDone(){
        _navigateToDetailRecipe.value = null
    }

}