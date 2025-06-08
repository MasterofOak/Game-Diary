package com.masterofoak.gamediary

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.masterofoak.gamediary.ui.viewmodel.AddGameViewModel
import com.masterofoak.gamediary.ui.viewmodel.GamesViewModel
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel

object GlobalViewModelProvider {
    
    val Factory = viewModelFactory {
        initializer {
            GamesViewModel(gameDiaryApplication().container.gamesDBRepository)
        }
        initializer {
            AddGameViewModel(gameDiaryApplication().container.gamesDBRepository)
        }
        initializer {
            UserRecordViewModel(gameDiaryApplication().container.gamesDBRepository)
        }
    }
}

fun CreationExtras.gameDiaryApplication(): GameDiaryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as GameDiaryApplication)