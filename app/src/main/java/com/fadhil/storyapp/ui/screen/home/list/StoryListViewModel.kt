package com.fadhil.storyapp.ui.screen.home.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fadhil.storyapp.domain.model.Story
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val storyUseCase: IStoryUseCase
) : ViewModel() {

    val size = MutableLiveData<Int>()
    val location = MutableLiveData<Int>()

    fun setSize(input: Int) {
        size.value = input
    }

    fun setLocation(input: Int) {
        location.value = input
    }

    fun getStoriesPaging() =
        storyUseCase.getPagingStory(size.value, location.value)

    val stories: LiveData<PagingData<Story>> =
        storyUseCase.getPagingStory(10, 1).cachedIn(viewModelScope)

}