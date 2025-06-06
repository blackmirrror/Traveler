package ru.blackmirrror.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mark_reviews")
data class ReviewEntity(
    @PrimaryKey val id: Long,
    val markId: Long,
    val authorId: Long,
    val text: String,
    val rating: Int,
    val date: Int
)
