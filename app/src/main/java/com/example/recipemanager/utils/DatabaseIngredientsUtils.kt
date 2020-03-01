package com.example.recipemanager.utils

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.editRecipe.EditRecipeViewModel
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.ingredients.IngredientsViewModel
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class DatabaseIngredientsUtils(application: Application) : HideKeyboardUtil{
    private val database = AppDatabase.getInstance(application)
    private val ingredientDao = database.ingredientDao


    fun getProfileIngredients(profileId: Long) = ingredientDao.getAllProfileIngredients(profileId)

    fun getRecipeIngredients(recipeId: Long) = ingredientDao.getAllRecipeIngredients(recipeId)

    fun insertIngredient(ingredient: Ingredient) = ingredientDao.insertIngredient(ingredient)

    fun deleteIngredient(ingredientId : Long) = ingredientDao.deleteIngredient(ingredientId)

    fun deleteRecipeIngredients() = ingredientDao.deleteRecipeIngredient(0)


    override fun hideKeyboard(fragment: Fragment) {
        val imm =
            fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }
}