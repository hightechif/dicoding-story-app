package com.fadhil.storyapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface ISettingRepository {

    fun getThemeSetting(): Flow<Boolean>

}