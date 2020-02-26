package com.example.recipemanager.appDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_table", foreignKeys = [ForeignKey(entity = Recipe::class, parentColumns = ["recipeId"], childColumns = ["recipeId"])])
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val favouriteId : Long = 0L,
    val profileId : Long,
    @ColumnInfo(index = true)
    val recipeId : Long
)