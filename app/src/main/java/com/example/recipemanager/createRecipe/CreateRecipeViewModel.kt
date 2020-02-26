package com.example.recipemanager.createRecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.IngredientDao
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import kotlinx.coroutines.*

class CreateRecipeViewModel(private val recipeDao : RecipeDao, private val ingredientDao: IngredientDao, private val profileId : Long) : ViewModel(){


    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes : LiveData<Boolean?>
    get() = _navigateToAllRecipes

    private fun checkExistence(recipeName : String) : Boolean{
        val recipes = recipeDao.getAllrecipes()
        if(recipes != null){
            for (r in recipes){
                if(recipeName == r.name){
                    return true
                }
            }
        }
        return false
    }

    fun insertRecipe(recipe : Recipe){

            coroutineScope.launch {
                if (!checkExistence(recipe.name)) {
                recipeDao.insertRecipe(recipe)
                var list = getIngredients(0)
                Log.d("msgrecept", recipe.recipeId.toString())
                val foundRecipe = recipeDao.getNamedRecipe(recipe.name)
                Log.d("foundrec", foundRecipe!!.recipeId.toString())
                createRecipeIngredients(foundRecipe!!, list!!)
                withContext(Dispatchers.Main){
                    navigateToAllRecipes()
                }
                }
        }
    }

    private fun deleteIngredients() {
                    ingredientDao.deleteRecipeIngredient(0)
    }

    private fun getIngredients(recipeId : Long)= ingredientDao.getAllRecipeIngredients(recipeId)

    private fun createRecipeIngredients(recipe: Recipe, ingredients: List<Ingredient>){
            for (ingredient in ingredients){
                Log.d("msg", recipe.recipeId.toString())
                ingredientDao.insertIngredient(Ingredient(recipeId = recipe.recipeId, ingredientText = ingredient.ingredientText))
            }
            deleteIngredients()

    }

    private fun navigateToAllRecipes(){
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone(){
        _navigateToAllRecipes.value = null
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}