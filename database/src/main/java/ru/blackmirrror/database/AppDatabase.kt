package ru.blackmirrror.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.blackmirrror.database.dao.MarkDao
import ru.blackmirrror.database.entity.MarkEntity

@Database(entities = [MarkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun markDao(): MarkDao
}
