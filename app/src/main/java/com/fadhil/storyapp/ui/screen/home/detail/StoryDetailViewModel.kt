package com.fadhil.storyapp.ui.screen.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val storyUseCase: IStoryUseCase
) : ViewModel() {

    private val _storyId = MutableLiveData<String>()
    var storyId: LiveData<String> = _storyId

    private val _avatarUrl = MutableLiveData<String?>()
    var avatarUrl: LiveData<String?> = _avatarUrl

    private val _isFavorite = MutableLiveData(false)
    var isFavorite: LiveData<Boolean> = _isFavorite

    fun setStoryId(input: String) {
        _storyId.postValue(input)
    }

    fun setAvatarUrl(input: String?) {
        _avatarUrl.postValue(input)
    }

    fun setFavorite(input: Boolean) {
        _isFavorite.postValue(input)
    }

    fun getStoryDetail(storyId: String) =
        storyUseCase.getStoryDetail(storyId).asLiveData()

}