package com.example.recipemanager.ingredients

import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.databinding.MyIngredientsBinding
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import kotlinx.android.synthetic.main.popup.view.*


class IngredientsFragment : Fragment() {
    lateinit var adapter: IngredientsRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MyIngredientsBinding =
            DataBindingUtil.inflate(inflater, R.layout.my_ingredients, container, false)
        val application = requireNotNull(this.activity).application
        val databaseIngredientsUtils = DatabaseIngredientsUtils(application)
        val profileId = arguments!!.getLong("profileId", 0)
        val viewModel = IngredientsViewModel(activity!!)
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
            adapter.deleteIngredient(it, profileId,binding.root)
        }, databaseIngredientsUtils, activity!!)
        binding.myIngredientsRecycler.layoutManager = LinearLayoutManager(context)
        binding.myIngredientsRecycler.adapter = adapter
        adapter.submitNewList(profileId)
        setupNavigationObservers(viewModel, profileId)
        setupOnClickListeners(viewModel, profileId, binding)
        return binding.root

    }

    private fun setupNavigationObservers(viewModel: IngredientsViewModel, profileId: Long) {
        viewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
        if (it == true) {
            this.findNavController().navigate(
                IngredientsFragmentDirections.actionIngredientsFragmentToAllRecipesFragment2(
                    profileId
                )
            )
            viewModel.navigationToAllRecipesDone()
        }
    })

        viewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToRecommendedRecipe2(
                        profileId
                    )
                )
                viewModel.navigationToRecommendedRecipesDone()
            }
        })
        viewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToFavouriteRecipeFragment(
                        profileId
                    )
                )
                viewModel.navigationToFavouriteRecipesDone()
            }
        })

    }

    private fun setupOnClickListeners(viewModel: IngredientsViewModel, profileId: Long, binding: MyIngredientsBinding){
        viewModel.popupView.insert_button.setOnClickListener {
            adapter.insertNewIngredient(
                Ingredient(
                    ingredientText = viewModel.popupView.ingredient_edit.text.toString(),
                    profileId = profileId
                ), profileId
            )
            viewModel.popupView.ingredient_edit.text.clear()
            viewModel.popupWindow.dismiss()
        }

        binding.allRecipesButton.setOnClickListener {
            viewModel.navigateToAllRecipes()
        }
        binding.recommendedRecipesButton.setOnClickListener {
            viewModel.navigateToRecommendedRecipes()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            viewModel.navigateToFavouriteRecipes()
        }
        binding.fabIngredients.setOnClickListener {
            viewModel.popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }
    }

}