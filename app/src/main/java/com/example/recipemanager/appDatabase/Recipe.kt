package com.example.recipemanager.appDatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "recipe_table", foreignKeys = [ForeignKey(entity = Profile::class, parentColumns = ["profileId"], childColumns = ["profileId"])])
data class Recipe (
    @PrimaryKey(autoGenerate = true)
    var recipeId : Long = 0L,
    var name : String,
    var photoUrl : String,
    var description: String,
    var timeToMake : String,
    var typeOfMeal : String,
    val gluten : Boolean = false,
    val lactose : Boolean = false,
    val caffeine : Boolean = false,
    val fructose : Boolean = false,
    @ColumnInfo(index = true)
    val profileId : Long? = null
) : Parcelable