package com.example.recipemanager.appDatabase

import androidx.room.*

@Entity(tableName = "user_profiles_table",
    foreignKeys =  [ForeignKey(entity = User::class,
        parentColumns = ["username"],
        childColumns = ["Username"])])
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val profileId : Long,
    @ColumnInfo(name = "Profile")
    val profileName : String,
    val lactose_intolerance : Boolean,
    val gluten_intolerance : Boolean,
    val caffeine_intolerance: Boolean,
    val fructose_intolerance : Boolean,
    @ColumnInfo(name = "Username", index = true)
    val profile_username : String
)