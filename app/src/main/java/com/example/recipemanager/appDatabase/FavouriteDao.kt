package com.example.recipemanager.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteDao{
    @Insert
    fun insertFavourite(favourite: Favourite)
    @Query("SELECT * FROM favourite_table WHERE profileId =:key")
    fun getAllProfileFavouirtes(key : Long) : List<Favourite>?
    @Query("DELETE FROM favourite_table WHERE favouriteId =:key")
    fun deleteFavourite(key : Long)
    @Query("SELECT * FROM favourite_table WHERE profileId =:profileKey AND recipeId =:recipeKey")
    fun getFavouriteRecipe(profileKey : Long, recipeKey : Long) : Favourite?

}