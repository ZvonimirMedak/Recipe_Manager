package com.example.recipemanager.profiles

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.recipemanager.utils.DatabaseProfileUtils
import kotlinx.android.synthetic.main.add_new_profile.*

class NewProfileFragment : Fragment() {
    private lateinit var databaseProfileUtils: DatabaseProfileUtils
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddNewProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_new_profile, container, false)
        val application = requireNotNull(this.activity).application
        databaseProfileUtils = DatabaseProfileUtils(application)
        val viewModel = ProfileViewModel(activity!!, databaseProfileUtils)
        val username = arguments!!.getString("username")
        binding.viewModel = viewModel
        setupNavigationObserver(viewModel)
        setupOnClickListeners(binding, viewModel, username!!)
        setupTouchListener(binding, this)
        return binding.root
    }

    private fun setupTouchListener(binding: AddNewProfileBinding, fragment : NewProfileFragment) {
        binding.root.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                databaseProfileUtils.hideKeyboard(fragment)
                return false
            }

        })
    }

    private fun setupOnClickListeners(binding : AddNewProfileBinding, viewModel: ProfileViewModel, username : String ){
        binding.createButton.setOnClickListener {
            val checkedBoxes = getAllBoxes()
            viewModel.inputProfile(profile_name_edit.text.toString(), checkedBoxes, username,binding.root)
        }
    }

    private fun setupNavigationObserver(viewModel: ProfileViewModel){
        viewModel.navigateToProfileFragment.observe(this, Observer {
            if (it == true) {
                activity!!.onBackPressed()
                viewModel.navigationToProfileFragmentDone()
            }
        })
    }

    private fun getAllBoxes(): ArrayList<Boolean> {
        val list = ArrayList<Boolean>()
        list.add(lactose_intolerance_check.isChecked)
        list.add(gluten_intolerance_check.isChecked)
        list.add(caffeine_intolerance_check.isChecked)
        list.add(fructose_intolerance_check.isChecked)
        return list

    }
}