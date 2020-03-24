package com.example.recipemanager.utils

import android.app.Application
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.recipemanager.appDatabase.AppDatabase
import com.example.recipemanager.appDatabase.Comment
import com.example.recipemanager.appDatabase.CommentDao

class DatabaseCommentUtils(application: Application) : HideKeyboardUtil {
    private val database = AppDatabase.getInstance(application)
    private val commentDao =  database.commentDao
    fun getComments(recipeId: Long) = commentDao.getComments(recipeId)
    fun insertComment(comment: Comment) = commentDao.insertComment(comment)
    fun deleteComment(commentId: Long) = commentDao.deleteComment(commentId)
    fun updateComment(commentId: Long, text: String) = commentDao.updateComment(text, commentId)
    override fun hideKeyboard(fragment: Fragment) {
        val imm =
            fragment.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.view!!.windowToken, 0)
    }
}