package com.example.recipemanager.appDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "comment_table"
    )
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val commentId: Long = 0L,
    val profileName: String,
    val commentText: String,
    val recipeId: Long
)