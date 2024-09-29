package com.fadhil.storyapp.ui.screen.home.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val storyUseCase: IStoryUseCase
) : ViewModel() {

    val page = MutableLiveData<Int>()
    val size = MutableLiveData<Int>()
    val location = MutableLiveData<Int>()

    fun setPage(input: Int) {
        page.value = input
    }

    fun setSize(input: Int) {
        size.value = input
    }

    fun setLocation(input: Int) {
        location.value = input
    }

    fun getAllStories(reload: Boolean) =
        storyUseCase.getAllStory(page.value, size.value, location.value, reload).asLiveData()

    fun getStoriesPaging() =
        storyUseCase.getPagingStory(size.value, location.value, true)

}