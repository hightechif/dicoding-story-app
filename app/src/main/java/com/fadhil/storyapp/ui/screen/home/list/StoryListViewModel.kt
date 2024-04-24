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

    fun getAllStories(page: Int?, size: Int?, location: Int?, reload: Boolean) =
        useCase.getAllStory(page, size, location, reload).asLiveData()

}