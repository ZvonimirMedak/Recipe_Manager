package com.example.recipemanager.recipe

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
import com.example.recipemanager.databinding.AllRecipesBinding
import com.example.recipemanager.recommendedRecipe.RecommendedRecipeViewModel
import com.example.recipemanager.utils.DatabaseRecipeUtils

class AllRecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AllRecipesBinding =
            DataBindingUtil.inflate(inflater, R.layout.all_recipes, container, false)
        val application = requireNotNull(this.activity).application
        val databaseRecipeUtils = DatabaseRecipeUtils(application)
        val profile = arguments!!.getParcelable<Profile>("profile")!!
        val viewModel = AllRecipesViewModel(databaseRecipeUtils)
        val adapter = AllRecipeRecyclerAdapter(RecipeOnClickListener {
            viewModel.navigateToDetailedRecipe(it)
        }, RecommendedRecipeViewModel(databaseRecipeUtils), profile)
        binding.viewModel = viewModel
        binding.recipeRecycler.adapter = adapter
        binding.recipeRecycler.layoutManager = LinearLayoutManager(context)
        viewModel.submitNewList(adapter)
        setupOnClickListeners(binding, viewModel, profile)
        setupNavigationObservers(viewModel, profile)
        return binding.root
    }


    private fun setupOnClickListeners(binding: AllRecipesBinding, viewModel: AllRecipesViewModel, profile: Profile){
        binding.recommendedRecipesButton.setOnClickListener {
            viewModel.navigateToRecommendedRecipes()
        }

        binding.myIngredientsButton.setOnClickListener {
            viewModel.navigateToMyIngredients()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            viewModel.navigateToFavouriteRecipes()
        }
        binding.fabAllRecipes.setOnClickListener {
            this.findNavController().navigate(
                AllRecipesFragmentDirections.actionAllRecipesFragment2ToCreateRecipeFragment(
                    profile
                )
            )
        }
    }


    private fun setupNavigationObservers(viewModel: AllRecipesViewModel, profile: Profile){
        viewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToFavouriteRecipeFragment(
                        profile
                    )
                )
                viewModel.navigationToFavouriteRecipesDone()
            }
        })
        viewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToRecommendedRecipe2(
                        profile
                    )
                )
                viewModel.navigationToRecommendedRecipesDone()
            }

        })
        viewModel.navigateToMyIngredients.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToIngredientsFragment(
                        profile
                    )
                )
                viewModel.navigationToMyIngredientsDone()
            }
        })

        viewModel.navigateToDetailedRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToDetailRecipeFragment(
                        it,
                        profile
                    )
                )
                viewModel.navigationToDetailedRecipeDone()
            }
        })
    }
}