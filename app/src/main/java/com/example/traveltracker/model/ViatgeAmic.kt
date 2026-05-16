package com.example.traveltracker.model

import kotlinx.serialization.Serializable

@Serializable
data class ViatgeAmic(
    val viatge_Id: Long,
    val usuari_id: Long
)
