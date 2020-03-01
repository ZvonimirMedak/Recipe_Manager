package com.example.recipemanager.profiles

import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.ProfileDao
import com.example.recipemanager.databinding.ProfileListItemBinding
import com.example.recipemanager.utils.DatabaseProfileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileRecyclerAdapter(
    private val onClickListener: ProfileOnClickListener
) : ListAdapter<Profile, ProfileRecyclerAdapter.ViewHolder>(ProfileDiffCallback()) {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile, onClickListener)

    }

    class ViewHolder private constructor(val binding: ProfileListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Profile, onClickListener: ProfileOnClickListener) {
            binding.profile = item
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProfileListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)

            }
        }
    }
}

class ProfileDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.profileId == newItem.profileId
    }

}

class ProfileOnClickListener(val onClickListener: (profileId: Long) -> Unit) {
    fun onClick(profile: Profile) = onClickListener(profile.profileId)
}
