package com.example.recipemanager.createRecipe

import android.graphics.Color
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
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.CreateRecipeBinding
import com.example.recipemanager.ingredients.IngredientOnClickListener
import com.example.recipemanager.ingredients.IngredientsRecyclerAdapter
import com.example.recipemanager.ingredients.IngredientsViewModel

class CreateRecipeFragment : Fragment() {
    private lateinit var adapter: IngredientsRecyclerAdapter
//    private lateinit var recipe: Recipe
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CreateRecipeBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_recipe, container, false)
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val recipeDao = database.recipeDao
        val ingredientDao = database.ingredientDao
        val profileId = arguments!!.getLong("profileId", 0)
        val viewModel = CreateRecipeViewModel(recipeDao, ingredientDao, profileId)
        val deletePopup = PopupWindow(activity)
        val deletePopupView = inflater.inflate(R.layout.popup_delete, null)
        deletePopup.isFocusable = true
        deletePopup.contentView = deletePopupView
        /*recipe = Recipe(
            name = "Pahuljaste palačinke s vodom",
            description = "Sve ručno miješati, brašno dodavati na kraju, ako je potrebno.",
            timeToMake = "40 min",
            typeOfMeal = "Desert/Slano",
            photoUrl = "https://i.imgur.com/8DeRKmP.jpg"
        )*/
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
            adapter.deleteRecipeIngredient(it)
        }, ingredientDao, deletePopup, deletePopupView, binding.root, profileId)

        binding.createRecipeIngredientsRecycler.adapter = adapter
        binding.createRecipeIngredientsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.insertRecipeButton.setOnClickListener {
            val recipe = Recipe(name = binding.recipeNameEdit.text.toString(), description = binding.descriptionEdit.text.toString(),
            timeToMake = binding.timeToMakeEdit.text.toString(), typeOfMeal = binding.typeOfMealEdit.text.toString(),
            gluten = binding.glutenCheck.isChecked, fructose = binding.fructoseCheck.isChecked, lactose = binding.lactoseCheck.isChecked,
            caffeine = binding.caffeineCheck.isChecked, profileId = profileId
            ,photoUrl = binding.photoUrlEdit.text.toString())
            viewModel.insertRecipe(recipe)
        }
        binding.ingredientButton.setOnClickListener {
            if (binding.ingredientEditRecipe.text.toString() != "") {
                adapter.createRecipeIngredient(
                    Ingredient(
                        ingredientText = binding.ingredientEditRecipe.text.toString(),
                        recipeId = 0
                    )
                )
                binding.ingredientEditRecipe.text.clear()
            }

        }
        viewModel.navigateToAllRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    CreateRecipeFragmentDirections.actionCreateRecipeFragmentToAllRecipesFragment2(
                        profileId
                    )
                )
                viewModel.navigationToAllRecipesDone()
            }
        })

        return binding.root
    }
}