package com.example.recipemanager.detailRecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.DetailRecipeBinding
import com.example.recipemanager.ingredients.IngredientOnClickListener
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.ingredients.IngredientsViewModel
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeWithIngredientsUtils
import kotlinx.android.synthetic.main.popup.view.*

class DetailRecipeFragment : Fragment() {

    lateinit var adapter: IngredientsRecyclerAdapter
    lateinit var databaseRecipeWithIngredientsUtils: DatabaseRecipeWithIngredientsUtils
    lateinit var databaseIngredientsUtils: DatabaseIngredientsUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailRecipeBinding =
            DataBindingUtil.inflate(inflater, R.layout.detail_recipe, container, false)
        val recipe = arguments!!.getParcelable<Recipe>("recipe")
        val profileId = arguments!!.getLong("profileId")
        binding.recipe = recipe
        val application = requireNotNull(this.activity).application
        databaseRecipeWithIngredientsUtils = DatabaseRecipeWithIngredientsUtils(application)
        databaseIngredientsUtils = DatabaseIngredientsUtils(application)
        val viewModel = DetailRecipeViewModel(activity!!)
        databaseRecipeWithIngredientsUtils.checkFavourite(recipe!!.recipeId, profileId, viewModel)
        Glide.with(binding.recipeImage).load(recipe.photoUrl).optionalCenterCrop()
            .into(binding.recipeImage)
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
        })
        binding.ingredientsRecipeRecycler.layoutManager = LinearLayoutManager(activity)
        binding.ingredientsRecipeRecycler.adapter = adapter
        databaseIngredientsUtils.submitRecipeList(adapter, recipe.recipeId)
        setupOnClickListeners(binding, viewModel, recipe, profileId)
        setupObservers(viewModel, recipe, profileId, binding)
        return binding.root
    }

    private fun setupOnClickListeners(binding: DetailRecipeBinding, viewModel: DetailRecipeViewModel, recipe : Recipe, profileId : Long){
        binding.favouriteButton.setOnClickListener {
            databaseRecipeWithIngredientsUtils.createFavouriteRecipe(recipe.recipeId, profileId, viewModel)
        }
        binding.editRecipeButton.setOnClickListener {
            viewModel.navigateToEditRecipe()
        }
        binding.deleteRecipeButton.setOnClickListener {
            databaseRecipeWithIngredientsUtils.deleteRecipe(recipe, profileId, viewModel)
        }
        viewModel.popupView.insert_button.setOnClickListener {
            databaseIngredientsUtils.insertNewIngredient(
                adapter,
                Ingredient(
                    ingredientText = viewModel.popupView.ingredient_edit.text.toString(),
                    profileId = profileId
                ), profileId
            )
            viewModel.popupWindow.dismiss()
        }
    }

    private fun setupObservers(viewModel: DetailRecipeViewModel, recipe: Recipe, profileId: Long, binding: DetailRecipeBinding){
        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToEditRecipeFragment(recipe, profileId)
                )
                viewModel.navigationToEditRecipeDone()
            }
        })
        viewModel.navigationToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToAllRecipesFragment2(
                        profileId
                    )
                )
                viewModel.navigationToAllRecipesDone()
            }
        })

        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController()
            }
        })
        viewModel.favouriteChecker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.favouriteButton.setImageResource(R.drawable.baseline_favorite_black_18dp)
            }
            if (it == null) {
                binding.favouriteButton.setImageResource(R.drawable.favourite_border)
            }

        })
    }
}