package com.example.recipemanager.recipeComments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.RecipeCommentsBinding
import com.example.recipemanager.databinding.RegistrationFormBinding
import com.example.recipemanager.registration.RegistrationFragment
import com.example.recipemanager.utils.DatabaseCommentUtils

class RecipeCommentsFragment : Fragment() {

    lateinit var adapter: RecipeCommentsRecyclerAdapter
    lateinit var databaseCommentUtils: DatabaseCommentUtils
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RecipeCommentsBinding =
            DataBindingUtil.inflate(inflater, R.layout.recipe_comments, container, false)
        val recipe = arguments!!.getParcelable<Recipe>("recipe")!!
        val profile = arguments!!.getParcelable<Profile>("profile")!!
        val application = requireNotNull(this.activity).application
        databaseCommentUtils = DatabaseCommentUtils(application)
        adapter = RecipeCommentsRecyclerAdapter(databaseCommentUtils, profile, recipe, activity!! )
        binding.commentsRecycler.layoutManager = LinearLayoutManager(context)
        binding.commentsRecycler.adapter = adapter
        binding.commentsRecycler.hideKeyboard()
        adapter.getAllComments(recipe.recipeId)
        setupOnClickListeners(binding, profile, recipe)
        setupTouchListener(binding, this)
        return binding.root
    }
    private fun setupOnClickListeners(binding: RecipeCommentsBinding, profile: Profile, recipe: Recipe){
        binding.doneButton.setOnClickListener {
            adapter.insertComment(binding.commentEdit.text.toString(), profile, recipe.recipeId)
            binding.commentEdit.text.clear()
            databaseCommentUtils.hideKeyboard(this)
        }
    }
    private fun setupTouchListener(binding: RecipeCommentsBinding, fragment: RecipeCommentsFragment) {
        binding.root.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                databaseCommentUtils.hideKeyboard(fragment)
                return false
            }

        })
    }
}
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}