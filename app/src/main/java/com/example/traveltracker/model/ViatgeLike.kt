package com.example.traveltracker.model
import kotlinx.serialization.Serializable

@Serializable
data class ViatgeLike(
    val viatge_id: Long,
    val usuari_id: Long
)
