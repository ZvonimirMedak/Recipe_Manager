package com.example.recipemanager.favouriteRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.databinding.FavouriteRecipesBinding
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import com.example.recipemanager.recipe.RecipeOnClickListener
import com.example.recipemanager.recommendedRecipe.RecommendedRecipeViewModel
import com.example.recipemanager.utils.DatabaseRecipeUtils

class FavouriteRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FavouriteRecipesBinding =
            DataBindingUtil.inflate(inflater, R.layout.favourite_recipes, container, false)
        val profile = arguments!!.getParcelable<Profile>("profile")!!
        val application = requireNotNull(this.activity).application
        val databaseRecipeUtils = DatabaseRecipeUtils(application)
        val viewModel = FavouriteRecipeViewModel(databaseRecipeUtils)
        val adapter = AllRecipeRecyclerAdapter(RecipeOnClickListener {
            viewModel.navigateToDetailedRecipe(it)
        }, RecommendedRecipeViewModel(databaseRecipeUtils), profile)

        binding.recipeRecycler.adapter = adapter
        binding.recipeRecycler.layoutManager = LinearLayoutManager(context)
        viewModel.submitNewFavouriteList(adapter, profile.profileId)
        setupOnClickListeners(binding, viewModel)
        setupNavigationObservers(viewModel, profile)
        return binding.root
    }

    private fun setupOnClickListeners(binding: FavouriteRecipesBinding, viewModel: FavouriteRecipeViewModel){
        binding.allRecipesButton.setOnClickListener {
            viewModel.navigateToAllRecipes()
        }
        binding.myIngredientsButton.setOnClickListener {
            viewModel.navigateToMyIngredients()
        }
        binding.recommendedRecipesButton.setOnClickListener {
            viewModel.navigateToRecommendedRecipes()
        }
    }

    private fun setupNavigationObservers(viewModel: FavouriteRecipeViewModel, profile: Profile){
        viewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    FavouriteRecipeFragmentDirections.actionFavouriteRecipeFragmentToAllRecipesFragment2(
                        profile
                    )
                )
                viewModel.navigationToAllRecipesDone()
            }
        })
        viewModel.navigateToDetailRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    FavouriteRecipeFragmentDirections.actionFavouriteRecipeFragmentToDetailRecipeFragment(
                        it,
                        profile
                    )
                )
                viewModel.navigationToDetailedRecipeDone()
            }
        })
        viewModel.navigateToMyIngredients.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    FavouriteRecipeFragmentDirections.actionFavouriteRecipeFragmentToIngredientsFragment(
                        profile
                    )
                )
                viewModel.navigationToMyIngredientsDone()
            }
        })
        viewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    FavouriteRecipeFragmentDirections.actionFavouriteRecipeFragmentToRecommendedRecipe2(
                        profile
                    )
                )
                viewModel.navigationToRecommendedRecipesDone()
            }
        })
    }
}