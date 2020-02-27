package com.example.recipemanager.profiles


import android.content.Context
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.databinding.ProfilePageBinding

class ProfileFragment : Fragment() {

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideKeyboard(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ProfilePageBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_page, container, false
        )
        val application = requireNotNull(this.activity).application
        val database = AppDatabase.getInstance(application)
        val profileViewModel = ProfileViewModel(database.profileDao)
        binding.viewModel = profileViewModel
        binding.setLifecycleOwner(this)
        val adapter = ProfileRecyclerAdapter(ProfileOnClickListener {
            profileViewModel.onProfileClicked(it)
        }, database.profileDao)

        val username = arguments!!.getString("username", "")
        adapter.submitNewList(username)

        binding.profileRecycler.layoutManager = LinearLayoutManager(context)
        binding.profileRecycler.adapter = adapter



        profileViewModel.navigateToDetailedProfile.observe(this, Observer { profile ->
            profile?.let {
                this.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToDetailProfileFragment(it)
                )
                profileViewModel.navigateToDetailProfileDone()
            }
        }
        )

        profileViewModel.navigateToNewProfileFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToNewProfileFragment(username)
                )
                profileViewModel.navigateToNewProfileFragmentDone()
            }
        })

        return binding.root
    }

    private fun hideKeyboard(fragment: Fragment) {
        val imm =
            fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

}
