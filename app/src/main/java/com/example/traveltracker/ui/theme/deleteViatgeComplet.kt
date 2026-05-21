package com.example.traveltracker.ui.theme

import com.example.traveltracker.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

suspend fun deleteViatgeComplet(viatgeId: Long) {

    SupabaseClient.client
        .from("Viatge_Like")
        .delete {
            filter {
                eq("viatge_id", viatgeId)
            }
        }

    SupabaseClient.client
        .from("Viatge_Amic")
        .delete {
            filter {
                eq("viatge_Id", viatgeId)
            }
        }

    SupabaseClient.client
        .from("Foto")
        .delete {
            filter {
                eq("viatge_id", viatgeId)
            }
        }

    SupabaseClient.client
        .from("Notificacio")
        .delete {
            filter {
                eq("viatge_id", viatgeId)
            }
        }

    SupabaseClient.client
        .from("Viatge")
        .delete {
            filter {
                eq("id", viatgeId)
            }
        }
}