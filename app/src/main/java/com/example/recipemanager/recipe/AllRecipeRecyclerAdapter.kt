package com.example.recipemanager.recipe


import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.*
import com.example.recipemanager.databinding.RecipeListItemBinding
import com.example.recipemanager.recommendedRecipe.RecommendedRecipeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllRecipeRecyclerAdapter(
    private val onClickListener: RecipeOnClickListener,private val recommendedRecipeViewModel: RecommendedRecipeViewModel, private val profile: Profile
) : ListAdapter<Recipe, AllRecipeRecyclerAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, profile, recommendedRecipeViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = getItem(position)
        holder.bind(currentRecipe, onClickListener)
    }

    class ViewHolder private constructor(val binding: RecipeListItemBinding,val recommendedRecipeViewModel: RecommendedRecipeViewModel, val profile: Profile) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recipe, onClickListener: RecipeOnClickListener) {
            binding.recipe = item
            if(!recommendedRecipeViewModel.checkCaffeine(item, profile) && !recommendedRecipeViewModel.checkFructose(item, profile) &&
                        !recommendedRecipeViewModel.checkGluten(item,profile) && !recommendedRecipeViewModel.checkLactose(item, profile)){
                binding.recipeTitle.setTextColor(Color.GREEN)
            }
            else{
                binding.recipeTitle.setTextColor(Color.RED)
            }

            binding.onClickListener = onClickListener
            Glide.with(binding.recipeImage).load(item.photoUrl).optionalCenterCrop()
                .into(binding.recipeImage)
            binding.recipeTitle.text = item.name
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, profile: Profile, recommendedRecipeViewModel: RecommendedRecipeViewModel): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding,recommendedRecipeViewModel, profile)

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