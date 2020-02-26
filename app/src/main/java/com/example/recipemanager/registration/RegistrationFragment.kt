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

class RegistrationFragment : Fragment(){

    private lateinit var binding: RegistrationFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_form, container, false)
        val application = requireNotNull(this.activity).application
        val appDatabase = AppDatabase.getInstance(application)
        val databaseDao = appDatabase.userDatabaseDao
        val registrationViewModel = RegistrationViewModel( databaseDao)
        binding.registrationViewModel = registrationViewModel
        binding.doneButton.setOnClickListener {
            registrationViewModel.insertNewUser(binding.regUsernameEdit.text.toString(),
                binding.regPasswordEdit.text.toString(), binding.regConfirmpassEdit.text.toString())
        }
        registrationViewModel.navigateToLogIn.observe(this, Observer {
            if(it == true){
                this.findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment())
                registrationViewModel.navigationToLogInDone()
            }
        })

        return binding.root
    }
}