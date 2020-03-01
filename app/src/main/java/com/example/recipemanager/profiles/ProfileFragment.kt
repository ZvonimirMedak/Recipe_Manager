package com.example.recipemanager.profiles



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemanager.R
import com.example.recipemanager.databinding.ProfilePageBinding
import com.example.recipemanager.utils.DatabaseProfileUtils

class ProfileFragment : Fragment() {
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
        val databaseProfileUtils= DatabaseProfileUtils(application)
        val viewModel = ProfileViewModel(activity!!, databaseProfileUtils)
        binding.viewModel = viewModel
        val adapter = ProfileRecyclerAdapter(ProfileOnClickListener {
            viewModel.navigateToDetailProfile(it)
        })
        val username = arguments!!.getString("username", "")
        viewModel.submitNewList(adapter,username)
        binding.profileRecycler.layoutManager = LinearLayoutManager(context)
        binding.profileRecycler.adapter = adapter
        setupNavigationObservers(viewModel, username)
        return binding.root
    }

    private fun setupNavigationObservers(viewModel: ProfileViewModel, username : String ){
        viewModel.navigateToDetailedProfile.observe(this, Observer {
            if(it!= null) {
                this.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToDetailProfileFragment(it, username)
                )
                viewModel.navigationToDetailProfileDone()
            }
        }
        )

        viewModel.navigateToNewProfileFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToNewProfileFragment(username)
                )
                viewModel.navigationToNewProfileFragmentDone()
            }
        })
    }
}
