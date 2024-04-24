package com.fadhil.storyapp.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fadhil.storyapp.data.source.local.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    /**
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
    */

    abstract fun storyDao(): StoryDao

}