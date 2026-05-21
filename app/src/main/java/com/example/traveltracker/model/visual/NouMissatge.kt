package com.example.traveltracker.model.visual
import kotlinx.serialization.Serializable


@Serializable
data class NouMissatge(
    val conversa_id: Long,
    val usuari_id: Long,
    val text: String,
    val datahora: String
)