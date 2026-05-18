package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable


@Serializable
data class NovaFoto(
    val viatge_id: Long,
    val path: String
)