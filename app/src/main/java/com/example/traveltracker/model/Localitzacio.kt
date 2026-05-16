package com.example.traveltracker.model
import kotlinx.serialization.Serializable

@Serializable
data class Localitzacio(
    val id: Long,
    val pais: String? = "",
    val regio: String? = "",
    val ciutat: String? = "",
)