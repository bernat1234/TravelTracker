package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable

@Serializable
data class PaisVisitat(
    val nom: String,
    val numVisites: Int
)