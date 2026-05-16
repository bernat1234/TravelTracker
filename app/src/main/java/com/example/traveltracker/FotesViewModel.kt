package com.example.traveltracker

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FotesViewModel : ViewModel() {


    fun pujarFotoPerfil(usuariId: Long, uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bytes = context.contentResolver
                    .openInputStream(uri)?.readBytes() ?: return@launch

                val nomFitxer = "perfil_$usuariId.jpg"

                SupabaseClient.client.storage
                    .from("fotos-perfil")
                    .upload(nomFitxer, bytes) { upsert = true }
                val url = SupabaseClient.client.storage
                    .from("fotos-perfil")
                    .publicUrl(nomFitxer)

                SupabaseClient.client.from("Usuari")
                    .update({ set("foto_perfil", url) }) {
                        filter { eq("id", usuariId) }
                    }

            } catch (e: Exception) { e.printStackTrace() }
        }
    }


    fun pujarFotoViatge(viatgeId: Long, uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bytes = context.contentResolver
                    .openInputStream(uri)?.readBytes() ?: return@launch

                val nomFitxer = "viatge_${viatgeId}_${System.currentTimeMillis()}.jpg"

                SupabaseClient.client.storage
                    .from("fotos-viatges")
                    .upload(nomFitxer, bytes) { upsert = true }
                val url = SupabaseClient.client.storage
                    .from("fotos-viatges")
                    .publicUrl(nomFitxer)

                // Guarda la URL a la taula Foto
                SupabaseClient.client.from("Foto")
                    .insert(mapOf("viatge_id" to viatgeId, "path" to url))

            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}