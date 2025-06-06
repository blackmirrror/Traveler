package ru.blackmirrror.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mark_likes")
data class LikeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val markId: Long,
    val userId: Long
)
