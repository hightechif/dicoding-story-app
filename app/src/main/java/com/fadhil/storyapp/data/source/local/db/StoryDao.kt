package com.fadhil.storyapp.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadhil.storyapp.data.source.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories ORDER BY created_time DESC")
    fun getStories(): Flow<List<StoryEntity>>

    @Insert(entity = StoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(story: StoryEntity)

}