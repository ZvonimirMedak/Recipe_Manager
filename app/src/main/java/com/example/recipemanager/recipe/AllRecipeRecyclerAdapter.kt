package com.example.recipemanager.recipe


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemanager.appDatabase.*
import com.example.recipemanager.databinding.RecipeListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllRecipeRecyclerAdapter(
    private val onClickListener: RecipeOnClickListener
) : ListAdapter<Recipe, AllRecipeRecyclerAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = getItem(position)
        holder.bind(currentRecipe, onClickListener)
    }

    class ViewHolder private constructor(val binding: RecipeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recipe, onClickListener: RecipeOnClickListener) {
            binding.recipe = item
            binding.onClickListener = onClickListener
            Glide.with(binding.recipeImage).load(item.photoUrl).optionalCenterCrop()
                .into(binding.recipeImage)
            binding.recipeTitle.text = item.name
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)

            }
        }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }
}

class RecipeOnClickListener(val onClickListener: (recipe: Recipe) -> Unit) {
    fun onClick(recipe: Recipe) = onClickListener(recipe)
}