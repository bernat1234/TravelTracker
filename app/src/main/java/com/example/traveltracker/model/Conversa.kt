package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversa(
    val id: Long,
    val usuari1_id: Long? = null,
    val usuari2_id: Long? = null
)
