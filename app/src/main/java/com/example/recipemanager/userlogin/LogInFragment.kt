package com.example.recipemanager.userlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipemanager.R
import com.example.recipemanager.databinding.LogInBinding
import com.example.recipemanager.utils.DatabaseUserUtils
import kotlinx.android.synthetic.main.log_in.*

class LogInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LogInBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.log_in, container, false
        )
        val application = requireNotNull(this.activity).application
        val databaseUserUtils = DatabaseUserUtils(application)
        val viewModel = LogInViewModel()
        binding.viewModel = viewModel
        binding.verifyButton.setOnClickListener {
            databaseUserUtils.onVerificationClicked(
                binding.usernameEdit.text.toString(),
                binding.passwordEdit.text.toString(),
                viewModel
            )
        }

        setupNavigationObservers(viewModel)

        return binding.root
    }

    private fun setupNavigationObservers(viewModel: LogInViewModel) {
        viewModel.navigateToProfileFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    LogInFragmentDirections.actionLogInFragmentToProfileFragment(username_edit.text.toString())
                )
                viewModel.navigationToProfileFragmentDone()
            }
        })

        viewModel.navigateToRegisterFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
                )
                viewModel.navigationToRegisterDone()
            }

        })
    }

}