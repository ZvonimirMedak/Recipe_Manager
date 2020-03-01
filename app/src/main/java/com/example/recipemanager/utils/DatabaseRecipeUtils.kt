package com.example.recipemanager.utils

import android.app.Application
import com.example.recipemanager.appDatabase.*
import com.example.recipemanager.detailRecipe.DetailRecipeViewModel
import com.example.recipemanager.editRecipe.EditRecipeViewModel
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import kotlinx.coroutines.*

class DatabaseRecipeUtils (application: Application){
    private val database = AppDatabase.getInstance(application)
    private val favouriteDao = database.favouriteDao
    private val recipeDao = database.recipeDao
    private val profileDao = database.profileDao

    fun getRecipes(): List<Recipe>? = recipeDao.getAllrecipes()

    fun getProfile(profileId: Long) = profileDao.getProfile(profileId)

    fun getFavourites(profileId: Long) = favouriteDao.getAllProfileFavouirtes(profileId)

    fun getRecipe(recipeId : Long) = recipeDao.getRecipe(recipeId)

    fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)

    fun getRecipeByName(recipeName: String) = recipeDao.getNamedRecipe(recipeName)

    fun getFavouriteRecipe(profileId: Long, recipeId: Long) = favouriteDao.getFavouriteRecipe(profileId, recipeId)

    fun deleteFavourite(favouriteId: Long) = favouriteDao.deleteFavourite(favouriteId)

    fun deleteRecipe(recipeId: Long) = recipeDao.deleteRecipe(recipeId)

    fun insertFavourite(favourite: Favourite) = favouriteDao.insertFavourite(favourite)

    fun updateRecipe(name : String, photoUrl : String, timeToMake : String, typeOfMeal: String, description : String,
                     gluten : Boolean, fructose : Boolean, lactose : Boolean, caffeine : Boolean, recipeId: Long) =
        recipeDao.updateRecipe(name, photoUrl, timeToMake, typeOfMeal, description, gluten, fructose, lactose, caffeine, recipeId)


}