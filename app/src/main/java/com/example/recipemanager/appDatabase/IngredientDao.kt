package com.example.recipemanager.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IngredientDao {
    @Insert
    fun insertAllIngredients(ingredients: List<Ingredient>)

    @Insert
    fun insertIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table WHERE recipeId =:key")
    fun getAllRecipeIngredients(key: Long): List<Ingredient>?

    @Query("SELECT * FROM ingredient_table WHERE profileId =:key")
    fun getAllProfileIngredients(key: Long): List<Ingredient>?

    @Query("DELETE FROM ingredient_table WHERE recipeId =:key")
    fun deleteRecipeIngredient(key: Long)

    @Query("DELETE FROM ingredient_table WHERE ingredientId =:key")
    fun deleteIngredient(key: Long)
}