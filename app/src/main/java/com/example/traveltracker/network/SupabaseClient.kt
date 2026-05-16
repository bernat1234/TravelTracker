package com.example.traveltracker.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://oporkaroyytsfatiydqp.supabase.co",
        supabaseKey = "sb_publishable_x6Lp_GopJ-tDK9YPI1kofA_ABmLGDyf"
    ) {
        install(Postgrest)
    }
}

