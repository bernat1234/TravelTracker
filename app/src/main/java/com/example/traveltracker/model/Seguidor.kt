package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Seguidor(
    val usuari_seguidor_id: Long,
    val usuari_seguit_id: Long
)
