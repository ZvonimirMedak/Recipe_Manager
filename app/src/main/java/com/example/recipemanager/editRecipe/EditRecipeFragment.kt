package com.example.recipemanager.editRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
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
import com.example.recipemanager.utils.DatabaseIngredientsUtils
import com.example.recipemanager.utils.DatabaseRecipeUtils

class EditRecipeFragment : Fragment(){

    private lateinit var adapter: IngredientsRecyclerAdapter
    private lateinit var databaseRecipeUtils : DatabaseRecipeUtils
    private lateinit var databaseIngredientsUtils: DatabaseIngredientsUtils
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : CreateRecipeBinding = DataBindingUtil.inflate(inflater, R.layout.create_recipe, container, false)
        val recipe = arguments!!.getParcelable<Recipe>("recipe")
        val profileId = arguments!!.getLong("profileId")
        setupUI(recipe, binding)
        val application = requireNotNull(this.activity).application
        databaseIngredientsUtils = DatabaseIngredientsUtils(application)
        databaseRecipeUtils = DatabaseRecipeUtils(application)
        val viewModel = EditRecipeViewModel()
        adapter = IngredientsRecyclerAdapter(IngredientOnClickListener {
            databaseIngredientsUtils.deleteRecipeIngredient(it, recipe!!.recipeId, adapter, binding.root)
        })
        binding.createRecipeIngredientsRecycler.adapter = adapter
        binding.createRecipeIngredientsRecycler.layoutManager = LinearLayoutManager(activity)
        databaseIngredientsUtils.submitRecipeList(adapter, recipe!!.recipeId)
        setupOnClickListeners(binding, viewModel, recipe)
        setupNavigationObserver(viewModel, profileId)
        setupOnTouchListener(binding, this)
        return binding.root
    }

    private fun setupOnTouchListener(binding: CreateRecipeBinding, editRecipeFragment: EditRecipeFragment) {
        binding.root.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                databaseIngredientsUtils.hideKeyboard(editRecipeFragment)
                return false
            }

        })

    }

    private fun setupNavigationObserver(viewModel: EditRecipeViewModel, profileId : Long){
        viewModel.navigateToDetailRecipe.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(
                    EditRecipeFragmentDirections.actionEditRecipeFragmentToDetailRecipeFragment(
                        it,
                        profileId
                    )
                )
                viewModel.navigationToDetailRecipeDone()
            }})
    }

    private fun setupUI(recipe: Recipe?, binding : CreateRecipeBinding) {
        binding.recipeNameEdit.setText(recipe!!.name)
        binding.typeOfMealEdit.setText(recipe.typeOfMeal)
        binding.timeToMakeEdit.setText(recipe.timeToMake)
        binding.descriptionEdit.setText(recipe.description)
        binding.photoUrlEdit.setText(recipe.photoUrl)
        binding.glutenCheck.isChecked = recipe.gluten
        binding.lactoseCheck.isChecked = recipe.lactose
        binding.caffeineCheck.isChecked = recipe.caffeine
        binding.fructoseCheck.isChecked = recipe.fructose
        binding.insertRecipeButton.setText(R.string.update)
    }

    private fun setupOnClickListeners(binding: CreateRecipeBinding, viewModel: EditRecipeViewModel, recipe: Recipe){
        binding.ingredientButton.setOnClickListener {
            databaseIngredientsUtils.createRecipeIngredient(adapter,Ingredient(ingredientText = binding.ingredientEditRecipe.text.toString(), recipeId = recipe.recipeId), recipe.recipeId)
            binding.ingredientEditRecipe.text.clear()
        }

        binding.insertRecipeButton.setOnClickListener {
            databaseRecipeUtils.updateRecipe(Recipe(
                recipeId = recipe.recipeId, name = binding.recipeNameEdit.text.toString(), timeToMake = binding.timeToMakeEdit.text.toString(),
                typeOfMeal = binding.typeOfMealEdit.text.toString(), photoUrl = binding.photoUrlEdit.text.toString(), description = binding.descriptionEdit.text.toString(),
                gluten = binding.glutenCheck.isChecked, fructose = binding.fructoseCheck.isChecked, lactose = binding.lactoseCheck.isChecked, caffeine = binding.caffeineCheck.isChecked
            ), viewModel)
        }
    }
}