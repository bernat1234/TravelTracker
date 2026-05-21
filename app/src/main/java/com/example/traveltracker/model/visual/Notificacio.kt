package com.example.traveltracker.model.visual

data class Notificacio(
    val idUsuari: Long?,
    val nomUsuari: String,
    val missatge: String,
    val tenimaImatge: Boolean = false,
    val fotoPerfil: String? = null,
    val fotoViatge: String? = null
)
