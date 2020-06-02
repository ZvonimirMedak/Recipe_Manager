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
import com.example.recipemanager.appDatabase.Comment
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.RecipeCommentsBinding
import com.example.recipemanager.databinding.RegistrationFormBinding
import com.example.recipemanager.registration.RegistrationFragment
import com.example.recipemanager.utils.DatabaseCommentUtils

class RecipeCommentsFragment : Fragment() {

    var rating = 0
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
        adapter.getAllComments(recipe.recipeId)
        setupOnClickListeners(binding, profile, recipe)
        setupTouchListener(binding, this)
        return binding.root
    }
    private fun setupOnClickListeners(binding: RecipeCommentsBinding, profile: Profile, recipe: Recipe){
        binding.doneButton.setOnClickListener {
            adapter.insertComment(Comment(commentText = binding.commentEdit.text.toString(), profileName = profile.profileName, recipeId = recipe.recipeId,rating =  rating))
            binding.commentEdit.text.clear()
            binding.star1.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_border_black_18dp)
            databaseCommentUtils.hideKeyboard(this)
        }
        binding.star1.setOnClickListener {
            binding.star1.setImageResource(R.drawable.round_star_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_border_black_18dp)
            rating = 1
        }
        binding.star2.setOnClickListener {
            binding.star1.setImageResource(R.drawable.round_star_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_border_black_18dp)
            rating = 2
        }
        binding.star3.setOnClickListener {
            binding.star1.setImageResource(R.drawable.round_star_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_border_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_border_black_18dp)
            rating = 3
        }
        binding.star4.setOnClickListener {
            binding.star1.setImageResource(R.drawable.round_star_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_border_black_18dp)
            rating = 4
        }
        binding.star5.setOnClickListener {
            binding.star1.setImageResource(R.drawable.round_star_black_18dp)
            binding.star2.setImageResource(R.drawable.round_star_black_18dp)
            binding.star3.setImageResource(R.drawable.round_star_black_18dp)
            binding.star4.setImageResource(R.drawable.round_star_black_18dp)
            binding.star5.setImageResource(R.drawable.round_star_black_18dp)
            rating = 5
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
