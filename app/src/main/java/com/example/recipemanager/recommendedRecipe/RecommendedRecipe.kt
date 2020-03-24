package com.example.recipemanager.recommendedRecipe

import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.databinding.RecommendedRecipesBinding
import com.example.recipemanager.recipe.AllRecipeRecyclerAdapter
import com.example.recipemanager.recipe.RecipeOnClickListener
import com.example.recipemanager.utils.DatabaseRecipeUtils

class RecommendedRecipe : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RecommendedRecipesBinding =
            DataBindingUtil.inflate(inflater, R.layout.recommended_recipes, container, false)
        val application = requireNotNull(this.activity).application
        val profile = arguments!!.getParcelable<Profile>("profile")!!
        val databaseRecipeUtils = DatabaseRecipeUtils(application)
        val viewModel = RecommendedRecipeViewModel(databaseRecipeUtils)
        val adapter = AllRecipeRecyclerAdapter(
            RecipeOnClickListener {
                viewModel.navigateToDetailedRecipe(it)
            }, viewModel, profile
        )
        binding.viewModel = viewModel
        binding.recommendedRecipes.adapter = adapter
        binding.recommendedRecipes.layoutManager = LinearLayoutManager(context)
        viewModel.submitNewRecommendedList(adapter, profile.profileId)
        setupOnClickListeners(binding, viewModel)
        setupNavigationObservers(viewModel, profile)

        return binding.root
    }

    private fun setupOnClickListeners(binding: RecommendedRecipesBinding, viewModel: RecommendedRecipeViewModel) {
        binding.allRecipesButton.setOnClickListener {
            viewModel.navigateToAllRecipes()
        }
        binding.myIngredientsButton.setOnClickListener {
            viewModel.navigateToMyIngredients()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            viewModel.navigateToFavouriteRecipes()
        }
    }

    private fun setupNavigationObservers(viewModel: RecommendedRecipeViewModel, profile: Profile){

        viewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToAllRecipesFragment2(
                        profile
                    )
                )
                viewModel.navigationToAllRecipesDone()
            }
        })

        viewModel.navigateToDetailedRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToDetailRecipeFragment(
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
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToIngredientsFragment(
                        profile
                    )
                )
                viewModel.navigationToMyIngredientsDone()
            }
        })

        viewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    RecommendedRecipeDirections.actionRecommendedRecipe2ToFavouriteRecipeFragment(
                        profile
                    )
                )
                viewModel.navigationToFavouriteRecipesDone()
            }
        })
    }

}