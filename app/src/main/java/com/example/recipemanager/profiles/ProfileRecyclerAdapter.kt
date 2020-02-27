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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileRecyclerAdapter(
    val onClickListener: ProfileOnClickListener,
    val databaseDao: ProfileDao
) : ListAdapter<DataItem, ProfileRecyclerAdapter.ViewHolder>(ProfileDiffCallback()) {


    private fun getProfiles(username: String) = databaseDao.getAllProfiles(username)
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun submitNewList(username: String) {
        adapterScope.launch {
            val list = getProfiles(username)
            val items = list?.map { DataItem.ProfileItem(it) }
            Log.d("msg", items.toString())
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileRecyclerAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProfileRecyclerAdapter.ViewHolder, position: Int) {
        val profile = getItem(position) as DataItem.ProfileItem
        holder.bind(profile.profile, onClickListener)

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

class ProfileDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.profileId == newItem.profileId
    }

}

class ProfileOnClickListener(val onClickListener: (profileId: Long) -> Unit) {
    fun onClick(profile: Profile) = onClickListener(profile.profileId)
}


sealed class DataItem {
    data class ProfileItem(val profile: Profile) : DataItem() {
        override val profileId = profile.profileId
    }

    abstract val profileId: Long
}