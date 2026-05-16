package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Missatge2(
    val id: Long = 0,
    val conversa_id: Long,
    val usuari_id: Long? = null,
    val esPropi: Boolean? = null,
    val text: String? = null,
    val datahora: String? = null
)
