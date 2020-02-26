package com.example.recipemanager.ingredients

import android.os.Bundle
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
import kotlinx.android.synthetic.main.popup.view.*


class IngredientsFragment : Fragment(){
    lateinit var adapter : IngredientsRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : MyIngredientsBinding = DataBindingUtil.inflate(inflater, R.layout.my_ingredients, container, false)
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val ingredientDao = database.ingredientDao
        val profileId = arguments!!.getLong("profileId", 0)
        val popupWindow = PopupWindow(activity)
        val popupView =inflater.inflate(R.layout.popup, null)
        val deletePopup = PopupWindow(activity)
        val deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        deletePopup.isFocusable = true
        deletePopup.contentView = deletePopupView
        val viewModel = IngredientsViewModel(ingredientDao, deletePopup, deletePopupView, binding.root)
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
            adapter.deleteIngredient(it)
        }, ingredientDao, deletePopup, deletePopupView, binding.root, profileId)

        popupWindow.isFocusable = true
        popupWindow.contentView = popupView
        popupView.insert_button.setOnClickListener{
            adapter.insertNewIngredient(Ingredient(ingredientText = popupView.ingredient_edit.text.toString(), profileId = profileId), profileId)
            popupWindow.dismiss()
        }
        viewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToAllRecipesFragment2(profileId)
                )
                viewModel.navigationToAllRecipesDone()
            }
        })

        viewModel.navigateToRecommendedRecipes.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToRecommendedRecipe2(profileId)
                )
                viewModel.navigationToRecommendedRecipesDone()
            }
        })
        viewModel.navigateToFavouriteRecipes.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(
                    IngredientsFragmentDirections.actionIngredientsFragmentToFavouriteRecipeFragment(profileId)
                )
                viewModel.navigationToFavouriteRecipesDone()
            }
        })

        binding.allRecipesButton.setOnClickListener{
            viewModel.navigateToAllRecipes()
        }
        binding.recommendedRecipesButton.setOnClickListener {
            viewModel.navigateToRecommendedRecipes()
        }
        binding.favouriteRecipesButton.setOnClickListener {
            viewModel.navigateToFavouriteRecipes()
        }

        binding.myIngredientsRecycler.layoutManager = LinearLayoutManager(context)
        binding.myIngredientsRecycler.adapter = adapter
        adapter.submitNewList(profileId)
        binding.fabIngredients.setOnClickListener {
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0,0)
        }
        return binding.root

    }

}