package com.example.recipemanager.ingredients

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanager.appDatabase.*
import com.example.recipemanager.databinding.IngredientListItemBinding
import com.example.recipemanager.databinding.ProfileListItemBinding
import com.example.recipemanager.profiles.ProfileDiffCallback
import com.example.recipemanager.profiles.ProfileOnClickListener
import com.example.recipemanager.profiles.ProfileRecyclerAdapter
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.*

class IngredientsRecyclerAdapter(
    val onClickListener: IngredientOnClickListener
) : ListAdapter<Ingredient, IngredientsRecyclerAdapter.ViewHolder>(IngredientsDiffCallback()) {
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