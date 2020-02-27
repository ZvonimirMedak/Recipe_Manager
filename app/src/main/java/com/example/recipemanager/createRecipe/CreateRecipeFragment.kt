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
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils
import com.example.recipemanager.utils.DatabaseRecipeWithIngredientsUtils

class CreateRecipeFragment : Fragment() {
    private lateinit var adapter: IngredientsRecyclerAdapter
//    private lateinit var recipe: Recipe
    private lateinit var databaseIngredientsUtils: DatabaseIngredientsUtils
    private lateinit var databaseRecipeWithIngredientsUtils: DatabaseRecipeWithIngredientsUtils
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CreateRecipeBinding =
            DataBindingUtil.inflate(inflater, R.layout.create_recipe, container, false)
        val application = requireNotNull(this.activity).application
        databaseIngredientsUtils = DatabaseIngredientsUtils(application)
        databaseRecipeWithIngredientsUtils = DatabaseRecipeWithIngredientsUtils(application)
        val profileId = arguments!!.getLong("profileId", 0)
        val viewModel = CreateRecipeViewModel(activity!!, binding.root)
        /*recipe = Recipe(
            name = "Pahuljaste palačinke s vodom",
            description = "Sve ručno miješati, brašno dodavati na kraju, ako je potrebno.",
            timeToMake = "40 min",
            typeOfMeal = "Desert/Slano",
            photoUrl = "https://i.imgur.com/8DeRKmP.jpg"
        )*/
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
            databaseIngredientsUtils.deleteRecipeIngredient(it,adapter = adapter, rootLayout = binding.root)
        })
        binding.createRecipeIngredientsRecycler.adapter = adapter
        binding.createRecipeIngredientsRecycler.layoutManager = LinearLayoutManager(activity)
        setupOnClickListeners(binding, viewModel, profileId)
        setupNavigationObserver(viewModel, profileId)

        return binding.root
    }

    private fun setupOnClickListeners(
        binding: CreateRecipeBinding,
        viewModel: CreateRecipeViewModel,
        profileId: Long
    ) {
        binding.insertRecipeButton.setOnClickListener {
            if(binding.photoUrlEdit.text.isEmpty()){
                val recipe = Recipe(name = binding.recipeNameEdit.text.toString(), description = binding.descriptionEdit.text.toString(),
                    timeToMake = binding.timeToMakeEdit.text.toString(), typeOfMeal = binding.typeOfMealEdit.text.toString(),
                    gluten = binding.glutenCheck.isChecked, fructose = binding.fructoseCheck.isChecked, lactose = binding.lactoseCheck.isChecked,
                    caffeine = binding.caffeineCheck.isChecked, profileId = profileId)
                databaseRecipeWithIngredientsUtils.insertRecipe(recipe, viewModel)
            }else{
                val recipe = Recipe(name = binding.recipeNameEdit.text.toString(), description = binding.descriptionEdit.text.toString(),
                    timeToMake = binding.timeToMakeEdit.text.toString(), typeOfMeal = binding.typeOfMealEdit.text.toString(),
                    gluten = binding.glutenCheck.isChecked, fructose = binding.fructoseCheck.isChecked, lactose = binding.lactoseCheck.isChecked,
                    caffeine = binding.caffeineCheck.isChecked, profileId = profileId
                    ,photoUrl = binding.photoUrlEdit.text.toString())
                databaseRecipeWithIngredientsUtils.insertRecipe(recipe, viewModel)
            }

        }
        binding.ingredientButton.setOnClickListener {
            if (binding.ingredientEditRecipe.text.toString() != "") {
                databaseIngredientsUtils.createRecipeIngredient(
                    adapter,
                    Ingredient(
                        ingredientText = binding.ingredientEditRecipe.text.toString(),
                        recipeId = 0
                    )
                )
                binding.ingredientEditRecipe.text.clear()
            }

        }
    }

    private fun setupNavigationObserver(viewModel: CreateRecipeViewModel, profileId : Long) {
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
    }
}