package com.example.recipemanager.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipemanager.appDatabase.User

@Dao
interface UserDatabaseDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM log_in_user_table WHERE username = :key")
    fun getUser(key: String): User?

    @Query("DELETE  FROM log_in_user_table")
    fun clear()
}