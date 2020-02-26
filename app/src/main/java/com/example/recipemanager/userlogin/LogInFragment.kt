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
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.databinding.LogInBinding
import com.example.recipemanager.profiles.ProfileFragment
import kotlinx.android.synthetic.main.log_in.*

class LogInFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : LogInBinding = DataBindingUtil.inflate(inflater,
            R.layout.log_in, container, false)

        val application = requireNotNull(this.activity).application
        val appDatabase = AppDatabase.getInstance(application)
        val logInDatabaseDao = appDatabase.userDatabaseDao

        val logInViewModel = LogInViewModel(
            logInDatabaseDao
        )

        binding.logInViewModel = logInViewModel

        binding.verifyButton.setOnClickListener {
            logInViewModel.onVerificationClicked(binding.usernameEdit.text.toString(), binding.passwordEdit.text.toString())
        }


        logInViewModel.navigateToRegisterFragment.observe(this, Observer {
            if(it == true){
                this.findNavController().navigate(
                    LogInFragmentDirections.actionLogInFragmentToRegistrationFragment())
                    logInViewModel.navigationToRegisterDone()
            }

        })

        logInViewModel.navigateToProfileFragment.observe(this, Observer {
            if(it == true){
                this.findNavController().navigate(
                    LogInFragmentDirections.actionLogInFragmentToProfileFragment(username_edit.text.toString())
                )
                logInViewModel.navigationToProfileFragmentDone()
            }
        })
        return binding.root
    }


}