package com.fadhil.storyapp.ui.screen.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val useCase: StoryUseCase
) : ViewModel() {

    val page = 0
    val size = 10
    val location = 0

    fun getAllStories(reload: Boolean) =
        useCase.getAllStory(page, size, location, reload).asLiveData()

}