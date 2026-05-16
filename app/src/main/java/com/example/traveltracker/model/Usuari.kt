package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuari(
    val id: Long,
    val correu: String,
    val contrasenya: String,
    val foto_perfil: String?,
    val nom: String,
    val cognom: String,
    val data_naixament: String,
    val telefon: Long,
    val localitzacio_id: Long,
    val continents: Int?,
    val paissos: Int?,
    val comunitats_autonomes: Int?
)