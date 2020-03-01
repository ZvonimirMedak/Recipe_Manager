package com.example.recipemanager.createRecipe

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.IngredientDao
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.appDatabase.RecipeDao
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch

class CreateRecipeViewModel(private val activity: Activity, private val databaseIngredientsUtils: DatabaseIngredientsUtils, private val databaseRecipeUtils: DatabaseRecipeUtils) : ViewModel() {

    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes: LiveData<Boolean?>
        get() = _navigateToAllRecipes


    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View
    private lateinit var deletePopup : PopupWindow
    private lateinit var deletePopupView : View
    init {
        setupPopup()
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun submitRecipeList(adapter: IngredientsRecyclerAdapter, recipeId: Long) {
        coroutineScope.launch {
            val list = databaseIngredientsUtils.getRecipeIngredients(recipeId)
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }
    fun createRecipeIngredient(adapter: IngredientsRecyclerAdapter,ingredient: Ingredient, recipeId: Long = 0) {
        coroutineScope.launch {
            databaseIngredientsUtils.insertIngredient(
                Ingredient(
                    ingredientText = ingredient.ingredientText,
                    recipeId = recipeId
                )
            )
            submitRecipeList(adapter,recipeId)
        }
    }
    fun deleteRecipeIngredient(ingredientId: Long, recipeId: Long = 0, adapter: IngredientsRecyclerAdapter,rootLayout : View) {
        deletePopup.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        deletePopupView.yes_button.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    databaseIngredientsUtils.deleteIngredient(ingredientId)
                    submitRecipeList(adapter, recipeId)
                }
            }

            deletePopup.dismiss()

        }
        deletePopupView.no_button.setOnClickListener {
            deletePopup.dismiss()
        }
    }

    private fun setupPopup(){
        val inflater = LayoutInflater.from(activity)
        popupWindow = PopupWindow(activity)
        popupView = inflater.inflate(R.layout.error_popup, null)
        popupWindow.isFocusable = true
        popupWindow.contentView = popupView
        popupView.ok_button.setOnClickListener {
            popupWindow.dismiss()
        }
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
        deletePopup = PopupWindow(activity)
        deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        deletePopup.contentView = deletePopupView
        deletePopup.isFocusable = true
        deletePopup.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
    }
    fun insertRecipe(recipe: Recipe, viewModel : CreateRecipeViewModel, rootLayout: View) {

        coroutineScope.launch {
            if (!checkExistence(recipe.name)) {
                databaseRecipeUtils.insertRecipe(recipe)
                var list = databaseIngredientsUtils.getRecipeIngredients(0)
                val foundRecipe = databaseRecipeUtils.getRecipeByName(recipe.name)
                createRecipeIngredientsWithDelete(foundRecipe!!, list!!)
                withContext(Dispatchers.Main) {
                    viewModel.navigateToAllRecipes()
                }
            }
            else{
                withContext(Dispatchers.Main){
                    viewModel.popupView.error_text.text = "Recipe with that name already exists or the name is empty"
                    viewModel.popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0,0)
                }
            }
        }
    }
    private fun createRecipeIngredientsWithDelete(recipe: Recipe, ingredients: List<Ingredient>) {
        for (ingredient in ingredients) {
            databaseIngredientsUtils.insertIngredient(
                Ingredient(
                    recipeId = recipe.recipeId,
                    ingredientText = ingredient.ingredientText
                )
            )
        }
        databaseIngredientsUtils.deleteRecipeIngredients()

    }

    private fun checkExistence(recipeName: String): Boolean {
        if(recipeName == "") {
            return true
        }
        val recipes = databaseRecipeUtils.getRecipes()
        if (recipes != null) {
            for (r in recipes) {
                if (recipeName == r.name) {
                    return true
                }
            }
        }
        return false
    }
    fun navigateToAllRecipes() {
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigateToAllRecipes.value = null
    }


}