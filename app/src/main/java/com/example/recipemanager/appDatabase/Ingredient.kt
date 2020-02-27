package com.example.recipemanager.appDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient_table")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long = 0L,
    val ingredientText: String,
    var recipeId: Long? = null,
    var profileId: Long? = null
)