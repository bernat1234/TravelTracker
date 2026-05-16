package com.example.traveltracker.model.visual

import com.example.traveltracker.model.Localitzacio
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge
import kotlinx.serialization.Serializable

@Serializable
data class ViatgeFeed(
    val viatge: Viatge,
    val usuari: Usuari,
    val localitzacio: Localitzacio?,
    val numPersones: Int,
    val primeraFoto: String? = null
)