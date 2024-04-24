package com.fadhil.storyapp.data.source.local.prefs

import android.content.SharedPreferences
import com.fadhil.storyapp.domain.model.Configuration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ConfigurationLocalSource @Inject constructor(sharedPreference: SharedPreferences) :
    LocalDataSource<Configuration?>(sharedPreference) {

    override fun getKeyName(): String = "configuration"
    override fun getValue(json: String): Configuration? =
        Gson().fromJson(json, object : TypeToken<Configuration?>() {}.type)

}