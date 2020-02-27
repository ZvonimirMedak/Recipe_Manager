package com.example.recipemanager.utils

import android.app.Application
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.FavouriteDao
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.detailRecipe.DetailRecipeViewModel
import com.example.recipemanager.editRecipe.EditRecipeViewModel
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import kotlinx.coroutines.*

class DatabaseRecipeUtils (application: Application){
    private val database = AppDatabase.getInstance(application)
    private val favouriteDao = database.favouriteDao
    private val recipeDao = database.recipeDao
    private val profileDao = database.profileDao
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)




    private fun getRecipes(): List<Recipe>? = recipeDao.getAllrecipes()

    fun submitNewList(adapter: AllRecipeRecyclerAdapter) {
        coroutineScope.launch {
            val list = getRecipes()
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }

    fun submitNewFavouriteList(adapter: AllRecipeRecyclerAdapter, profileId : Long) {
        coroutineScope.launch {
            val list = getFavourites(profileId, favouriteDao)
            val recipeList = mutableListOf<Recipe>()
            if (list != null) {
                for (favourite in list) {
                    recipeList.add(recipeDao.getRecipe(favourite.recipeId))
                }
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(recipeList)
            }

        }
    }

    private fun getFavourites(profileId: Long, favoriteDao: FavouriteDao) =
        favoriteDao.getAllProfileFavouirtes(profileId)

    fun submitNewRecommendedList(adapter: AllRecipeRecyclerAdapter, profileId: Long) {
        coroutineScope.launch {
            val recipes = getRecipes()
            val profile = profileDao.getProfile(profileId)
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

    private fun checkLactose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.lactose || profile.lactose_intolerance) {
            return recipe.lactose == profile.lactose_intolerance
        }
        return false
    }

    private fun checkCaffeine(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.caffeine || profile.caffeine_intolerance) {
            return recipe.caffeine == profile.caffeine_intolerance
        }
        return false
    }

    private fun checkGluten(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.gluten || profile.gluten_intolerance) {
            return recipe.gluten == profile.gluten_intolerance
        }
        return false
    }

    private fun checkFructose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.fructose || profile.fructose_intolerance) {
            return recipe.fructose == profile.fructose_intolerance
        }
        return false
    }

    fun updateRecipe(recipe : Recipe, viewModel: EditRecipeViewModel){
        coroutineScope.launch {
            recipeDao.updateRecipe(recipe.name, recipe.photoUrl, timeToMake = recipe.timeToMake, typeOfMeal = recipe.typeOfMeal,
                description = recipe.description, gluten = recipe.gluten, fructose = recipe.fructose, lactose = recipe.lactose, caffeine = recipe.caffeine
                ,recipeId = recipe.recipeId)
            withContext(Dispatchers.Main){
                viewModel.navigateToDetailRecipe(recipe)
            }
        }

    }



}