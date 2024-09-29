package com.fadhil.storyapp.data.source.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadhil.storyapp.data.source.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    @Insert(entity = StoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryEntity>)

    @Query("SELECT * FROM stories ORDER BY created_time DESC")
    fun getStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories ORDER BY created_time DESC")
    fun getPagingStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()

}