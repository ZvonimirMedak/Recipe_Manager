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
import com.example.recipemanager.databinding.AllRecipesBinding
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
        val profileId = arguments!!.getLong("profileId")
        val viewModel = AllRecipesViewModel(databaseRecipeUtils)
        val adapter = AllRecipeRecyclerAdapter(RecipeOnClickListener {
            viewModel.navigateToDetailedRecipe(it)
        })
        binding.viewModel = viewModel
        binding.recipeRecycler.adapter = adapter
        binding.recipeRecycler.layoutManager = LinearLayoutManager(context)
        viewModel.submitNewList(adapter)
        setupOnClickListeners(binding, viewModel, profileId)
        setupNavigationObservers(viewModel, profileId)
        return binding.root
    }


    private fun setupOnClickListeners(binding: AllRecipesBinding, viewModel: AllRecipesViewModel, profileId : Long){
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
                    profileId
                )
            )
        }
    }


    private fun setupNavigationObservers(viewModel: AllRecipesViewModel, profileId: Long){
        viewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToFavouriteRecipeFragment(
                        profileId
                    )
                )
                viewModel.navigationToFavouriteRecipesDone()
            }
        })
        viewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToRecommendedRecipe2(
                        arguments!!.getLong("profileId", 0)
                    )
                )
                viewModel.navigationToRecommendedRecipesDone()
            }

        })
        viewModel.navigateToMyIngredients.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToIngredientsFragment(
                        profileId
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
                        profileId
                    )
                )
                viewModel.navigationToDetailedRecipeDone()
            }
        })
    }
}