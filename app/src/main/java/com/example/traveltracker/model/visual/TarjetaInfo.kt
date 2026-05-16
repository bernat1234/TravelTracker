package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable

@Serializable
data class TarjetaInfo(
    val lloc: String,
    val persones: String,
    val dates: String
)
