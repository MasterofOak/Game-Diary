package com.example.gamediary.ui.navigation

import kotlinx.serialization.Serializable

object NavigationGraphs {
    @Serializable
    object GamesGraph
    
    @Serializable
    object SearchGraph
    
    @Serializable
    object FeedGraph
}

object NavigationDestinations {
    @Serializable
    object GamesScreen
    
    @Serializable
    object GameDetail
    
    @Serializable
    object SearchScreen
    
    @Serializable
    object FeedScreen
}