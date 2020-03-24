package com.example.recipemanager.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CommentDao {
    @Insert
    fun insertComment(comment: Comment)
    @Query("DELETE FROM comment_table WHERE commentId=:key")
    fun deleteComment(key:Long)
    @Query("SELECT * FROM comment_table WHERE recipeId =:key")
    fun getComments(key: Long): List<Comment>?
    @Query("UPDATE comment_table SET commentText =:text WHERE commentId =:key ")
    fun updateComment(text: String, key: Long)
}