package com.example.recipemanager.detailProfile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.navigation.fragment.findNavController
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.databinding.ProfileDetailBinding
import com.example.recipemanager.databinding.ProfileListItemBinding
import com.example.recipemanager.utils.DatabaseProfileUtils
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
        val username = arguments!!.getString("username")
        val profileId = arguments!!.getLong("profileId", 0)
        val application = requireNotNull(this.activity).application
        val databaseProfileUtils = DatabaseProfileUtils(application)
        val viewModel = DetailProfileViewModel(databaseProfileUtils)
        viewModel.getProfileFromDatabase(profileId)
        setupNavigationObservers(viewModel, username!!, profileId)
        setupOnClickListeners(binding, viewModel, profileId)
        setupProfileObserver(viewModel, binding)

        return binding.root
    }

    private fun setupNavigationObservers(viewModel : DetailProfileViewModel, username : String, profileId : Long){

        viewModel.navigateToProfileFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailProfileFragmentDirections.actionDetailProfileFragmentToProfileFragment(
                        username
                    )
                )
                viewModel.navigationToProfileFragmentDone()
            }
        })

        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DetailProfileFragmentDirections.actionDetailProfileFragmentToRecommendedRecipe2(
                        profileId
                    )
                )
                viewModel.navigateToRecipesDone()
            }
        })
    }

    private fun setupProfileObserver(viewModel: DetailProfileViewModel, binding: ProfileDetailBinding){
        viewModel.currentProfile.observe(this, Observer {
            if (it != null) {
                bind(it, binding)
            }
        })
    }
    private fun setupOnClickListeners(binding: ProfileDetailBinding, viewModel: DetailProfileViewModel, profileId: Long){
        binding.deleteButton.setOnClickListener {
            viewModel.deleteProfile(profileId)
            viewModel.navigateToProfileFragment()
        }

        binding.selectButton.setOnClickListener {
            viewModel.navigateToRecipes()
        }
    }

    private fun bind(profile : Profile?, binding: ProfileDetailBinding) {
        binding.profileNameDetail.setText(profile!!.profileName)
        bindGluten(profile, binding)
        bindLactose(profile, binding)
        bindCaffeine(profile, binding)
        bindFructose(profile, binding)

    }

    private fun bindLactose(profile: Profile?, binding: ProfileDetailBinding) {
        if (profile!!.lactose_intolerance) {
            binding.lactoseIntoleranceDetail.setText(getString(R.string.lactose_intolerant))
        } else {
            binding.lactoseIntoleranceDetail.setText(getString(R.string.lactose_tolerant))
        }
    }

    private fun bindCaffeine(profile: Profile?, binding: ProfileDetailBinding) {
        if (profile!!.caffeine_intolerance) {
            binding.caffeineIntoleranceDetail.setText(getString(R.string.caffeine_intolerant))
        } else {
            binding.caffeineIntoleranceDetail.setText(getString(R.string.caffeine_tolerant))
        }
    }

    private fun bindGluten(profile: Profile?, binding: ProfileDetailBinding) {
        if (profile!!.gluten_intolerance) {
            binding.glutenIntoleranceDetail.setText(getString(R.string.gluten_intolerant))
        } else {
            binding.glutenIntoleranceDetail.setText(getString(R.string.gluten_tolerant))
        }
    }

    private fun bindFructose(profile: Profile?, binding: ProfileDetailBinding) {
        if (profile!!.fructose_intolerance) {
            binding.fructoseIntoleranceDetail.setText(getString(R.string.fructose_intolerant))
        } else {
            binding.fructoseIntoleranceDetail.setText(getString(R.string.fructose_tolerant))
        }
    }
}