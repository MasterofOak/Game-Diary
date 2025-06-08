package com.masterofoak.gamediary

import android.app.Application
import androidx.core.content.FileProvider
import com.masterofoak.gamediary.database.DBContainer

class GameDiaryApplication : Application() {
    
    lateinit var container: DBContainer
    override fun onCreate() {
        super.onCreate()
        container = DBContainer(this)
    }
}

class MyFileProvider() : FileProvider()
