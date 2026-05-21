package com.example.traveltracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.Localitzacio
import com.example.traveltracker.model.Seguidor
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge
import com.example.traveltracker.model.visual.PaisVisitat
import com.example.traveltracker.model.visual.UsuariRanking
import com.example.traveltracker.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EstadistiquesViewModel : ViewModel() {

    private val _rankingMundial = MutableStateFlow<List<UsuariRanking>>(emptyList())
    val rankingMundial: StateFlow<List<UsuariRanking>> = _rankingMundial.asStateFlow()

    private val _rankingAmics = MutableStateFlow<List<UsuariRanking>>(emptyList())
    val rankingAmics: StateFlow<List<UsuariRanking>> = _rankingAmics.asStateFlow()

    private val _posicioUsuariMundial = MutableStateFlow<UsuariRanking?>(null)
    val posicioUsuariMundial: StateFlow<UsuariRanking?> = _posicioUsuariMundial.asStateFlow()

    private val _posicioUsuariAmics = MutableStateFlow<UsuariRanking?>(null)
    val posicioUsuariAmics: StateFlow<UsuariRanking?> = _posicioUsuariAmics.asStateFlow()

    private val _paisMesVisitat = MutableStateFlow<PaisVisitat?>(null)
    val paisMesVisitat: StateFlow<PaisVisitat?> = _paisMesVisitat.asStateFlow()

    private val _paisMesVistatMes = MutableStateFlow<PaisVisitat?>(null)
    val paisMesVistatMes: StateFlow<PaisVisitat?> = _paisMesVistatMes.asStateFlow()

    private val _paisMesVistatAmics = MutableStateFlow<PaisVisitat?>(null)
    val paisMesVistatAmics: StateFlow<PaisVisitat?> = _paisMesVistatAmics.asStateFlow()

    private val _paisosAmics = MutableStateFlow<List<String>>(emptyList())
    val paisosAmics: StateFlow<List<String>> = _paisosAmics.asStateFlow()

    private val TOTAL_PAISOS = 195f

    @RequiresApi(Build.VERSION_CODES.O)
    fun carregarEstadistiques(usuariId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totsUsuaris = SupabaseClient.client
                    .from("Usuari").select().decodeList<Usuari>()

                val totsViatges = SupabaseClient.client
                    .from("Viatge").select().decodeList<Viatge>()

                val ara = java.time.LocalDate.now()
                val locIdsUnics = totsViatges.mapNotNull { it.localitzacio_id }.distinct()

                val localitzacionsViatges = if (locIdsUnics.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Localitzacio")
                        .select { filter { isIn("id", locIdsUnics as List<Any>) } }
                        .decodeList<Localitzacio>()
                        .associateBy { it.id }
                } else emptyMap()

                val rankingComplet = totsUsuaris
                    .sortedByDescending { it.paissos ?: 0 }
                    .mapIndexed { i, usuari ->
                        val num = usuari.paissos?.toInt() ?: 0
                        val pct = (num / TOTAL_PAISOS) * 100f
                        UsuariRanking(usuari = usuari, numPaisos = num, percentatge = pct, posicio = i + 1)
                    }

                _rankingMundial.value = rankingComplet.take(5)
                _posicioUsuariMundial.value = rankingComplet.find { it.usuari.id == usuariId }

                val paisMesVistatEntry = totsViatges
                    .mapNotNull { localitzacionsViatges[it.localitzacio_id]?.pais }
                    .filter { it.isNotBlank() }
                    .groupingBy { it }.eachCount()
                    .maxByOrNull { it.value }
                _paisMesVisitat.value = paisMesVistatEntry?.let { PaisVisitat(it.key, it.value) }

                val paisMesDelMesEntry = totsViatges
                    .filter { v ->
                        v.data_inici?.let {
                            try {
                                val d = java.time.LocalDate.parse(it)
                                d.year == ara.year && d.monthValue == ara.monthValue
                            } catch (e: Exception) { false }
                        } ?: false
                    }
                    .mapNotNull { localitzacionsViatges[it.localitzacio_id]?.pais }
                    .filter { it.isNotBlank() }
                    .groupingBy { it }.eachCount()
                    .maxByOrNull { it.value }
                _paisMesVistatMes.value = paisMesDelMesEntry?.let { PaisVisitat(it.key, it.value) }

                val seguits = SupabaseClient.client
                    .from("Seguidor")
                    .select { filter { eq("usuari_seguidor_id", usuariId) } }
                    .decodeList<Seguidor>()
                    .map { it.usuari_seguit_id }

                val rankingAmicsComplet = totsUsuaris
                    .filter { it.id in seguits }
                    .sortedByDescending { it.paissos ?: 0 }
                    .mapIndexed { i, usuari ->
                        val num = usuari.paissos?.toInt() ?: 0
                        val pct = (num / TOTAL_PAISOS) * 100f
                        UsuariRanking(usuari = usuari, numPaisos = num, percentatge = pct, posicio = i + 1)
                    }

                _rankingAmics.value = rankingAmicsComplet.take(5)
                _posicioUsuariAmics.value = rankingAmicsComplet.find { it.usuari.id == usuariId }

                val viatgesAmics = totsViatges.filter { it.usuari_Id in seguits }

                val paisAmicsEntry = viatgesAmics
                    .mapNotNull { localitzacionsViatges[it.localitzacio_id]?.pais }
                    .filter { it.isNotBlank() }
                    .groupingBy { it }.eachCount()
                    .maxByOrNull { it.value }
                _paisMesVistatAmics.value = paisAmicsEntry?.let { PaisVisitat(it.key, it.value) }

                _paisosAmics.value = viatgesAmics
                    .mapNotNull { localitzacionsViatges[it.localitzacio_id]?.pais }
                    .filter { it.isNotBlank() }
                    .distinct()

            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}