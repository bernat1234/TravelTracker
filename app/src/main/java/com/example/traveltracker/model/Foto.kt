package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class Foto(
    val id: Long,
    val viatge_id: Long,
    val path: String? = null
)