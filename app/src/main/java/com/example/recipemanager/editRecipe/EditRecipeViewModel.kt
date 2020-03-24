package com.example.recipemanager.editRecipe

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import com.example.recipemanager.createRecipe.CreateRecipeViewModel
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.coroutines.*

class EditRecipeViewModel(private val databaseRecipeUtils: DatabaseRecipeUtils, private val currentRecipe: Recipe, private val view: View, private val createRecipeViewModel: CreateRecipeViewModel) : ViewModel(){
    private val _navigateToDetailRecipe = MutableLiveData<Recipe?>()
    val navigateToDetailRecipe : LiveData<Recipe?>
    get() = _navigateToDetailRecipe


    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)


    fun updateRecipe(recipe : Recipe, profile: Profile){
        if(recipe.creator == currentRecipe.creator && recipe.name == currentRecipe.name){
            coroutineScope.launch {
                databaseRecipeUtils.updateRecipe(recipe.name, recipe.photoUrl, timeToMake = recipe.timeToMake, typeOfMeal = recipe.typeOfMeal,
                    description = recipe.description, gluten = recipe.gluten, fructose = recipe.fructose, lactose = recipe.lactose, caffeine = recipe.caffeine
                    ,recipeId = recipe.recipeId)
                withContext(Dispatchers.Main){
                    navigateToDetailRecipe(recipe)
                }
            }
        }
        else{
                    createRecipeViewModel.insertRecipe(Recipe(
                        name = recipe.name,
                        description = recipe.description,
                        timeToMake = recipe.timeToMake,
                        typeOfMeal = recipe.typeOfMeal,
                        gluten = recipe.gluten,
                        fructose = recipe.fructose,
                        lactose = recipe.lactose,
                        caffeine = recipe.caffeine,
                        profileId = profile.profileId,
                        creator = recipe.creator
                    ), view)

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