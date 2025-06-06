package ru.blackmirrror.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.database.entity.CategoryEntity
import ru.blackmirrror.database.entity.LikeEntity
import ru.blackmirrror.database.entity.MarkEntity
import ru.blackmirrror.database.entity.MarkWithRelations
import ru.blackmirrror.database.entity.ReviewEntity
import ru.blackmirrror.database.entity.UserEntity

@Dao
interface MarkDao {

    @Query("SELECT * FROM marks")
    suspend fun getAllMarks(): List<MarkEntity>

//    @Transaction
//    @Query("SELECT * FROM marks")
//    suspend fun getAllMarksWithRelations(): List<MarkWithRelations>

    @Query("SELECT * FROM marks WHERE id = :markId")
    suspend fun getMarkById(markId: Long): MarkEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMarks(marks: List<MarkEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUsers(users: List<UserEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCategories(categories: List<CategoryEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertReviews(reviews: List<ReviewEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertLikes(likes: List<LikeEntity>)
}
