package com.example.focusproject

abstract class YoutubePlayerAPIConfig {
    private val apiKey = "AIzaSyDj3wbeTPmuxLgmMuxNUK09O1yOAySByvE"

    fun getApiKey(): String {
        return apiKey
    }

}