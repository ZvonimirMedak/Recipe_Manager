package com.example.recipemanager.recipeComments


import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanager.MainActivity
import com.example.recipemanager.R
import com.example.recipemanager.appDatabase.Comment
import com.example.recipemanager.appDatabase.Ingredient
import com.example.recipemanager.appDatabase.Profile
import com.example.recipemanager.appDatabase.Recipe
import com.example.recipemanager.databinding.CommentListItemBinding
import com.example.recipemanager.utils.DatabaseCommentUtils
import kotlinx.android.synthetic.main.popup.view.*
import kotlinx.android.synthetic.main.popup_delete.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeCommentsRecyclerAdapter(private val databaseCommentUtils: DatabaseCommentUtils,private val profile: Profile, private val recipe: Recipe, private val activity: Activity) : ListAdapter<Comment, RecipeCommentsRecyclerAdapter.ViewHolder>(
    CommentsDiffCallback()){

    private lateinit var popupWindow : PopupWindow
    private lateinit var popupView : View

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        setupPopupWindow()
    }


    private fun setupPopupWindow(){

        val inflater = LayoutInflater.from(activity)

        popupWindow = PopupWindow(activity)
        popupView = inflater.inflate(R.layout.popup, null)
        popupWindow.contentView = popupView
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )


    }
    fun deleteComment(comment: Comment){
        coroutineScope.launch {
            databaseCommentUtils.deleteComment(comment.commentId)
            val list = databaseCommentUtils.getComments(recipe.recipeId)
            withContext(Dispatchers.Main){
                submitList(list)
            }
        }
    }
    fun openEditPopup(rootLayout: View, commentId: Long){
        popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
        popupView.insert_button.setOnClickListener {
            editComment(commentId, popupView.ingredient_edit.text.toString())
            popupWindow.dismiss()
        }

    }
    private fun editComment(commentId: Long, text: String){
        coroutineScope.launch{
            databaseCommentUtils.updateComment(commentId, text)
            val list = databaseCommentUtils.getComments(recipe.recipeId)
            withContext(Dispatchers.Main){
                submitList(list)
            }
        }
    }

    fun getAllComments(recipeId: Long){
        coroutineScope.launch {
            val list = databaseCommentUtils.getComments(recipeId)
            withContext(Dispatchers.Main){
                submitList(list)
            }
        }
    }
    fun insertComment(comment: Comment){
        if(comment.commentText != "") {
            coroutineScope.launch {
                databaseCommentUtils.insertComment(
                    Comment(
                        profileName = profile.profileName,
                        recipeId = comment.recipeId,
                        commentText = comment.commentText,
                        rating =  comment.rating
                    )
                )
                val list = databaseCommentUtils.getComments(comment.recipeId)
                withContext(Dispatchers.Main) {
                    submitList(list)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, profile, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }


    class ViewHolder private constructor(val binding: CommentListItemBinding,val profile: Profile, val adapter: RecipeCommentsRecyclerAdapter) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Comment) {
            if(profile.profileName != item.profileName){
                binding.deleteComment.isVisible = false
                binding.editComment.isVisible = false
            }
            binding.editComment.setOnClickListener{
                adapter.openEditPopup(binding.root, item.commentId)
            }
            binding.deleteComment.setOnClickListener{
                adapter.deleteComment(item)
            }
            when (item.rating){
                1 -> binding.star1.setImageResource(R.drawable.round_star_black_18dp)
                2 ->{binding.star1.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star2.setImageResource(R.drawable.round_star_black_18dp)}
                3 ->{ binding.star1.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star2.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star3.setImageResource(R.drawable.round_star_black_18dp)}
                4 ->{binding.star1.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star2.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star3.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star4.setImageResource(R.drawable.round_star_black_18dp)}
                5 ->{binding.star1.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star2.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star3.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star4.setImageResource(R.drawable.round_star_black_18dp)
                    binding.star5.setImageResource(R.drawable.round_star_black_18dp)}
            }
            binding.comment = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, profile: Profile, adapter: RecipeCommentsRecyclerAdapter): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, profile, adapter)

            }
        }
    }


}
class CommentsDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentId == newItem.commentId
    }
}
class EditCommentOnClickListener(val onClickListener: (commentId: Long) -> Unit) {
    fun onClick(comment: Comment) = onClickListener(comment.commentId)
}