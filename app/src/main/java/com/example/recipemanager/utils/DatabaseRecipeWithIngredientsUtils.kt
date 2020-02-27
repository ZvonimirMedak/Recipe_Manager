package com.example.recipemanager.utils

import android.app.Application
import android.util.Log
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Favourite
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.createRecipe.CreateRecipeViewModel
import com.example.recipemanager.detailRecipe.DetailRecipeViewModel
import kotlinx.coroutines.*

class DatabaseRecipeWithIngredientsUtils (application: Application){
    val database = AppDatabase.getInstance(application)
    private val recipeDao = database.recipeDao
    private val ingredientDao = database.ingredientDao
    private val favouriteDao = database.favouriteDao

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job )

    private fun checkExistence(recipeName: String): Boolean {
        val recipes = recipeDao.getAllrecipes()
        if (recipes != null) {
            for (r in recipes) {
                if (recipeName == r.name) {
                    return true
                }
            }
        }
        return false
    }

    fun insertRecipe(recipe: Recipe, viewModel : CreateRecipeViewModel) {

        coroutineScope.launch {
            if (!checkExistence(recipe.name)) {
                recipeDao.insertRecipe(recipe)
                var list = getIngredients(0)
                val foundRecipe = recipeDao.getNamedRecipe(recipe.name)
                createRecipeIngredientsWithDelete(foundRecipe!!, list!!)
                withContext(Dispatchers.Main) {
                    viewModel.navigateToAllRecipes()
                }
            }
        }
    }

    private fun deleteIngredients() {
        ingredientDao.deleteRecipeIngredient(0)
    }

    private fun getIngredients(recipeId: Long) = ingredientDao.getAllRecipeIngredients(recipeId)

    private fun createRecipeIngredientsWithDelete(recipe: Recipe, ingredients: List<Ingredient>) {
        for (ingredient in ingredients) {
            ingredientDao.insertIngredient(
                Ingredient(
                    recipeId = recipe.recipeId,
                    ingredientText = ingredient.ingredientText
                )
            )
        }
        deleteIngredients()

    }
    private fun getFavourites(profileId: Long) = favouriteDao.getAllProfileFavouirtes(profileId)

    fun checkFavourite(recipeId: Long, profileId: Long, viewModel : DetailRecipeViewModel) {
        coroutineScope.launch {
            val list = getFavourites(profileId)
            if (list != null) {
                for (favourite in list) {
                    if (favourite.recipeId == recipeId)
                        withContext(Dispatchers.Main) {
                            viewModel.favouriteChecker.value = true
                        }
                }
            }
        }

    }
    fun deleteRecipe(recipe: Recipe, profileId: Long, viewModel: DetailRecipeViewModel) {
        coroutineScope.launch {
            val favourite = favouriteDao.getFavouriteRecipe(profileId, recipe.recipeId)
            if (favourite != null) {
                favouriteDao.deleteFavourite(favourite.favouriteId)
            }
            recipeDao.deleteRecipe(recipe.recipeId)
            val list = getRecipeIngredients(recipe.recipeId)
            for (ingredient in list!!) {
                ingredientDao.deleteIngredient(ingredient.ingredientId)
            }
        }
        viewModel.navigateToAllRecipes()
    }

    private fun getRecipeIngredients(recipeId: Long) =
        ingredientDao.getAllRecipeIngredients(recipeId)

    fun createFavouriteRecipe(recipeId: Long, profileId: Long, viewModel: DetailRecipeViewModel) {
        coroutineScope.launch {
            if (viewModel.favouriteChecker.value == null) {
                favouriteDao.insertFavourite(Favourite(profileId = profileId, recipeId = recipeId))
                checkFavourite(recipeId, profileId, viewModel)
            } else {
                withContext(Dispatchers.Main) {
                    viewModel.favouriteChecker.value = null
                }
                val favourite = favouriteDao.getFavouriteRecipe(profileId, recipeId)
                favouriteDao.deleteFavourite(favourite!!.favouriteId)
            }

        }
    }
}