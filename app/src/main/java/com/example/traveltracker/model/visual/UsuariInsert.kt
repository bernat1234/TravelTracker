package com.example.traveltracker.model.visual

import kotlinx.serialization.Serializable

@Serializable
data class UsuariInsert(
    val correu: String,
    val contrasenya: String,
    val foto_perfil: String? = null,
    val nom: String,
    val cognom: String,
    val data_naixament: String,
    val telefon: Long,
    val localitzacio_id: Long,
    val continents: Int? = 1,
    val paissos: Int? = 1,
    val comunitats_autonomes: Int? = 1
)