package com.example.recipemanager.detailRecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.DetailRecipeBinding
import com.example.recipemanager.ingredients.IngredientOnClickListener
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.ingredients.IngredientsViewModel
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils
import kotlinx.android.synthetic.main.popup.view.*

class DetailRecipeFragment : Fragment() {

    private lateinit var adapter: IngredientsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailRecipeBinding =
            DataBindingUtil.inflate(inflater, R.layout.detail_recipe, container, false)
        val recipe = arguments!!.getParcelable<Recipe>("recipe")
        val profile = arguments!!.getParcelable<Profile>("profile")!!
        binding.recipe = recipe
        val application = requireNotNull(this.activity).application
        val databaseIngredientsUtils = DatabaseIngredientsUtils(application)
        val databaseRecipeUtils = DatabaseRecipeUtils(application)
        val viewModel = DetailRecipeViewModel(activity!!,databaseRecipeUtils, databaseIngredientsUtils)
        viewModel.checkFavourite(recipe!!.recipeId, profile.profileId)
        Glide.with(binding.recipeImage).load(recipe.photoUrl).optionalCenterCrop()
            .into(binding.recipeImage)
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
        },databaseIngredientsUtils ,activity!!)
        binding.ingredientsRecipeRecycler.layoutManager = LinearLayoutManager(activity)
        binding.ingredientsRecipeRecycler.adapter = adapter
        adapter.submitRecipeList(recipe.recipeId)
        setupOnClickListeners(binding,recipe, profile.profileId,viewModel)
        setupObservers(viewModel, recipe, profile, binding)
        return binding.root
    }

    private fun setupOnClickListeners(binding: DetailRecipeBinding, recipe : Recipe, profileId : Long, viewModel: DetailRecipeViewModel){
        binding.favouriteButton.setOnClickListener {
            viewModel.createFavouriteRecipe(recipe.recipeId, profileId)
        }
        binding.editRecipeButton.setOnClickListener {
            viewModel.navigateToEditRecipe()
        }
        if(profileId == recipe.profileId){
            binding.commentRecipeButton.isVisible = false
            binding.deleteRecipeButton.setOnClickListener {
                viewModel.deleteRecipe(recipe, profileId)
            }
            binding.commentButton.setOnClickListener {
                viewModel.commentRecipe()
            }

        }
        else{
            binding.deleteRecipeButton.isVisible = false
            binding.commentButton.isVisible = false
            binding.commentRecipeButton.setOnClickListener{
                viewModel.commentRecipe()
            }
        }


        viewModel.popupView.insert_button.setOnClickListener {
            adapter.insertNewIngredient(
                Ingredient(
                    ingredientText = viewModel.popupView.ingredient_edit.text.toString(),
                    profileId = profileId
                ), profileId
            )
            viewModel.popupWindow.dismiss()
        }
    }

    private fun setupObservers(viewModel: DetailRecipeViewModel, recipe: Recipe, profile: Profile, binding: DetailRecipeBinding){
        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToEditRecipeFragment(recipe, profile)
                )
                viewModel.navigationToEditRecipeDone()
            }
        })
        viewModel.navigationToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToAllRecipesFragment2(
                        profile
                    )
                )
                viewModel.navigationToAllRecipesDone()
            }
        })

        viewModel.navigateToCommentRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToRecipeCommentsFragment(profile, recipe)
                )
                viewModel.navigatedToComments()
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