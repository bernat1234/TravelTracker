package com.example.traveltracker

import com.example.traveltracker.network.SupabaseClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.Foto
import com.example.traveltracker.model.Localitzacio
import com.example.traveltracker.model.Seguidor
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge
import com.example.traveltracker.model.ViatgeAmic
import com.example.traveltracker.model.visual.ViatgeFeed
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SearchMode { AMICS, PAISOS, USUARIS }

class PrincipalViewModel : ViewModel() {

    private val _feed = MutableStateFlow<List<ViatgeFeed>>(emptyList())
    val feed: StateFlow<List<ViatgeFeed>> = _feed.asStateFlow()

    private val _usuarisResultat = MutableStateFlow<List<Usuari>>(emptyList())
    val usuarisResultat: StateFlow<List<Usuari>> = _usuarisResultat.asStateFlow()

    private val _viatgesResultat = MutableStateFlow<List<ViatgeFeed>>(emptyList())
    val viatgesResultat: StateFlow<List<ViatgeFeed>> = _viatgesResultat.asStateFlow()


    fun carregarFeed(userId: Long, amics: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val amicsIds = obtenirAmicsIds(userId)

                val totsViatges = SupabaseClient.client
                    .from("Viatge").select().decodeList<Viatge>()

                val viatgesFiltrats = if (amics) {
                    totsViatges.filter { v -> v.usuari_Id in amicsIds }
                } else {
                    totsViatges.filter { v -> v.usuari_Id != userId && v.usuari_Id !in amicsIds }
                }.sortedByDescending { it.created_at }

                _feed.value = construirFeed(viatgesFiltrats)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }


    fun cercarUsuaris(query: String) {
        if (query.isBlank()) { _usuarisResultat.value = emptyList(); return }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tots = SupabaseClient.client
                    .from("Usuari").select().decodeList<Usuari>()
                _usuarisResultat.value = tots.filter { u ->
                    val nomComplet = "${u.nom} ${u.cognom}".lowercase()
                    nomComplet.contains(query.lowercase())
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun cercarPerLloc(query: String) {
        if (query.isBlank()) { _viatgesResultat.value = emptyList(); return }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val q = query.trim().lowercase()

                val locIds = SupabaseClient.client
                    .from("Localitzacio")
                    .select {
                        filter {
                            or {
                                ilike("pais", "%$q%")
                                ilike("regio", "%$q%")
                                ilike("ciutat", "%$q%")
                            }
                        }
                    }
                    .decodeList<Localitzacio>()
                    .map { it.id }

                if (locIds.isEmpty()) {
                    _viatgesResultat.value = emptyList()
                    return@launch
                }

                val viatges = SupabaseClient.client
                    .from("Viatge")
                    .select {
                        filter {
                            isIn("localitzacio_id", locIds as List<Any>)
                        }
                    }
                    .decodeList<Viatge>()

                _viatgesResultat.value = construirFeed(viatges)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private suspend fun obtenirAmicsIds(userId: Long): List<Long> {
        return SupabaseClient.client
            .from("Seguidor")
            .select { filter { eq("usuari_seguidor_id", userId) } }
            .decodeList<Seguidor>()
            .map { it.usuari_seguit_id }
    }

    private suspend fun construirFeed(viatgesFiltrats: List<Viatge>): List<ViatgeFeed> {
        val usuarisIds = viatgesFiltrats.mapNotNull { it.usuari_Id }.distinct()
        val usuaris = if (usuarisIds.isNotEmpty()) {
            SupabaseClient.client.from("Usuari")
                .select { filter { isIn("id", usuarisIds as List<Any>) } }
                .decodeList<Usuari>()
        } else emptyList()

        val localitzacioIds = viatgesFiltrats.map { it.localitzacio_id }.distinct()
        val localitzacions = if (localitzacioIds.isNotEmpty()) {
            SupabaseClient.client.from("Localitzacio")
                .select { filter { isIn("id", localitzacioIds as List<Any>) } }
                .decodeList<Localitzacio>().associateBy { it.id }
        } else emptyMap()

        val viatgeAmics = SupabaseClient.client
            .from("Viatge_Amic").select().decodeList<ViatgeAmic>()

        val totesFotos = SupabaseClient.client
            .from("Foto")
            .select()
            .decodeList<Foto>()

        return viatgesFiltrats.mapNotNull { v ->
            val usuari = usuaris.find { it.id == v.usuari_Id } ?: return@mapNotNull null
            val primeraFoto = totesFotos.firstOrNull { it.viatge_id == v.id }?.path

            ViatgeFeed(
                viatge = v,
                usuari = usuari,
                localitzacio = v.localitzacio_id.let { localitzacions[it] },
                numPersones = viatgeAmics.count { it.viatge_Id == v.id } + 1,
                primeraFoto = primeraFoto
            )
        }

    }
}