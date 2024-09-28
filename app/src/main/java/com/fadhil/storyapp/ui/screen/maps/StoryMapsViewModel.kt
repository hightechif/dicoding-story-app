package com.fadhil.storyapp.ui.screen.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryMapsViewModel @Inject constructor(
    private val storyUseCase: IStoryUseCase
) : ViewModel() {

    private val _page = MutableLiveData<Int>().apply { postValue(0) }
    val page: LiveData<Int> = _page
    private val _size = MutableLiveData<Int>().apply { postValue(10) }
    val size: LiveData<Int> = _size
    private val _location = MutableLiveData<Int>().apply { postValue(1) }
    val location: LiveData<Int> = _location

    fun getAllStories(reload: Boolean) =
        storyUseCase.getAllStory(page.value, size.value, location.value, reload).asLiveData()

}