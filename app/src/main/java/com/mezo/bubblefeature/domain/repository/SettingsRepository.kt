package com.mezo.bubblefeature.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDynamicThemeEnabled: Flow<Boolean>
    suspend fun setDynamicThemeEnabled(enabled: Boolean)
}