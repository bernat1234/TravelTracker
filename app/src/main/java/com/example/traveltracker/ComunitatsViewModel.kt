package com.example.traveltracker
import com.example.traveltracker.network.SupabaseClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.Viatge
import com.example.traveltracker.model.Localitzacio
import com.example.traveltracker.model.Usuari
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.from



class ComunitatsViewModel : ViewModel() {

    private val _llista = MutableStateFlow<Set<String>>(emptySet())
    val llista: StateFlow<Set<String>> = _llista.asStateFlow()

    fun carregarComunitats(usuariId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val usuari = SupabaseClient.client
                    .from("Usuari")
                    .select { filter { eq("id", usuariId) } }
                    .decodeSingleOrNull<Usuari>()
                val viatges = SupabaseClient.client
                    .from("Viatge")
                    .select { filter { eq("usuari_Id", usuariId) } }
                    .decodeList<Viatge>()

                val ids = (viatges.map { it.localitzacio_id } +
                        listOfNotNull(usuari?.localitzacio_id))
                    .distinct()

                val localitzacions = if (ids.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Localitzacio")
                        .select { filter { isIn("id", ids) } }
                        .decodeList<Localitzacio>()
                } else emptyList()

                val regioUsuari = usuari?.localitzacio_id?.let { id ->
                    localitzacions.find { it.id == id }?.regio
                }

                val regionsSet = (localitzacions
                    .mapNotNull { it.regio } + listOfNotNull(regioUsuari))
                    .filter { it.isNotBlank() }
                    .toSet()

                _llista.value = regionsSet

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}


