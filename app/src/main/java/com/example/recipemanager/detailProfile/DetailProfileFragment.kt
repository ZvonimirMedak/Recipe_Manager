package com.example.recipemanager.detailProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.databinding.ProfileDetailBinding
import com.example.recipemanager.databinding.ProfileListItemBinding
import kotlinx.android.synthetic.main.profile_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class DetailProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ProfileDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.profile_detail, container, false)
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val databaseDao = database.profileDao
        val profileViewModelFactory =
            DetailProfileViewModelFactory(arguments!!.getLong("profileId", 0), databaseDao)
        val profileViewModel = profileViewModelFactory.create(DetailProfileViewModel::class.java)

        profileViewModel.currentProfile.observe(this, Observer {
            if (it != null) {
                profileViewModel.profile = profileViewModel.currentProfile.value
                bind(profileViewModel)
            }
        })

        profileViewModel.navigateToProfileFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailProfileFragmentDirections.actionDetailProfileFragmentToProfileFragment(
                        profileViewModel.profile!!.profile_username
                    )
                )
                profileViewModel.navigateToProfileFragmentDone()
            }
        })

        profileViewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailProfileFragmentDirections.actionDetailProfileFragmentToRecommendedRecipe2(
                        arguments!!.getLong("profileId", 0)
                    )
                )
                profileViewModel.navigateToRecipesDone()
            }
        })


        binding.deleteButton.setOnClickListener {
            profileViewModel.deleteProfile()
        }

        binding.selectButton.setOnClickListener {
            profileViewModel.navigateToRecipesWanted()
        }

        return binding.root
    }

    private fun bind(profileViewModel: DetailProfileViewModel) {
        profile_name_detail.text = profileViewModel.profile!!.profileName
        bindGluten(profileViewModel)
        bindLactose(profileViewModel)
        bindCaffeine(profileViewModel)
        bindFructose(profileViewModel)

    }

    private fun bindLactose(profileViewModel: DetailProfileViewModel) {
        if (profileViewModel.profile!!.lactose_intolerance) {
            lactose_intolerance_detail.text = getString(R.string.lactose_intolerant)
        } else {
            lactose_intolerance_detail.text = getString(R.string.lactose_tolerant)
        }
    }

    private fun bindCaffeine(profileViewModel: DetailProfileViewModel) {
        if (profileViewModel.profile!!.caffeine_intolerance) {
            caffeine_intolerance_detail.text = getString(R.string.caffeine_intolerant)
        } else {
            caffeine_intolerance_detail.text = getString(R.string.caffeine_tolerant)
        }
    }

    private fun bindGluten(profileViewModel: DetailProfileViewModel) {
        if (profileViewModel.profile!!.gluten_intolerance) {
            gluten_intolerance_detail.text = getString(R.string.gluten_intolerant)
        } else {
            gluten_intolerance_detail.text = getString(R.string.gluten_tolerant)
        }
    }

    private fun bindFructose(profileViewModel: DetailProfileViewModel) {
        if (profileViewModel.profile!!.fructose_intolerance) {
            fructose_intolerance_detail.text = getString(R.string.fructose_intolerant)
        } else {
            fructose_intolerance_detail.text = getString(R.string.fructose_tolerant)
        }
    }
}