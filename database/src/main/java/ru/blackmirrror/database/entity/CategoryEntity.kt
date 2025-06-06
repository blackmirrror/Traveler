package ru.blackmirrror.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mark_categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val markId: Long,
    val category: String
)
