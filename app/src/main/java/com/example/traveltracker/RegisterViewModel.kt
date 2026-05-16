package com.example.traveltracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.traveltracker.model.visual.UsuariInsert

class RegisterViewModel : ViewModel() {

    var usuari by mutableStateOf(
        UsuariInsert(
            correu = "",
            contrasenya = "",
            nom = "",
            cognom = "",
            data_naixament = "",
            telefon = 0,
            localitzacio_id = 0,
            foto_perfil = null,
            continents = 0,
            paissos = 0,
            comunitats_autonomes = 0
        )
    )

}