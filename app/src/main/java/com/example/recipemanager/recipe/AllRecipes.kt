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

class AllRecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AllRecipesBinding =
            DataBindingUtil.inflate(inflater, R.layout.all_recipes, container, false)
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val profileId = arguments!!.getLong("profileId", 0)
        val recipeDao = database.recipeDao
        val recipeViewModel = AllRecipesViewModel()
        val profileDao = database.profileDao
        val favouriteDao = database.favouriteDao
        val adapter = AllRecipeRecyclerAdapter(RecipeOnClickListener {
            recipeViewModel.navigateToDetailedRecipe(it)
        }, recipeDao, profileDao, profileId, favouriteDao)
        binding.viewModel = recipeViewModel
        binding.recommendedRecipesButton.setOnClickListener {
            recipeViewModel.navigateToRecommendedRecipes()
        }

        binding.myIngredientsButton.setOnClickListener {
            recipeViewModel.navigateToMyIngredients()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            recipeViewModel.navigateToFavouriteRecipes()
        }
        recipeViewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToFavouriteRecipeFragment(
                        profileId
                    )
                )
                recipeViewModel.navigationToFavouriteRecipesDone()
            }
        })
        recipeViewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToRecommendedRecipe2(
                        arguments!!.getLong("profileId", 0)
                    )
                )
                recipeViewModel.navigationToRecommendedRecipesDone()
            }

        })

        recipeViewModel.navigateToMyIngredients.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToIngredientsFragment(
                        profileId
                    )
                )
                recipeViewModel.navigationToMyIngredientsDone()
            }
        })
        adapter.submitNewList()
        binding.recipeRecycler.adapter = adapter
        binding.recipeRecycler.layoutManager = LinearLayoutManager(context)
        binding.fabAllRecipes.setOnClickListener {
            this.findNavController().navigate(
                AllRecipesFragmentDirections.actionAllRecipesFragment2ToCreateRecipeFragment(
                    profileId
                )
            )
        }

        recipeViewModel.navigateToDetailedRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    AllRecipesFragmentDirections.actionAllRecipesFragment2ToDetailRecipeFragment(
                        it,
                        profileId
                    )
                )
                recipeViewModel.navigationToDetailedRecipeDone()
            }
        })
        return binding.root
    }
}