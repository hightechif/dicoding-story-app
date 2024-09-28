package com.fadhil.storyapp.ui.screen.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadhil.storyapp.domain.usecase.IStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyUseCase: IStoryUseCase
) : ViewModel() {

    private val _description: MutableLiveData<String> =
        MutableLiveData<String>().also { it.postValue("") }
    val description: LiveData<String> = _description

    fun setDescription(input: String) {
        _description.postValue(input)
    }

    fun addNewStory(
        context: Context,
        description: String,
        uri: Uri,
        lat: Double?,
        lon: Double?
    ) = storyUseCase.addNewStory(context, description, uri, lat, lon)

}