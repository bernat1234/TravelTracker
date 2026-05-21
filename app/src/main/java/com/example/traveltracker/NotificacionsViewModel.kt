package com.example.traveltracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.visual.NotificacioData
import com.example.traveltracker.network.SupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.traveltracker.model.Seguidor
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge
import com.example.traveltracker.model.Foto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order


class NotificacionsViewModel : ViewModel() {

    private val _nouViatge = MutableStateFlow<List<NotificacioData>>(emptyList())
    val nouViatge: StateFlow<List<NotificacioData>> = _nouViatge.asStateFlow()

    private val _seguidors = MutableStateFlow<List<NotificacioData>>(emptyList())
    val seguidors: StateFlow<List<NotificacioData>> = _seguidors.asStateFlow()

    private val _likes = MutableStateFlow<List<NotificacioData>>(emptyList())
    val likes: StateFlow<List<NotificacioData>> = _likes.asStateFlow()

    fun carregarNotificacions(usuariId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notificacions = SupabaseClient.client
                    .from("Notificacio")
                    .select {
                        filter { eq("usuari_id", usuariId) }
                        order("datahora", Order.DESCENDING)
                    }
                    .decodeList<NotificacioData>()

                val origenIds = notificacions.mapNotNull { it.origen_usuari_id }.distinct()

                val usuarisOrigen = if (origenIds.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Usuari")
                        .select {
                            filter {
                                isIn("id", origenIds)
                            }
                        }
                        .decodeList<Usuari>()
                        .associateBy { it.id }
                } else emptyMap()

                val viatgeIds = notificacions
                    .mapNotNull { it.viatge_id }
                    .distinct()

                val totsViatges = if (viatgeIds.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Viatge")
                        .select {
                            filter {
                                isIn("id", viatgeIds)
                            }
                        }
                        .decodeList<Viatge>()
                        .associateBy { it.id }
                } else emptyMap()

                val fotos = if (viatgeIds.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Foto")
                        .select {
                            filter {
                                isIn("viatge_id", viatgeIds)
                            }
                        }
                        .decodeList<Foto>()
                        .groupBy { it.viatge_id }
                        .mapValues { it.value.firstOrNull()?.path }
                } else emptyMap()

                val notificacionsAmbDades = notificacions.map { n ->
                    n.copy(
                        usuariOrigen = usuarisOrigen[n.origen_usuari_id],
                        viatge = totsViatges[n.viatge_id],
                        fotoViatge = fotos[n.viatge_id]
                    )
                }

                _nouViatge.value = notificacionsAmbDades.filter { it.tipus == "nou_viatge" }
                _seguidors.value = notificacionsAmbDades.filter { it.tipus == "nou_seguidor" }
                _likes.value = notificacionsAmbDades.filter { it.tipus == "like" }

            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}