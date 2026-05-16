package com.example.traveltracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var usuariId by mutableStateOf<Long?>(null)
        private set

    fun setUser(id: Long) {
        usuariId = id
    }

    fun logout() {
        usuariId = null
    }
}