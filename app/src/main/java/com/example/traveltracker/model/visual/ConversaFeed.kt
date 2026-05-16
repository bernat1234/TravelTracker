package com.example.traveltracker.model.visual

import com.example.traveltracker.model.Conversa
import com.example.traveltracker.model.Missatge2
import com.example.traveltracker.model.Usuari
import kotlinx.serialization.Serializable

@Serializable
data class ConversaFeed(
    val conversa: Conversa,
    val usuari: Usuari,
    val ultimMissatge: Missatge2?
)