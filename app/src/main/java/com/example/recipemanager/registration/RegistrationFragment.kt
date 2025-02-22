package com.example.recipemanager.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.databinding.AddNewProfileBinding
import com.example.recipemanager.databinding.RegistrationFormBinding
import com.example.recipemanager.profiles.NewProfileFragment
import com.example.recipemanager.utils.DatabaseUserUtils

class RegistrationFragment : Fragment() {
   lateinit var databaseUserUtils: DatabaseUserUtils


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : RegistrationFormBinding = DataBindingUtil.inflate(inflater, R.layout.registration_form, container, false)
        val application = requireNotNull(this.activity).application
        databaseUserUtils = DatabaseUserUtils(application)
        val viewModel = RegistrationViewModel(activity!!, databaseUserUtils)
        binding.viewModel = viewModel
        setupNavigationObserver(viewModel)
        setupOnClickListeners(binding, viewModel)
        setupTouchListener(binding, this)
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

    private fun setupOnClickListeners(binding: RegistrationFormBinding, viewModel: RegistrationViewModel){
        binding.doneButton.setOnClickListener {
            viewModel.insertNewUser(
                binding.regUsernameEdit.text.toString(),
                binding.regPasswordEdit.text.toString(), binding.regConfirmpassEdit.text.toString(),
                binding.root
            )
        }
    }
    private fun setupTouchListener(binding: RegistrationFormBinding, fragment: RegistrationFragment) {
        binding.root.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                databaseUserUtils.hideKeyboard(fragment)
                return false
            }

        })
    }

}