package com.example.recipemanager.ingredients


import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanager.MainActivity
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.*
import com.example.recipemanager.databinding.IngredientListItemBinding
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class IngredientsRecyclerAdapter(
    private val onClickListener: IngredientOnClickListener,private val databaseIngredientsUtils: DatabaseIngredientsUtils, private val activity: Activity
) : ListAdapter<Ingredient, IngredientsRecyclerAdapter.ViewHolder>(IngredientsDiffCallback()) {
    private lateinit var deletePopup : PopupWindow
    private lateinit var deletePopupView : View
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    init {
        setupPopupWindow()
    }

    private fun setupPopupWindow() {
        val inflater = LayoutInflater.from(activity)
        deletePopup = PopupWindow(activity)
        deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        deletePopup.contentView = deletePopupView
        deletePopup.isFocusable = true
        deletePopup.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
    }

    fun submitNewList(profileId: Long) {
        coroutineScope.launch {
            val list = databaseIngredientsUtils.getProfileIngredients(profileId)
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
    fun insertNewIngredient(ingredient: Ingredient, profileId: Long) {
        coroutineScope.launch {
            databaseIngredientsUtils.insertIngredient(ingredient)
            submitNewList(profileId)
        }
    }
    fun deleteIngredient(ingredientId: Long, profileId: Long,rootLayout: View) {
        deletePopup.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        deletePopupView.yes_button.setOnClickListener {
            coroutineScope.launch {
                databaseIngredientsUtils.deleteIngredient(ingredientId)
                submitNewList(profileId)

            }

            deletePopup.dismiss()

        }
        deletePopupView.no_button.setOnClickListener {
            deletePopup.dismiss()
        }
    }
    fun deleteRecipeIngredient(ingredientId: Long, recipeId: Long,rootLayout: View) {
        deletePopup.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        deletePopupView.yes_button.setOnClickListener {
            coroutineScope.launch {
                databaseIngredientsUtils.deleteIngredient(ingredientId)
                submitRecipeList(recipeId)

            }

            deletePopup.dismiss()

        }
        deletePopupView.no_button.setOnClickListener {
            deletePopup.dismiss()
        }
    }

    fun submitRecipeList(recipeId : Long){
        coroutineScope.launch {
            val list = databaseIngredientsUtils.getRecipeIngredients(recipeId)
            withContext(Dispatchers.Main){
                submitList(list)
            }
        }
    }
    fun createRecipeIngredient(ingredient : Ingredient, recipeId: Long){
        coroutineScope.launch {
            databaseIngredientsUtils.insertIngredient(ingredient)
            val list = databaseIngredientsUtils.getRecipeIngredients(recipeId)
            withContext(Dispatchers.Main){
                submitList(list)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bind(ingredient, onClickListener)
    }

    class ViewHolder private constructor(val binding: IngredientListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ingredient, onClickListener: IngredientOnClickListener) {
            binding.ingredient = item
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)

            }
        }
    }


}

class IngredientsDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredientId == newItem.ingredientId
    }
}

class IngredientOnClickListener(val onClickListener: (ingredientId: Long) -> Unit) {
    fun onClick(ingredient: Ingredient) = onClickListener(ingredient.ingredientId)
}