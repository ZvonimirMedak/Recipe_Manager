package com.example.recipemanager.appDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Profile::class, User::class, Recipe::class, Ingredient::class, Favourite::class], version = 13, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract val profileDao : ProfileDao
    abstract val favouriteDao : FavouriteDao
    abstract val userDatabaseDao : UserDatabaseDao
    abstract val recipeDao : RecipeDao
    abstract val ingredientDao: IngredientDao
    companion object {
        private var INSTANCE: AppDatabase?  = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(this){
                var instance =
                    INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }

        }

    }
}