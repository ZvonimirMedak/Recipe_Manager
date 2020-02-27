package com.example.recipemanager.createRecipe

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
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
import kotlinx.android.synthetic.main.error_popup.view.*
import kotlinx.coroutines.*

class CreateRecipeViewModel(private val activity: Activity, val rootLayout : View) : ViewModel() {

    private val _navigateToAllRecipes = MutableLiveData<Boolean?>()
    val navigateToAllRecipes: LiveData<Boolean?>
        get() = _navigateToAllRecipes


    lateinit var popupWindow: PopupWindow
    lateinit var popupView : View

    init {
        setupPopup()
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
    }
    fun navigateToAllRecipes() {
        _navigateToAllRecipes.value = true
    }

    fun navigationToAllRecipesDone() {
        _navigateToAllRecipes.value = null
    }


}