package com.mezo.bubblefeature.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.mezo.bubblefeature.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    private val _isDynamicThemeEnabled = MutableStateFlow(prefs.getBoolean("dynamic_theme", true))

    override val isDynamicThemeEnabled: Flow<Boolean> = _isDynamicThemeEnabled.asStateFlow()

    override suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("dynamic_theme", enabled).apply()
        _isDynamicThemeEnabled.value = enabled
    }
}