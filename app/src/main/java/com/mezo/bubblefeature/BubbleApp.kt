package com.mezo.bubblefeature

import android.app.Application
import com.mezo.bubblefeature.data.datasource.NotificationDataSource
import com.mezo.bubblefeature.data.repository.ChatRepositoryImpl
import com.mezo.bubblefeature.data.repository.SettingsRepositoryImpl
import com.mezo.bubblefeature.domain.repository.ChatRepository
import com.mezo.bubblefeature.domain.repository.SettingsRepository

class BubbleApp : Application() {

    // Manual Dependency Injection (Service Locator pattern)
    lateinit var chatRepository: ChatRepository
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        val dataSource = NotificationDataSource(this)
        chatRepository = ChatRepositoryImpl(dataSource)
        settingsRepository = SettingsRepositoryImpl(this)
    }
}