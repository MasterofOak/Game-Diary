package com.masterofoak.gamediary.data

import com.masterofoak.gamediary.model.Game

object FakeGamesData {
    
    val games = listOf<Game>(
        Game(id = 1, gameName = "Game #1", imageUri = null),
        Game(id = 2, gameName = "Game #2", imageUri = null),
        Game(id = 3, gameName = "Game #3", imageUri = null),
    )
}