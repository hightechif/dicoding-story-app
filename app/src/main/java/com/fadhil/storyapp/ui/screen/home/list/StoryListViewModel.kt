package com.fadhil.storyapp.ui.screen.home.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.fadhil.storyapp.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val useCase: StoryUseCase
) : ViewModel() {

    private val _page = MutableLiveData<Int>().apply { postValue(0) }
    val page: LiveData<Int> = _page
    private val _size = MutableLiveData<Int>().apply { postValue(10) }
    val size: LiveData<Int> = _size
    private val _location = MutableLiveData<Int>().apply { postValue(1) }
    val location: LiveData<Int> = _location

    fun getAllStories(reload: Boolean) =
        useCase.getAllStory(page.value, size.value, location.value, reload).asLiveData()

    val storiesPagingFlow = useCase.getPagingStory(page.value, size.value, location.value, true)
        .map { pagingData ->
            pagingData.map { it }
        }
        .cachedIn(viewModelScope)

}