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
    val onClickListener: RecipeOnClickListener,
    val databaseDao: RecipeDao,
    val profileDao: ProfileDao,
    val profileId: Long,
    val favouriteDao: FavouriteDao
) : ListAdapter<DataItemRecipe, AllRecipeRecyclerAdapter.ViewHolder>(RecipeDiffCallback()) {

    private fun getRecipes(): List<Recipe>? = databaseDao.getAllrecipes()
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun submitNewList() {
        adapterScope.launch {
            val list = getRecipes()
            Log.d("msg", list.toString())
            val items = list?.map { DataItemRecipe.RecipeItem(it) }
            Log.d("msg", items.toString())
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun checkLactose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.lactose || profile.lactose_intolerance) {
            return recipe.lactose == profile.lactose_intolerance
        }
        return false
    }

    private fun checkCaffeine(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.caffeine || profile.caffeine_intolerance) {
            return recipe.caffeine == profile.caffeine_intolerance
        }
        return false
    }

    private fun checkGluten(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.gluten || profile.gluten_intolerance) {
            return recipe.gluten == profile.gluten_intolerance
        }
        return false
    }

    private fun checkFructose(recipe: Recipe, profile: Profile): Boolean {
        if (recipe.fructose || profile.fructose_intolerance) {
            return recipe.fructose == profile.fructose_intolerance
        }
        return false
    }

    fun submitNewFavouriteList() {
        adapterScope.launch {
            val list = getFavourites(profileId, favouriteDao)
            val recipeList = mutableListOf<Recipe>()
            if (list != null) {
                for (favourite in list) {
                    recipeList.add(databaseDao.getRecipe(favourite.recipeId))
                }
            }
            val items = recipeList?.map { DataItemRecipe.RecipeItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }

        }
    }

    private fun getFavourites(profileId: Long, favoriteDao: FavouriteDao) =
        favoriteDao.getAllProfileFavouirtes(profileId)

    fun submitNewRecommendedList() {
        adapterScope.launch {
            val recipes = getRecipes()
            val profile = profileDao.getProfile(profileId)
            val recommendedRecipes = mutableListOf<Recipe>()
            Log.d("msg123", recipes.toString())
            for (recipe in recipes!!) {
                if (!checkCaffeine(recipe, profile!!) && !checkFructose(
                        recipe,
                        profile!!
                    ) && !checkGluten(recipe, profile!!) && !checkLactose(recipe, profile!!)
                ) {
                    recommendedRecipes.add(recipe)
                }
            }
            val items = recommendedRecipes.map { DataItemRecipe.RecipeItem(it) }
            Log.d("msg123", recommendedRecipes.toString())
            withContext(Dispatchers.Main) {
                submitList(items)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return AllRecipeRecyclerAdapter.ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AllRecipeRecyclerAdapter.ViewHolder, position: Int) {
        val currentRecipe = getItem(position) as DataItemRecipe.RecipeItem
        holder.bind(currentRecipe.recipe, onClickListener)
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

class RecipeDiffCallback : DiffUtil.ItemCallback<DataItemRecipe>() {
    override fun areItemsTheSame(oldItem: DataItemRecipe, newItem: DataItemRecipe): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItemRecipe, newItem: DataItemRecipe): Boolean {
        return oldItem.recipeId == newItem.recipeId
    }
}

class RecipeOnClickListener(val onClickListener: (recipe: Recipe) -> Unit) {
    fun onClick(recipe: Recipe) = onClickListener(recipe)
}


sealed class DataItemRecipe {
    data class RecipeItem(val recipe: Recipe) : DataItemRecipe() {
        override val recipeId = recipe.recipeId

    }

    abstract val recipeId: Long
}