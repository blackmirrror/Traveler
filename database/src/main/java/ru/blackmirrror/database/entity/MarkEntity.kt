package ru.blackmirrror.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marks")
data class MarkEntity(
    @PrimaryKey val id: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val averageRating: Double?,
    val likeCount: Int?,
    val dateChange: Int?,
    val dateCreate: Int?,
    val authorId: Long
)
