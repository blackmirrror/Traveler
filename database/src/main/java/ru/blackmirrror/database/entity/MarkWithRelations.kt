package ru.blackmirrror.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MarkWithRelations(
    @Embedded val mark: MarkEntity,

    @Relation(
        parentColumn = "authorId",
        entityColumn = "id"
    )
    val author: UserEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "markId"
    )
    val categories: List<CategoryEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "markId"
    )
    val reviews: List<ReviewEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "markId"
    )
    val likes: List<LikeEntity>
)
