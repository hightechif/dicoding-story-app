package com.fadhil.storyapp.data.source

import com.fadhil.storyapp.data.source.local.SettingLocalDataSource
import com.fadhil.storyapp.domain.repository.ISettingRepository
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val localSource: SettingLocalDataSource
) : ISettingRepository {

    override fun getThemeSetting() = localSource.getThemeSetting()

}