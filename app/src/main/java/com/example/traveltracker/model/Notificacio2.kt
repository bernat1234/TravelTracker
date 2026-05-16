package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Notificacio2(
    val id: Long,
    val usuari_id: Long,
    val viatge_id: Long,
    val tipus: String? = null,
    val datahora: String? = null
)
