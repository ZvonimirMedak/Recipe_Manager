package com.example.recipemanager.recipe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.databinding.RecommendedRecipesBinding
import kotlinx.android.synthetic.main.recommended_recipes.view.*

class RecommendedRecipe : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RecommendedRecipesBinding =
            DataBindingUtil.inflate(inflater, R.layout.recommended_recipes, container, false)
        val profileId = arguments!!.getLong("profileId", 0)
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val recipeDao = database.recipeDao
        val profileDao = database.profileDao
        val favouriteDao = database.favouriteDao
        val recipeViewModel = RecommendedRecipeViewModel()
        val adapter = AllRecipeRecyclerAdapter(RecipeOnClickListener {
            recipeViewModel.navigateToDetailedRecipe(it)
        }, recipeDao, profileDao, profileId, favouriteDao)
        binding.viewModel = recipeViewModel


        binding.recommendedRecipes.adapter = adapter
        binding.recommendedRecipes.layoutManager = LinearLayoutManager(context)
        adapter.submitNewRecommendedList()
        binding.allRecipesButton.setOnClickListener {
            recipeViewModel.navigateToAllRecipes()
        }
        binding.myIngredientsButton.setOnClickListener {
            recipeViewModel.navigateToMyIngredients()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            recipeViewModel.navigateToFavouriteRecipes()
        }
        recipeViewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToAllRecipesFragment2(
                        profileId
                    )
                )
                recipeViewModel.navigationToAllRecipesDone()
            }
        })
        recipeViewModel.navigateToDetailedRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToDetailRecipeFragment(
                        it,
                        profileId
                    )
                )
                recipeViewModel.navigationToDetailedRecipeDone()
            }
        })
        recipeViewModel.navigateToMyIngredients.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToIngredientsFragment(
                        profileId
                    )
                )
                recipeViewModel.navigationToMyIngredientsDone()
            }
        })
        recipeViewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToFavouriteRecipeFragment(
                        profileId
                    )
                )
                recipeViewModel.navigationToFavouriteRecipesDone()
            }
        })

        return binding.root
    }

}