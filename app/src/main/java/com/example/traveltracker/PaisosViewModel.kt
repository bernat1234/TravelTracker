package com.example.traveltracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveltracker.model.Localitzacio
import com.example.traveltracker.model.Usuari
import com.example.traveltracker.model.Viatge
import com.example.traveltracker.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class PaisosViewModel : ViewModel() {

    private val _llista = MutableStateFlow<Set<String>>(emptySet())
    val llista: StateFlow<Set<String>> = _llista.asStateFlow()

    fun carregarPaissos(usuariId: Long) {
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

                val paisosUsuari = usuari?.localitzacio_id?.let { id ->
                    localitzacions.find { it.id == id }?.pais
                }

                val paisosSet = (localitzacions
                    .mapNotNull { it.pais } + listOfNotNull(paisosUsuari))
                    .filter { it.isNotBlank() }
                    .toSet()

                _llista.value = paisosSet

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    fun paisToEmoji(pais: String): String {
        return when (pais.trim().lowercase()) {
            "spain" -> "🇪🇸"
            "france" -> "🇫🇷"
            "portugal" -> "🇵🇹"
            "united kingdom", "uk" -> "🇬🇧"
            "germany" -> "🇩🇪"
            "italy" -> "🇮🇹"
            "netherlands" -> "🇳🇱"
            "belgium" -> "🇧🇪"
            "switzerland" -> "🇨🇭"
            "sweden" -> "🇸🇪"
            "norway" -> "🇳🇴"
            "finland" -> "🇫🇮"
            "denmark" -> "🇩🇰"
            "poland" -> "🇵🇱"
            "greece" -> "🇬🇷"
            "turkey" -> "🇹🇷"
            "austria" -> "🇦🇹"
            "czech republic", "czechia" -> "🇨🇿"
            "slovakia" -> "🇸🇰"
            "hungary" -> "🇭🇺"
            "romania" -> "🇷🇴"
            "bulgaria" -> "🇧🇬"
            "croatia" -> "🇭🇷"
            "serbia" -> "🇷🇸"
            "slovenia" -> "🇸🇮"
            "bosnia and herzegovina" -> "🇧🇦"
            "north macedonia" -> "🇲🇰"
            "albania" -> "🇦🇱"
            "kosovo" -> "🇽🇰"
            "montenegro" -> "🇲🇪"
            "ireland" -> "🇮🇪"
            "iceland" -> "🇮🇸"
            "luxembourg" -> "🇱🇺"
            "malta" -> "🇲🇹"
            "cyprus" -> "🇨🇾"
            "estonia" -> "🇪🇪"
            "latvia" -> "🇱🇻"
            "lithuania" -> "🇱🇹"
            "belarus" -> "🇧🇾"
            "ukraine" -> "🇺🇦"
            "moldova" -> "🇲🇩"
            "russia" -> "🇷🇺"
            "andorra" -> "🇦🇩"
            "monaco" -> "🇲🇨"
            "san marino" -> "🇸🇲"
            "vatican", "vatican city" -> "🇻🇦"
            "liechtenstein" -> "🇱🇮"

            "united states", "usa", "us" -> "🇺🇸"
            "canada" -> "🇨🇦"
            "mexico" -> "🇲🇽"
            "argentina" -> "🇦🇷"
            "brazil" -> "🇧🇷"
            "chile" -> "🇨🇱"
            "colombia" -> "🇨🇴"
            "peru" -> "🇵🇪"
            "venezuela" -> "🇻🇪"
            "ecuador" -> "🇪🇨"
            "bolivia" -> "🇧🇴"
            "paraguay" -> "🇵🇾"
            "uruguay" -> "🇺🇾"
            "cuba" -> "🇨🇺"
            "dominican republic" -> "🇩🇴"
            "haiti" -> "🇭🇹"
            "jamaica" -> "🇯🇲"
            "puerto rico" -> "🇵🇷"
            "costa rica" -> "🇨🇷"
            "panama" -> "🇵🇦"
            "guatemala" -> "🇬🇹"
            "honduras" -> "🇭🇳"
            "el salvador" -> "🇸🇻"
            "nicaragua" -> "🇳🇮"
            "belize" -> "🇧🇿"
            "trinidad and tobago" -> "🇹🇹"
            "bahamas" -> "🇧🇸"
            "barbados" -> "🇧🇧"
            "guyana" -> "🇬🇾"
            "suriname" -> "🇸🇷"
            "grenada" -> "🇬🇩"
            "saint lucia" -> "🇱🇨"
            "saint vincent and the grenadines" -> "🇻🇨"
            "antigua and barbuda" -> "🇦🇬"
            "dominica" -> "🇩🇲"
            "saint kitts and nevis" -> "🇰🇳"

            "china" -> "🇨🇳"
            "japan" -> "🇯🇵"
            "india" -> "🇮🇳"
            "south korea", "korea" -> "🇰🇷"
            "north korea" -> "🇰🇵"
            "indonesia" -> "🇮🇩"
            "pakistan" -> "🇵🇰"
            "bangladesh" -> "🇧🇩"
            "vietnam" -> "🇻🇳"
            "thailand" -> "🇹🇭"
            "philippines" -> "🇵🇭"
            "malaysia" -> "🇲🇾"
            "singapore" -> "🇸🇬"
            "myanmar", "burma" -> "🇲🇲"
            "cambodia" -> "🇰🇭"
            "laos" -> "🇱🇦"
            "taiwan" -> "🇹🇼"
            "hong kong" -> "🇭🇰"
            "nepal" -> "🇳🇵"
            "sri lanka" -> "🇱🇰"
            "afghanistan" -> "🇦🇫"
            "iran" -> "🇮🇷"
            "iraq" -> "🇮🇶"
            "saudi arabia" -> "🇸🇦"
            "united arab emirates", "uae" -> "🇦🇪"
            "israel" -> "🇮🇱"
            "jordan" -> "🇯🇴"
            "lebanon" -> "🇱🇧"
            "syria" -> "🇸🇾"
            "kuwait" -> "🇰🇼"
            "qatar" -> "🇶🇦"
            "bahrain" -> "🇧🇭"
            "oman" -> "🇴🇲"
            "yemen" -> "🇾🇪"
            "uzbekistan" -> "🇺🇿"
            "kazakhstan" -> "🇰🇿"
            "turkmenistan" -> "🇹🇲"
            "tajikistan" -> "🇹🇯"
            "kyrgyzstan" -> "🇰🇬"
            "azerbaijan" -> "🇦🇿"
            "armenia" -> "🇦🇲"
            "georgia" -> "🇬🇪"
            "mongolia" -> "🇲🇳"
            "bhutan" -> "🇧🇹"
            "maldives" -> "🇲🇻"
            "timor-leste", "east timor" -> "🇹🇱"
            "brunei" -> "🇧🇳"
            "palestine" -> "🇵🇸"

            "morocco" -> "🇲🇦"
            "egypt" -> "🇪🇬"
            "nigeria" -> "🇳🇬"
            "south africa" -> "🇿🇦"
            "kenya" -> "🇰🇪"
            "ethiopia" -> "🇪🇹"
            "ghana" -> "🇬🇭"
            "tanzania" -> "🇹🇿"
            "uganda" -> "🇺🇬"
            "algeria" -> "🇩🇿"
            "tunisia" -> "🇹🇳"
            "libya" -> "🇱🇾"
            "sudan" -> "🇸🇩"
            "south sudan" -> "🇸🇸"
            "cameroon" -> "🇨🇲"
            "ivory coast", "cote d'ivoire" -> "🇨🇮"
            "senegal" -> "🇸🇳"
            "mali" -> "🇲🇱"
            "burkina faso" -> "🇧🇫"
            "niger" -> "🇳🇪"
            "chad" -> "🇹🇩"
            "angola" -> "🇦🇴"
            "mozambique" -> "🇲🇿"
            "zimbabwe" -> "🇿🇼"
            "zambia" -> "🇿🇲"
            "madagascar" -> "🇲🇬"
            "rwanda" -> "🇷🇼"
            "somalia" -> "🇸🇴"
            "democratic republic of the congo", "drc" -> "🇨🇩"
            "republic of the congo" -> "🇨🇬"
            "gabon" -> "🇬🇦"
            "equatorial guinea" -> "🇬🇶"
            "central african republic" -> "🇨🇫"
            "eritrea" -> "🇪🇷"
            "djibouti" -> "🇩🇯"
            "benin" -> "🇧🇯"
            "togo" -> "🇹🇬"
            "guinea" -> "🇬🇳"
            "guinea-bissau" -> "🇬🇼"
            "sierra leone" -> "🇸🇱"
            "liberia" -> "🇱🇷"
            "mauritania" -> "🇲🇷"
            "gambia" -> "🇬🇲"
            "cape verde" -> "🇨🇻"
            "sao tome and principe" -> "🇸🇹"
            "comoros" -> "🇰🇲"
            "mauritius" -> "🇲🇺"
            "seychelles" -> "🇸🇨"
            "botswana" -> "🇧🇼"
            "namibia" -> "🇳🇦"
            "lesotho" -> "🇱🇸"
            "eswatini", "swaziland" -> "🇸🇿"
            "malawi" -> "🇲🇼"
            "burundi" -> "🇧🇮"

            "australia" -> "🇦🇺"
            "new zealand" -> "🇳🇿"
            "papua new guinea" -> "🇵🇬"
            "fiji" -> "🇫🇯"
            "solomon islands" -> "🇸🇧"
            "vanuatu" -> "🇻🇺"
            "samoa" -> "🇼🇸"
            "kiribati" -> "🇰🇮"
            "tonga" -> "🇹🇴"
            "micronesia" -> "🇫🇲"
            "palau" -> "🇵🇼"
            "marshall islands" -> "🇲🇭"
            "nauru" -> "🇳🇷"
            "tuvalu" -> "🇹🇻"

            else -> "🌍"
        }
    }
}
