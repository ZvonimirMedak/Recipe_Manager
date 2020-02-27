package com.example.recipemanager.registration

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
import com.example.recipemanager.databinding.RegistrationFormBinding
import com.example.recipemanager.utils.DatabaseUserUtils

class RegistrationFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : RegistrationFormBinding = DataBindingUtil.inflate(inflater, R.layout.registration_form, container, false)
        val application = requireNotNull(this.activity).application
        val databaseUserUtils = DatabaseUserUtils(application)
        val viewModel = RegistrationViewModel()
        binding.viewModel = viewModel
        binding.doneButton.setOnClickListener {
            databaseUserUtils.insertNewUser(
                binding.regUsernameEdit.text.toString(),
                binding.regPasswordEdit.text.toString(), binding.regConfirmpassEdit.text.toString(),
                viewModel
            )
        }
        setupNavigationObserver(viewModel)

        return binding.root
    }

    private fun setupNavigationObserver(viewModel: RegistrationViewModel){
        viewModel.navigateToLogin.observe(this, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment())
                viewModel.navigationToLoginDone()
            }
        })
    }
}