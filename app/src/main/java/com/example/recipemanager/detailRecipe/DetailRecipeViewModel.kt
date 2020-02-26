package com.example.recipemanager.detailRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.appDatabase.*
import kotlinx.coroutines.*

class DetailRecipeViewModel(
    val recipeDao: RecipeDao,
    val ingredientDao: IngredientDao,
    val favouriteDao: FavouriteDao
) : ViewModel() {
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    val favouriteChecker = MutableLiveData<Boolean?>()

    private val _navigationToAllRecipes = MutableLiveData<Boolean?>()
    val navigationToAllRecipes: LiveData<Boolean?>
        get() = _navigationToAllRecipes

    private fun navigationToAllRecipesWanted() {
        _navigationToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigationToAllRecipes.value = null
    }

    fun deleteRecipe(recipe: Recipe, profileId: Long) {
        coroutineScope.launch {
            val favourite = favouriteDao.getFavouriteRecipe(profileId, recipe.recipeId)
            if(favourite != null){
                favouriteDao.deleteFavourite(favourite.favouriteId)
            }
            recipeDao.deleteRecipe(recipe.recipeId)
            val list = getRecipeIngredients(recipe.recipeId)
            for (ingredient in list!!) {
                ingredientDao.deleteIngredient(ingredient.ingredientId)
            }
        }
        navigationToAllRecipesWanted()
    }

    private fun getRecipeIngredients(recipeId: Long) =
        ingredientDao.getAllRecipeIngredients(recipeId)

    fun createFavouriteRecipe(recipeId: Long, profileId: Long) {
        coroutineScope.launch {
            if(favouriteChecker.value == null){
                favouriteDao.insertFavourite(Favourite(profileId = profileId, recipeId = recipeId))
                checkFavourite(recipeId, profileId)
            }
            else{
                withContext(Dispatchers.Main){
                    favouriteChecker.value = null
                }
                val favourite = favouriteDao.getFavouriteRecipe(profileId, recipeId)
                favouriteDao.deleteFavourite(favourite!!.favouriteId)
            }

        }
    }



    fun checkFavourite(recipeId: Long, profileId: Long) {
        coroutineScope.launch {
            val list = getFavourites(profileId)
            if (list != null) {
                for (favourite in list) {
                    if (favourite.recipeId == recipeId)
                        withContext(Dispatchers.Main) {
                            favouriteChecker.value = true
                        }
                }
            }
        }

    }

    private fun getFavourites(profileId: Long) = favouriteDao.getAllProfileFavouirtes(profileId)
    override fun onCleared() {
        super.onCleared()
        job.complete()
    }
}