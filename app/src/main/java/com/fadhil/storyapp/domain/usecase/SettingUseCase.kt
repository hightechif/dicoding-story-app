package com.fadhil.storyapp.domain.usecase

import com.fadhil.storyapp.data.source.SettingRepository
import javax.inject.Inject

class SettingUseCase @Inject constructor(
    private val settingRepository: SettingRepository
): ISettingUseCase {

    override fun getThemeSetting() = settingRepository.getThemeSetting()

}