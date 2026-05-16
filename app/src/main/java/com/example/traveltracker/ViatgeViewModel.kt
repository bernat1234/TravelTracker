package com.example.traveltracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViatgeViewModel : ViewModel() {
    var viatge_Id by mutableStateOf<Long?>(null)

    fun setViatge(id: Long) {
        viatge_Id = id
    }


}