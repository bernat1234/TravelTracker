package com.example.traveltracker.model.visual
import com.example.traveltracker.model.Usuari


data class UsuariRanking(
    val usuari: Usuari,
    val numPaisos: Int,
    val percentatge: Float,
    val posicio: Int
)