package com.example.recipemanager.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.recipemanager.appDatabase.Profile

@Dao
interface ProfileDao {
    @Insert
    fun insertProfile(profile: Profile)

    @Update
    fun updateProfile(profile: Profile)

    @Query("DELETE  FROM user_profiles_table")
    fun clearAll()

    @Query("DELETE FROM user_profiles_table WHERE profileId= :key")
    fun deleteProfile(key: Long)

    @Query("SELECT * FROM user_profiles_table WHERE profileId=:key")
    fun getProfile(key: Long): Profile?

    @Query("SELECT * FROM user_profiles_table WHERE Username= :key")
    fun getAllProfiles(key: String): List<Profile>?

    @Query("SELECT * FROM user_profiles_table WHERE Profile= :key AND Username = :username")
    fun checkProfile(key: String, username: String): Profile?
}