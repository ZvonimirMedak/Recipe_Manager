package com.example.recipemanager.appDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_in_user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    var username: String,
    @ColumnInfo(name = "Password")
    var password: String
)
