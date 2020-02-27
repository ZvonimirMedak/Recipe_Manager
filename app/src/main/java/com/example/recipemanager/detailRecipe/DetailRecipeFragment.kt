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
import kotlinx.android.synthetic.main.popup.view.*

class DetailRecipeFragment : Fragment() {

    lateinit var adapter: IngredientsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailRecipeBinding =
            DataBindingUtil.inflate(inflater, R.layout.detail_recipe, container, false)
        val recipe = arguments!!.getParcelable<Recipe>("recipe")
        val profileId = arguments!!.getLong("profileId", 0)
        binding.recipe = recipe
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val ingredientDao = database.ingredientDao
        val recipeDao = database.recipeDao
        val favouriteDao = database.favouriteDao
        val viewModel = DetailRecipeViewModel(recipeDao, ingredientDao, favouriteDao)
        viewModel.checkFavourite(recipe!!.recipeId, profileId)
        val popupWindow = PopupWindow(activity)
        val popupView = inflater.inflate(R.layout.popup, null)
        val deletePopup = PopupWindow(activity)
        val deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        Glide.with(binding.recipeImage).load(recipe.photoUrl).optionalCenterCrop()
            .into(binding.recipeImage)
        deletePopup.isFocusable = true
        deletePopup.contentView = deletePopupView
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
        }, ingredientDao, deletePopup, deletePopupView, binding.root, profileId)

        popupWindow.isFocusable = true
        popupWindow.contentView = popupView
        viewModel.favouriteChecker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.favouriteButton.setImageResource(R.drawable.baseline_favorite_black_18dp)
            }
            if (it == null) {
                binding.favouriteButton.setImageResource(R.drawable.favourite_border)
            }

        })
        popupView.insert_button.setOnClickListener {
            adapter.insertNewIngredient(
                Ingredient(
                    ingredientText = popupView.ingredient_edit.text.toString(),
                    profileId = profileId
                ), profileId
            )
            popupWindow.dismiss()
        }
        binding.deleteRecipeButton.setOnClickListener {
            viewModel.deleteRecipe(recipe, profileId)
        }
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
        binding.editRecipeButton.setOnClickListener {
            viewModel.navigateToEditRecipe()
        }
        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController()
            }
        })
        binding.ingredientsRecipeRecycler.layoutManager = LinearLayoutManager(activity)
        binding.ingredientsRecipeRecycler.adapter = adapter
        Log.d("msgrecipe", recipe.recipeId.toString())
        adapter.submitRecipeList(recipe.recipeId)
        binding.favouriteButton.setOnClickListener {
            viewModel.createFavouriteRecipe(recipe.recipeId, profileId)
        }
        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    DetailRecipeFragmentDirections.actionDetailRecipeFragmentToEditRecipeFragment(recipe, profileId)
                )
                viewModel.navigationToEditRecipeDone()
            }
        })

        return binding.root
    }
}