package com.example.recipemanager.utils

import android.app.Application
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.editRecipe.EditRecipeViewModel
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.ingredients.IngredientsViewModel
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class DatabaseIngredientsUtils(application: Application){
    val database = AppDatabase.getInstance(application)
    val ingredientDao = database.ingredientDao
    lateinit var deletePopup : PopupWindow
    lateinit var deletePopupView : View
    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    init {
        setupPopupWindow(application)
    }
    private fun getProfileIngredients(profileId: Long) =
        ingredientDao.getAllProfileIngredients(profileId)


    fun submitNewList(adapter: IngredientsRecyclerAdapter,profileId: Long) {
        coroutineScope.launch {
            val list = getProfileIngredients(profileId)
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }

    private fun getRecipeIngredients(recipeId: Long) =
        ingredientDao.getAllRecipeIngredients(recipeId)

    fun submitRecipeList(adapter: IngredientsRecyclerAdapter,recipeId: Long) {
        coroutineScope.launch {
            val list = getRecipeIngredients(recipeId)
            withContext(Dispatchers.Main) {
                adapter.submitList(list)
            }
        }
    }

    fun insertNewIngredient(adapter : IngredientsRecyclerAdapter,ingredient: Ingredient, profileId: Long) {
        coroutineScope.launch {
            ingredientDao.insertIngredient(ingredient)
            submitNewList(adapter, profileId)
        }
    }

    fun createRecipeIngredient(adapter: IngredientsRecyclerAdapter,ingredient: Ingredient, recipeId: Long = 0) {
        coroutineScope.launch {
            ingredientDao.insertIngredient(
                Ingredient(
                    ingredientText = ingredient.ingredientText,
                    recipeId = recipeId
                )
            )
            submitRecipeList(adapter,recipeId)
        }
    }

    fun deleteIngredient(ingredientId: Long, profileId: Long, adapter: IngredientsRecyclerAdapter, rootLayout: View) {
        deletePopup.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        deletePopupView.yes_button.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    ingredientDao.deleteIngredient(ingredientId)
                    submitNewList(adapter,profileId)
                }
            }

            deletePopup.dismiss()

        }
        deletePopupView.no_button.setOnClickListener {
            deletePopup.dismiss()
        }

    }

    fun deleteRecipeIngredient(ingredientId: Long, recipeId: Long = 0, adapter: IngredientsRecyclerAdapter,rootLayout : View) {
        deletePopup.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        deletePopupView.yes_button.setOnClickListener {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    ingredientDao.deleteIngredient(ingredientId)
                    submitRecipeList(adapter, recipeId)
                }
            }

            deletePopup.dismiss()

        }
        deletePopupView.no_button.setOnClickListener {
            deletePopup.dismiss()
        }
    }
    private fun setupPopupWindow(application: Application){
        val inflater = LayoutInflater.from(application)
        deletePopup = PopupWindow(application)
        deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        deletePopup.contentView = deletePopupView
        deletePopup.isFocusable = true
    }
}