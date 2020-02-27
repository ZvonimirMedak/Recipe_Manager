package com.example.recipemanager.appDatabase

import androidx.room.*

@Dao
interface RecipeDao {
    @Insert
    fun insertRecipe(recipe: Recipe)

    @Insert
    fun insertAllRecipes(recipes: List<Recipe>)

    @Query("DELETE FROM recipe_table WHERE recipeId= :key")
    fun deleteRecipe(key: Long)

    @Update
    fun updateRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe_table")
    fun getAllrecipes(): List<Recipe>?

    @Query("SELECT * FROM recipe_table WHERE name =:key")
    fun getNamedRecipe(key: String): Recipe?

    @Query("SELECT * FROM recipe_table WHERE recipeId =:key")
    fun getRecipe(key: Long): Recipe

    @Query("UPDATE recipe_table SET name =:name, photoUrl=:photoUrl, description=:description, typeOfMeal=:typeOfMeal, timeToMake =:timeToMake,  gluten=:gluten, caffeine=:caffeine, fructose=:fructose, lactose=:lactose WHERE recipeId =:recipeId")
    fun updateRecipe(name : String, photoUrl : String, description : String, typeOfMeal : String, timeToMake : String,
                     gluten : Boolean, caffeine : Boolean, fructose : Boolean, lactose : Boolean, recipeId : Long)

}