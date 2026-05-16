package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable

@Serializable
data class CheckInsertViatge(
    val usuari_Id: Long,
    val data_inici: String,
    val data_final: String,
    val puntuacio: Int,
    val frase_estrella: String,
    val descripcio: String,
    val localitzacio_id: Long
)