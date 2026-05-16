package com.example.traveltracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.Conversa
import com.example.traveltracker.model.Missatge2
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.visual.ConversaFeed
import com.example.traveltracker.model.visual.NouMissatge
import com.example.traveltracker.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MissatgesViewModel : ViewModel() {
    private val _converses = MutableStateFlow<List<ConversaFeed>>(emptyList())
    val converses: StateFlow<List<ConversaFeed>> = _converses.asStateFlow()
    private val _missatges = MutableStateFlow<List<Missatge2>>(emptyList())
    val missatges: StateFlow<List<Missatge2>> = _missatges.asStateFlow()
    fun carregarConverses(usuariId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val converses = SupabaseClient.client
                    .from("Conversa")
                    .select()
                    .decodeList<Conversa>()
                    .filter { it.usuari1_id == usuariId || it.usuari2_id == usuariId }

                val altresIds = converses.mapNotNull { c ->
                    if (c.usuari1_id == usuariId) c.usuari2_id else c.usuari1_id
                }.distinct()

                val usuaris = if (altresIds.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Usuari")
                        .select { filter { isIn("id", altresIds as List<Any>) } }
                        .decodeList<Usuari>()
                        .associateBy { it.id }
                } else emptyMap()

                val totsMissatges = SupabaseClient.client
                    .from("Missatges")
                    .select()
                    .decodeList<Missatge2>()

                _converses.value = converses.mapNotNull { c ->
                    val altreId = if (c.usuari1_id == usuariId) c.usuari2_id else c.usuari1_id
                    val altre = usuaris[altreId] ?: return@mapNotNull null
                    val ultimMissatge = totsMissatges
                        .filter { it.conversa_id == c.id }
                        .maxByOrNull { it.datahora ?: "" }
                    ConversaFeed(conversa = c, usuari = altre, ultimMissatge = ultimMissatge)
                }.sortedByDescending { it.ultimMissatge?.datahora }

            } catch (e: Exception) { e.printStackTrace() }
        }
    }


    fun carregarMissatges(conversaId: Long, usuariActualId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val llista = SupabaseClient.client
                    .from("Missatges")
                    .select {
                        filter { eq("conversa_id", conversaId) }
                        order("datahora", Order.ASCENDING)
                    }
                    .decodeList<Missatge2>()
                    .map { it.copy(esPropi = it.usuari_id == usuariActualId) }

                _missatges.value = llista
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun enviarMissatge(conversaId: Long, text: String, usuariId: Long) {
        if (text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nou = NouMissatge(
                    conversa_id = conversaId,
                    usuari_id = usuariId,
                    text = text,
                    datahora = java.time.LocalDateTime.now().toString()
                )
                SupabaseClient.client.from("Missatges").insert(nou)
                carregarMissatges(conversaId, usuariId)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
    fun obtenirOCrearConversaINavegar(usuariActualId: Long, altreUsuariId: Long, onConversaObtinguda: (Long) -> Unit) {
        if (usuariActualId == altreUsuariId) return
        viewModelScope.launch(Dispatchers.IO) {
            val conversaId = obtenirOCrearConversa(usuariActualId, altreUsuariId)
            conversaId?.let {
                kotlinx.coroutines.withContext(Dispatchers.Main) {
                    onConversaObtinguda(it)
                }
            }
        }
    }
    suspend fun obtenirOCrearConversa(usuariActualId: Long, altreUsuariId: Long): Long? {
        return try {
            val u1 = minOf(usuariActualId, altreUsuariId)
            val u2 = maxOf(usuariActualId, altreUsuariId)

            val existent = SupabaseClient.client
                .from("Conversa")
                .select {
                    filter {
                        eq("usuari1_id", u1)
                        eq("usuari2_id", u2)
                    }
                }
                .decodeList<Conversa>()
                .firstOrNull()

            if (existent != null) {
                existent.id
            } else {
                SupabaseClient.client
                    .from("Conversa")
                    .insert(mapOf("usuari1_id" to u1, "usuari2_id" to u2))
                SupabaseClient.client
                    .from("Conversa")
                    .select {
                        filter {
                            eq("usuari1_id", u1)
                            eq("usuari2_id", u2)
                        }
                    }
                    .decodeList<Conversa>()
                    .firstOrNull()?.id
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}