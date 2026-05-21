package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge

@Serializable
data class NotificacioData(
    val id: Long,
    val usuari_id: Long,
    val viatge_id: Long?,
    val tipus: String,
    val datahora: String,
    val origen_usuari_id: Long?,
    @Transient
    val usuariOrigen: Usuari? = null,
    @Transient
    val viatge: Viatge? = null,
    @Transient
    val fotoViatge: String? = null
)