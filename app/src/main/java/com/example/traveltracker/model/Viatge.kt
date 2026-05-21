package com.example.traveltracker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Viatge(
    val id: Long,
    val created_at: String,
    val usuari_Id: Long? = null,
    val data_inici: String? = null,
    val data_final: String? = null,
    val puntuacio: Int? = null,
    val frase_estrella: String? = null,
    val descripcio: String? = null,
    val localitzacio_id: Long
)
