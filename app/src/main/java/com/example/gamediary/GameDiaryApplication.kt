package com.example.gamediary

import android.app.Application
import com.example.gamediary.database.DBContainer

class GameDiaryApplication : Application() {
    
    lateinit var container: DBContainer
    override fun onCreate() {
        super.onCreate()
        container = DBContainer(this)
    }
}