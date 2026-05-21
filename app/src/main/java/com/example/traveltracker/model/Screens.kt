package com.example.traveltracker.model
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color.rgb
import android.net.Uri
import android.os.Build
import io.github.jan.supabase.postgrest.rpc
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.traveltracker.network.SupabaseClient
import coil.compose.AsyncImage
import org.maplibre.android.style.layers.LineLayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression.color
import org.maplibre.android.style.expressions.Expression.get
import org.maplibre.android.style.expressions.Expression.literal
import org.maplibre.android.style.expressions.Expression.match
import org.maplibre.android.style.layers.BackgroundLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.TransitionOptions
import org.maplibre.android.style.sources.GeoJsonSource
import java.util.Calendar
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.example.traveltracker.ComunitatsViewModel
import com.example.traveltracker.EstadistiquesViewModel
import com.example.traveltracker.MissatgesViewModel
import com.example.traveltracker.NotificacionsViewModel
import com.example.traveltracker.PaisosViewModel
import com.example.traveltracker.PrincipalViewModel
import com.example.traveltracker.R
import com.example.traveltracker.RegisterViewModel
import com.example.traveltracker.SearchMode
import com.example.traveltracker.UserViewModel
import com.example.traveltracker.ViatgeViewModel
import com.example.traveltracker.model.visual.CheckInsertViatge
import com.example.traveltracker.model.visual.ConversaFeed
import com.example.traveltracker.model.visual.Notificacio
import com.example.traveltracker.model.visual.NovaFoto
import com.example.traveltracker.model.visual.PaisResponse
import com.example.traveltracker.model.visual.PaisVisitat
import com.example.traveltracker.model.visual.TarjetaInfo
import com.example.traveltracker.model.visual.UsuariRanking
import com.example.traveltracker.model.visual.ViatgeFeed
import com.example.traveltracker.ui.theme.deleteViatgeComplet
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



enum class Screens() {
    Pantalla_Principal,
    Pantalla_Estadistiques,
    Pantalla_Missatges,
    Pantalla_Perfil,
    Pantalla_Afegir,
    Pantalla_Chat,
    Pantalla_Notificacions,
    Pantalla_TermesCondicions,
    Pantalla_Configuracio,
    Pantalla_NotificacionsConfig,
    Pantalla_Audio,
    Pantalla_Privacitat,
    Pantalla_Cookies,
    Pantalla_Login,
    Pantalla_Registre,
    Pantalla_Viatge,
    Pantalla_Perfil_Estadistica,
    Pantalla_Perfil_Extern,
    Pantalla_Perfil_Estadistica_Extern,
    Pantalla_Crear_Perfil,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla_Principal(navController: NavController, usuariId: UserViewModel, principalViewModel: PrincipalViewModel) {
    val amicsSeleccionat = remember { mutableStateOf(true) }
    val searchQuery = remember { mutableStateOf("") }
    val searchMode = remember { mutableStateOf(SearchMode.AMICS) }
    val feed by principalViewModel.feed.collectAsState()
    val usuarisResultat by principalViewModel.usuarisResultat.collectAsState()
    val viatgesResultat by principalViewModel.viatgesResultat.collectAsState()

    LaunchedEffect(amicsSeleccionat.value) {
        usuariId.usuariId?.let { id ->
            principalViewModel.carregarFeed(id, amicsSeleccionat.value)
        }
    }

    LaunchedEffect(searchQuery.value, searchMode.value) {
        val q = searchQuery.value
        when (searchMode.value) {
            SearchMode.USUARIS -> principalViewModel.cercarUsuaris(q)
            SearchMode.PAISOS  -> principalViewModel.cercarPerLloc(q)
            SearchMode.AMICS   -> { }
        }
    }

    val llistaActiva: List<ViatgeFeed> = when {searchQuery.value.isBlank() -> feed
        searchMode.value == SearchMode.PAISOS -> viatgesResultat
        else -> feed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF2F2F2))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.buscar),
                    contentDescription = "Cerca",
                    tint = Color(0xFFB5E550),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchQuery.value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.buscar_aqui),
                                    color = Color.LightGray,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                if (searchQuery.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Netejar",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { searchQuery.value = "" }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF0F0F0))
                    .padding(4.dp)
            ) {
                listOf(true to stringResource(R.string.amics), false to stringResource(R.string.viatgers))
                    .forEach { (valor, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (searchMode.value == SearchMode.AMICS && amicsSeleccionat.value == valor) Color(
                                        0xFFB5E550
                                    ) else Color.Transparent
                                )
                                .clickable {
                                    searchMode.value = SearchMode.AMICS
                                    amicsSeleccionat.value = valor
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (searchMode.value == SearchMode.AMICS && amicsSeleccionat.value == valor) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (searchMode.value == SearchMode.AMICS && amicsSeleccionat.value == valor) Color.White else Color.Gray
                            )
                        }
                    }
            }

            listOf(
                SearchMode.USUARIS to Pair(R.drawable.persones, "Usuaris"),
                SearchMode.PAISOS  to Pair(R.drawable.logo, "Països")
            ).forEach { (mode, info) ->
                val (iconRes, label) = info
                val selected = searchMode.value == mode
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (selected) Color(0xFFB5E550) else Color(0xFFF0F0F0))
                        .clickable { searchMode.value = mode }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = if (selected) Color.White else Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) Color.White else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchMode.value == SearchMode.USUARIS && searchQuery.value.isNotBlank()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (usuarisResultat.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_s_ha_trobat_cap_usuari), color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                } else {
                    items(usuarisResultat) { usuari ->
                        TarjetaUsuari(navController = navController, usuari = usuari)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (searchQuery.value.isNotBlank() && searchMode.value == SearchMode.PAISOS && viatgesResultat.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No s'han trobat viatges", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                } else {
                    items(llistaActiva) { item ->
                        TarjetaViatge(
                            navController = navController,
                            id = item.viatge.id,
                            nom = "${item.usuari.nom} ${item.usuari.cognom}",
                            ubicacio = buildString { append(item.localitzacio?.ciutat ?: "")
                                if (!item.localitzacio?.pais.isNullOrBlank())
                                    append(", ${item.localitzacio.pais}")
                            },
                            persones = item.numPersones,
                            dies = "${item.viatge.data_inici} - ${item.viatge.data_final}",
                            valoracio = item.viatge.puntuacio!!,
                            fotoPerfil = item.usuari.foto_perfil,
                            fotoViatge = item.primeraFoto
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.Pantalla_Afegir.name) },
                shape = CircleShape,
                containerColor = Color(0xFFB5E550),
                modifier = Modifier
                    .size(56.dp)
                    .zIndex(10f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.afegir),
                    contentDescription = "Afegir viatge",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun TarjetaUsuari(navController: NavController, usuari: Usuari) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${usuari.id}") }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            if (!usuari.foto_perfil.isNullOrBlank()) {
                AsyncImage(
                    model = usuari.foto_perfil,
                    contentDescription = "Foto perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = usuari.nom.firstOrNull()?.uppercase() ?: "?",
                    color = Color(0xFF4CAF50),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "${usuari.nom} ${usuari.cognom}".trim(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.Black
            )
            if (usuari.correu.isNotBlank()) {
                Text(
                    text = usuari.correu,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.opcions),
            contentDescription = null,
            tint = Color(0xFFB5E550),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TarjetaViatge(navController: NavController, id: Long, nom: String, ubicacio: String, persones: Int, dies: String, valoracio: Int, fotoPerfil: String?,fotoViatge: String? ) {
    Card(
        onClick = { navController.navigate("${Screens.Pantalla_Viatge.name}/${id}") },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 0.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCCCCCC)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!fotoPerfil.isNullOrBlank()) {
                            AsyncImage(
                                model = fotoPerfil,
                                contentDescription = "Foto perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = nom.firstOrNull()?.uppercase() ?: "?",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = nom,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Text(
                            text = ubicacio,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Image(
                        painter = painterResource(id = R.drawable.opcions),
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDDDDDD))
                ) {
                    if (!fotoViatge.isNullOrBlank()) {
                        AsyncImage(
                            model = fotoViatge,
                            contentDescription = "Foto viatge",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(30.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.persones),
                            contentDescription = "Persones",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "$persones", fontSize = 14.sp, color = Color.DarkGray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendari),
                            contentDescription = "Dies",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = dies, fontSize = 14.sp, color = Color.DarkGray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.estrella),
                            contentDescription = "Valoració",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = valoracio.toString(), fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla_Afegir(navController: NavController, usuariId: UserViewModel, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataAnada = remember { mutableStateOf("") }
    val dataTornada = remember { mutableStateOf("") }
    val frase = remember { mutableStateOf("") }
    val descripcio = remember { mutableStateOf("") }
    val puntuacio = remember { mutableIntStateOf(0) }
    val amics = remember { mutableStateOf<List<Usuari>>(emptyList()) }
    val amicsSeleccionats = remember { mutableStateOf(setOf<Long>()) }
    val expandedAmics = remember { mutableStateOf(false) }
    val paisos = remember { mutableStateOf<List<String>>(emptyList()) }
    val regions = remember { mutableStateOf<List<String>>(emptyList()) }
    val ciutats = remember { mutableStateOf<List<String>>(emptyList()) }
    val paisSeleccionat = remember { mutableStateOf("") }
    val regioSeleccionada = remember { mutableStateOf("") }
    val ciutatSeleccionada = remember { mutableStateOf("") }
    val expandedPais = remember { mutableStateOf(false) }
    val expandedRegio = remember { mutableStateOf(false) }
    val expandedCiutat = remember { mutableStateOf(false) }
    val estat = remember { mutableStateOf<String?>(null) }
    val fotesUris = remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        fotesUris.value = (fotesUris.value + uris).distinct()
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val result = SupabaseClient.client.postgrest
                .rpc(
                    function = "get_paises",
                    parameters = Unit
                )
                .decodeList<PaisResponse>()

            paisos.value = result.map { it.pais }
        }
    }

    LaunchedEffect(paisSeleccionat.value) {
        if (paisSeleccionat.value.isNotBlank()) {
            regioSeleccionada.value = ""
            ciutatSeleccionada.value = ""
            regions.value = emptyList()
            ciutats.value = emptyList()
            withContext(Dispatchers.IO) {
                val localitzacions = SupabaseClient.client
                    .from("Localitzacio")
                    .select {
                        filter { eq("pais", paisSeleccionat.value) }
                    }
                    .decodeList<Localitzacio>()
                regions.value = localitzacions
                    .mapNotNull { it.regio }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
    }

    LaunchedEffect(regioSeleccionada.value) {
        if (regioSeleccionada.value.isNotBlank()) {
            ciutatSeleccionada.value = ""
            ciutats.value = emptyList()
            withContext(Dispatchers.IO) {
                val localitzacions = SupabaseClient.client
                    .from("Localitzacio")
                    .select {
                        filter {
                            eq("pais", paisSeleccionat.value)
                            eq("regio", regioSeleccionada.value)
                        }
                    }
                    .decodeList<Localitzacio>()
                ciutats.value = localitzacions
                    .mapNotNull { it.ciutat }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
    }


    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val seguidors = SupabaseClient.client
                .from("Seguidor")
                .select {
                    filter {
                        eq("usuari_seguidor_id", usuariId.usuariId!!)
                    }
                }
                .decodeList<Seguidor>()

            val idsAmics = seguidors.map { it.usuari_seguit_id }
            if (idsAmics.isNotEmpty()) {
                val usuaris = SupabaseClient.client
                    .from("Usuari")
                    .select {
                        filter {
                            isIn("id", idsAmics)
                        }
                    }
                    .decodeList<Usuari>()

                amics.value = usuaris
            }
        }
    }

    fun mostrarDatePicker(onDateSelected: (String) -> Unit) {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected("$day/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(229, 255, 227)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Afegir un viatge",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) withContext@{
                        try {
                            val localitzacio = SupabaseClient.client
                                .from("Localitzacio")
                                .select {
                                    filter {
                                        eq("pais", paisSeleccionat.value)
                                        eq("regio", regioSeleccionada.value)
                                        eq("ciutat", ciutatSeleccionada.value)
                                    }
                                }
                                .decodeSingleOrNull<Localitzacio>()

                            val locId = localitzacio?.id?.toLong() ?: return@withContext
                            val dataIniciFormateada = formatejarPerSupabase(dataAnada.value)
                            val dataFinalFormateada = formatejarPerSupabase(dataTornada.value)


                            val viatge = SupabaseClient.client
                                .from("Viatge")
                                .insert(
                                    CheckInsertViatge(
                                        usuari_Id = usuariId.usuariId!!,
                                        data_inici = dataIniciFormateada,
                                        data_final = dataFinalFormateada,
                                        puntuacio = puntuacio.value,
                                        frase_estrella = frase.value,
                                        descripcio = descripcio.value,
                                        localitzacio_id = locId
                                    )
                                ) {
                                    select()
                                }
                                .decodeSingle<Viatge>()
                            fotesUris.value.forEach { uri ->
                                val bytes = context.contentResolver
                                    .openInputStream(uri)?.readBytes()
                                if (bytes != null) {
                                    val nomFitxer = "viatge_${viatge.id}_${System.currentTimeMillis()}.jpg"

                                    SupabaseClient.client.storage
                                        .from("fotos-viatges")
                                        .upload(nomFitxer, bytes) { upsert = true }

                                    val url = SupabaseClient.client.storage
                                        .from("fotos-viatges")
                                        .publicUrl(nomFitxer)

                                    SupabaseClient.client.from("Foto")
                                        .insert(NovaFoto(viatge_id = viatge.id, path = url))
                                }
                            }

                            amicsSeleccionats.value.forEach { amicId ->

                                SupabaseClient.client
                                    .from("Viatge_Amic")
                                    .insert(
                                        mapOf(
                                            "viatge_Id" to viatge.id,
                                            "usuari_id" to amicId
                                        )
                                    )
                            }

                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                containerColor = Color(0xFFB5E550)
            ) {
                Icon(
                    painter = painterResource(R.drawable.afegir),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Pais", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = paisSeleccionat.value,
            opcions = paisos.value,
            placeholder = "Selecciona un país",
            expanded = expandedPais.value,
            onExpandedChange = { expandedPais.value = it },
            onSeleccionar = { paisSeleccionat.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Regio", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = regioSeleccionada.value,
            opcions = regions.value,
            placeholder = if (paisSeleccionat.value.isEmpty()) "Primer selecciona un país" else "Selecciona una regió",
            expanded = expandedRegio.value,
            onExpandedChange = { if (paisSeleccionat.value.isNotEmpty()) expandedRegio.value = it },
            onSeleccionar = { regioSeleccionada.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ciutat", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = ciutatSeleccionada.value,
            opcions = ciutats.value,
            placeholder = if (regioSeleccionada.value.isEmpty()) "Primer selecciona una regió" else "Selecciona una ciutat",
            expanded = expandedCiutat.value,
            onExpandedChange = { if (regioSeleccionada.value.isNotEmpty()) expandedCiutat.value = it },
            onSeleccionar = { ciutatSeleccionada.value = it }
        )
        if (estat.value != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = estat.value!!, color = Color.Red, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Data anada",
                    color = Color(rgb(96, 106, 129)),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            Color(0xFFE0E0E0),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            mostrarDatePicker {
                                dataAnada.value = it
                            }
                        }
                        .padding(14.dp)
                ) {

                    Text(text = if (dataAnada.value.isEmpty()) "Seleccionar" else dataAnada.value)
                }
            }

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Data tornada",
                    color = Color(rgb(96, 106, 129)),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            Color(0xFFE0E0E0),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            mostrarDatePicker {
                                dataTornada.value = it
                            }
                        }
                        .padding(14.dp)
                ) {

                    Text(
                        text = if (dataTornada.value.isEmpty())
                            "Seleccionar"
                        else
                            dataTornada.value
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Amics",
            color = Color(rgb(96, 106, 129)),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expandedAmics.value,
            onExpandedChange = {
                expandedAmics.value = !expandedAmics.value
            }
        ) {

            Box(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        Color(0xFFE0E0E0),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(14.dp)
            ) {

                Text(
                    text = if (amicsSeleccionats.value.isEmpty())
                        "Seleccionar amics"
                    else
                        "${amicsSeleccionats.value.size} amics seleccionats"
                )
            }

            ExposedDropdownMenu(
                expanded = expandedAmics.value,
                onDismissRequest = {
                    expandedAmics.value = false
                }
            ) {

                amics.value.forEach { amic ->

                    val seleccionat =
                        amicsSeleccionats.value.contains(amic.id)

                    DropdownMenuItem(
                        text = {
                            Text("${amic.nom} ${amic.cognom}")
                        },
                        onClick = {

                            amicsSeleccionats.value =
                                if (seleccionat) {
                                    amicsSeleccionats.value - amic.id
                                } else {
                                    amicsSeleccionats.value + amic.id
                                }
                        },
                        trailingIcon = {

                            if (seleccionat) {
                                Icon(
                                    painterResource(R.drawable.check),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Puntuació",
            color = Color(rgb(96, 106, 129)),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row {

            (1..5).forEach { i ->

                Icon(
                    painter =
                        if (i <= puntuacio.value)
                            painterResource(R.drawable.estrella_full)
                        else
                            painterResource(R.drawable.estrella),

                    contentDescription = null,

                    tint =
                        if (i <= puntuacio.value)
                            Color(0xFFB5E550)
                        else
                            Color.LightGray,

                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            puntuacio.value = i
                        }
                )

                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Frase estrella",
            color = Color(rgb(96, 106, 129)),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(
                    1.dp,
                    Color(0xFFE0E0E0),
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {

            BasicTextField(
                value = frase.value,
                onValueChange = {
                    frase.value = it
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Descripció",
            color = Color(rgb(96, 106, 129)),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(
                    1.dp,
                    Color(0xFFE0E0E0),
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {

            BasicTextField(
                value = descripcio.value,
                onValueChange = {
                    descripcio.value = it
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = stringResource(R.string.fotos_videos), color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        val columnes = 3
        val totesFotos = fotesUris.value

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnes),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(totesFotos) { uri ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable { fotesUris.value = fotesUris.value - uri },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.fotos),
                            contentDescription = "Afegir fotos",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Afegir\nfotos",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pantalla_Estadistiques(navController: NavHostController, usuariId: UserViewModel, vm: EstadistiquesViewModel) {
    val mundialSeleccionat = remember { mutableStateOf(true) }
    val rankingMundial by vm.rankingMundial.collectAsState()
    val rankingAmics by vm.rankingAmics.collectAsState()
    val posicioMundial by vm.posicioUsuariMundial.collectAsState()
    val posicioAmics by vm.posicioUsuariAmics.collectAsState()
    val paisMesVisitat by vm.paisMesVisitat.collectAsState()
    val paisMesVistatMes by vm.paisMesVistatMes.collectAsState()
    val paisMesVistatAmics by vm.paisMesVistatAmics.collectAsState()
    val paisosAmics by vm.paisosAmics.collectAsState()

    LaunchedEffect(Unit) {
        usuariId.usuariId?.let { vm.carregarEstadistiques(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF0F0F0))
                .padding(4.dp)
        ) {
            listOf(true to "Mundial", false to "Amics").forEach { (valor, label) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (mundialSeleccionat.value == valor) Color(0xFFB5E550)
                            else Color.Transparent
                        )
                        .clickable { mundialSeleccionat.value = valor }
                        .padding(horizontal = 32.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (mundialSeleccionat.value == valor) Color.White else Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (mundialSeleccionat.value) {

            Text(text = "Top 5 Mundial", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            SeccioRanking(ranking = rankingMundial, posicioUsuari = posicioMundial)
            Spacer(modifier = Modifier.height(20.dp))
            SeccioPaisDestacats(titol = "País més visitat", pais = paisMesVisitat, subtitol = "de tots els temps")
            Spacer(modifier = Modifier.height(12.dp))
            SeccioPaisDestacats(titol = "Tendència del mes", pais = paisMesVistatMes, subtitol = "aquest mes")
            Spacer(modifier = Modifier.height(20.dp))

        } else {
            Text(text = " Top 5 Amics", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            SeccioRanking(ranking = rankingAmics, posicioUsuari = posicioAmics)
            Spacer(modifier = Modifier.height(20.dp))
            SeccioPaisDestacats(titol = "País més visitat entre amics", pais = paisMesVistatAmics, subtitol = "per els teus amics")
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Mapa dels amics", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) { MapaMundo(paisos = paisosAmics) }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SeccioPaisDestacats(titol: String, pais: PaisVisitat?, subtitol: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌍", fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = titol, fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = pais?.nom ?: "Sense dades",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (pais != null) {
                    Text(
                        text = "${pais.numVisites} visites $subtitol",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun SeccioRanking(ranking: List<UsuariRanking>, posicioUsuari: UsuariRanking?) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            ranking.forEachIndexed { index, ur ->
                FilaRanking(ur = ur, esTop3 = index < 3)
                if (index < ranking.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF0F0F0)
                    )
                }
            }
            posicioUsuari?.let { pos ->
                if (ranking.none { it.usuari.id == pos.usuari.id }) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFE0E0E0),
                        thickness = 2.dp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF9FBE7))
                    ) {
                        FilaRanking(ur = pos, esTop3 = false, destacat = true)
                    }
                }
            }
        }
    }
}

@Composable
fun FilaRanking(ur: UsuariRanking, esTop3: Boolean, destacat: Boolean = false) {
    val medalles = listOf("🥇", "🥈", "🥉")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(36.dp), contentAlignment = Alignment.Center) {
            Text(
                text = if (esTop3) medalles[ur.posicio - 1] else "#${ur.posicio}",
                fontSize = if (esTop3) 20.sp else 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            
            contentAlignment = Alignment.Center
        ) {
            if (!ur.usuari.foto_perfil.isNullOrBlank()) {
                AsyncImage(
                    model = ur.usuari.foto_perfil,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = ur.usuari.nom.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${ur.usuari.nom ?: ""} ${ur.usuari.cognom ?: ""}".trim(),
                fontSize = 14.sp,
                fontWeight = if (destacat) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${ur.numPaisos} països",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (esTop3) Color(0xFFB5E550) else Color(0xFFF0F0F0)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${"%.1f".format(ur.percentatge)}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (esTop3) Color.White else Color.Gray
            )
        }
    }
}



@Composable
fun Pantalla_Perfil(navController: NavController, userViewModel: UserViewModel, comunitatsViewModel: ComunitatsViewModel, paisosViewModel: PaisosViewModel) {
    var localitzacio by remember { mutableStateOf<Localitzacio?>(null) }
    var usuari by remember { mutableStateOf<Usuari?>(null) }
    var viatges by remember { mutableStateOf<List<Viatge>>(emptyList()) }
    var localitzacionsViatges by remember { mutableStateOf<Map<Long, Localitzacio>>(emptyMap()) }
    var personesPerViatge by remember { mutableStateOf<Map<Long, Int>>(emptyMap()) }
    var seguidorsUsuari by remember { mutableStateOf<Map<Long, List<Seguidor>>>(emptyMap()) }
    var primeraFotoPerViatge by remember { mutableStateOf<Map<Long, String?>>(emptyMap()) }
    val usuariId = userViewModel.usuariId
    LaunchedEffect(usuariId) {
        if (usuariId == null) return@LaunchedEffect
        usuariId.let { id ->
            withContext(Dispatchers.IO) {
                comunitatsViewModel.carregarComunitats(id)
                paisosViewModel.carregarPaissos(id)
                val usuariResultat = SupabaseClient.client
                    .from("Usuari")
                    .select() {
                        filter { eq("id", id) }
                    }
                    .decodeSingleOrNull<Usuari>()


                val seguidors = SupabaseClient.client
                    .from("Seguidor")
                    .select() {
                        filter { eq("usuari_seguit_id", id) }
                    }
                    .decodeList<Seguidor>()

                seguidorsUsuari = seguidors.groupBy { it.usuari_seguit_id }
                if (usuariResultat != null) {
                    usuari = usuariResultat

                    val localitzacioResultat = SupabaseClient.client
                        .from("Localitzacio")
                        .select() {
                            filter { eq("id", usuariResultat.localitzacio_id) }
                        }
                        .decodeSingleOrNull<Localitzacio>()

                    if (localitzacioResultat != null) {
                        localitzacio = localitzacioResultat
                    }

                    val viatgesResultat = SupabaseClient.client
                        .from("Viatge")
                        .select() {
                            filter { eq("usuari_Id", id) }
                        }
                        .decodeList<Viatge>()

                    viatges = viatgesResultat
                    val locMap = mutableMapOf<Long, Localitzacio>()
                    val personesMap = mutableMapOf<Long, Int>()

                    viatgesResultat.forEach { viatge ->

                        val loc = SupabaseClient.client
                            .from("Localitzacio")
                            .select() {
                                filter { eq("id", viatge.localitzacio_id) }
                            }
                            .decodeSingleOrNull<Localitzacio>()

                        if (loc != null) {
                            locMap[viatge.id] = loc
                        }

                        val persones = SupabaseClient.client
                            .from("Viatge_Amic")
                            .select() {
                                filter { eq("viatge_Id", viatge.id) }
                            }
                            .decodeList<ViatgeAmic>()

                        personesMap[viatge.id] = persones.size
                    }

                    localitzacionsViatges = locMap
                    personesPerViatge = personesMap

                    val viatgeIds = viatgesResultat.map { it.id }
                    val fotos = if (viatgeIds.isNotEmpty()) {
                        SupabaseClient.client
                            .from("Foto")
                            .select {
                                filter { isIn("viatge_id", viatgeIds as List<Any>) }
                            }
                            .decodeList<Foto>()
                    } else emptyList()

                    primeraFotoPerViatge = fotos
                        .groupBy { it.viatge_id }
                        .mapValues { it.value.first().path }

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (!usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = usuari!!.foto_perfil,
                        contentDescription = "Foto perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (usuari != null) {
                        "${usuari?.nom} ${usuari?.cognom}"
                    } else {
                        "Cargant nom..."
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (localitzacio != null) {
                            "${localitzacio?.ciutat}, ${localitzacio?.regio}, ${localitzacio?.pais}"
                        } else {
                            "Cargant ubicació..."
                        },
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val mundial = (usuari?.paissos?.div(195f))?.times(100f)

            EstadisticaItem(valor = "${viatges.size}", etiqueta = "Viatges")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.paissos}/195"} else { "..." }, etiqueta = "Paissos")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${mundial?.toInt()}%"} else { "..." }, etiqueta = "Mundial")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {navController.navigate(Screens.Pantalla_Perfil_Estadistica.name)},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
            ) {
                Text(text = stringResource(R.string.estadistiques), color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            val numSeguidors = seguidorsUsuari.size
            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Text(
                    text = if (numSeguidors == 1) "1 Seguidor" else "$numSeguidors Seguidors",
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(16.dp))

        if (viatges.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hi ha viatges encara", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height((((viatges.size + 1) / 2) * 270).dp)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viatges) { viatge ->
                    val loc = localitzacionsViatges[viatge.id]
                    val persones = personesPerViatge[viatge.id] ?: 0
                    TarjetaPerfil(
                        info = TarjetaInfo(
                            lloc = if (loc != null) "${loc.ciutat}, ${loc.pais}" else "...",
                            persones = "${persones + 1} persones",
                            dates = "${viatge.data_inici} - ${viatge.data_final}"
                        ),
                        navController = navController,
                        viatge_Id = viatge.id,
                        fotoViatge = primeraFotoPerViatge[viatge.id]
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Composable
fun Pantalla_Perfil_Estadistica(navController: NavController, userViewModel: UserViewModel, comunitatsViewModel: ComunitatsViewModel, paisosViewModel: PaisosViewModel) {
    var localitzacio by remember { mutableStateOf<Localitzacio?>(null) }
    var usuari by remember { mutableStateOf<Usuari?>(null) }
    var seguidorsUsuari by remember { mutableStateOf<Map<Long, List<Seguidor>>>(emptyMap()) }
    var viatgestotal by remember { mutableStateOf<List<Viatge>>(emptyList()) }
    val comunitats by comunitatsViewModel.llista.collectAsState()
    val paisos by paisosViewModel.llista.collectAsState()
    val usuariId = userViewModel.usuariId

    LaunchedEffect(usuariId) {
        usuariId?.let { id ->
            withContext(Dispatchers.IO) {
                try {
                    val usuariTemp = SupabaseClient.client
                        .from("Usuari")
                        .select { filter { eq("id", id) } }
                        .decodeSingleOrNull<Usuari>()

                    val seguidors = SupabaseClient.client
                        .from("Seguidor")
                        .select { filter { eq("usuari_seguit_id", id) } }
                        .decodeList<Seguidor>()

                    val viatges = SupabaseClient.client
                        .from("Viatge")
                        .select { filter { eq("usuari_Id", id) } }
                        .decodeList<Viatge>()

                    viatgestotal = viatges

                    val idsNeccesaris = (viatges.map { it.localitzacio_id } +
                            listOfNotNull(usuariTemp?.localitzacio_id)).distinct()

                    val localitzacionsMap = if (idsNeccesaris.isNotEmpty()) {
                        SupabaseClient.client
                            .from("Localitzacio")
                            .select { filter { isIn("id", idsNeccesaris) } }
                            .decodeList<Localitzacio>()
                            .associateBy { it.id }
                    } else emptyMap()

                    withContext(Dispatchers.Main) {
                        usuari = usuariTemp
                        seguidorsUsuari = seguidors.groupBy { it.usuari_seguit_id }
                        localitzacio = usuariTemp?.localitzacio_id?.let { localitzacionsMap[it] }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (!usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = usuari!!.foto_perfil,
                        contentDescription = "Foto perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (usuari != null) {
                        "${usuari?.nom} ${usuari?.cognom}"
                    } else {
                        "Cargant nom..."
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (localitzacio != null) {
                            "${localitzacio?.ciutat}, ${localitzacio?.regio}, ${localitzacio?.pais}"
                        } else {
                            "Cargant ubicació..."
                        },
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.comunitats_autonomes}/17"} else { "..." }, etiqueta = "Comunitats At")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.paissos}/195"} else { "..." }, etiqueta = "Paissos")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${viatgestotal.size}"} else { "..." }, etiqueta = "Viatges")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (usuari != null) {

                val paissos = usuari!!.paissos?.toFloat()
                val comunitats = usuari!!.comunitats_autonomes?.toFloat()
                val mundial = (paissos?.div(195f))?.times(100f)
                val europeu = (paissos?.div(50f))?.times(100f)
                val espanya = (comunitats?.div(17f))?.times(100f)

                EstadisticaItem(valor = "${mundial?.toInt()}%", etiqueta = "Mundial")
                VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
                EstadisticaItem(valor = "${europeu?.toInt()}%", etiqueta = "Europeu")
                VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
                EstadisticaItem(valor = "${espanya?.toInt()}%" , etiqueta = "Espanya")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screens.Pantalla_Perfil.name) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
            ) {
                Text(text = "Perfil", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            val numSeguidors = seguidorsUsuari.size
            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Text(text = if (numSeguidors == 1) "1 Seguidor" else "$numSeguidors Seguidors", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Paissos Visitats", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(10.dp))
        Log.d("MAPA_DEBUG", "PAÏSOS = ${paisos.joinToString()}")
        Log.d("MAPA_DEBUG", "COMUNITATS = ${comunitats.joinToString()}")

        LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(paisos.toList()) { paisNom ->

                val emoji = paisosViewModel.paisToEmoji(paisNom)

                Box(modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mapa de Espanya Visitat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) { MapaEspanya(comunitats.toList()) }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mapa Mundial Visitat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) {MapaMundo(paisos.toList())}
    }

        Spacer(modifier = Modifier.height(15.dp))
    }





@Composable
fun MapaMundo(paisos: List<String>) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->

            MapLibre.getInstance(ctx)

            MapView(ctx).apply {

                getMapAsync { map ->

                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(20.0, 0.0))
                        .zoom(0.1)
                        .build()

                    map.uiSettings.setAllGesturesEnabled(true)

                    map.setStyle(
                        Style.Builder().withTransition(TransitionOptions(0, 0))
                    ) { style ->

                        style.addLayer(
                            BackgroundLayer("background").apply {
                                setProperties(
                                    PropertyFactory.backgroundColor("#FFFFFF")
                                )
                            }
                        )

                        val geoJson = ctx.assets
                            .open("world.geojson")
                            .bufferedReader()
                            .readText()

                        val source = GeoJsonSource("world-source", geoJson)
                        style.addSource(source)

                        val expr = arrayOf(
                            get("name"),
                            *paisos.flatMap {
                                listOf(
                                    literal(it),
                                    color("#ffb832".toColorInt())
                                )
                            }.toTypedArray(),
                            color("#9CA3AF".toColorInt())
                        )

                        style.addLayer(
                            FillLayer("world-fill", "world-source").apply {
                                setProperties(
                                    PropertyFactory.fillColor(match(*expr)),
                                    PropertyFactory.fillOpacity(0.8f)
                                )
                            }
                        )

                        style.addLayer(
                            LineLayer("world-line", "world-source").apply {
                                setProperties(
                                    PropertyFactory.lineColor("#FFFFFF"),
                                    PropertyFactory.lineWidth(1.2f)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun MapaEspanya(comunitats: List<String>) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { ctx ->
            MapLibre.getInstance(ctx)
            MapView(ctx).apply {
                getMapAsync { map ->
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(40.0, -3.5))
                        .zoom(3.5)
                        .build()

                    map.uiSettings.setAllGesturesEnabled(true)
                    map.setStyle(
                        Style.Builder().withTransition(TransitionOptions(0, 0))
                    ) { style ->

                        style.addLayer(
                            BackgroundLayer("background").apply {
                                setProperties(
                                    PropertyFactory.backgroundColor("#FFFFFF")
                                )
                            }
                        )

                        val geoJson = ctx.assets
                            .open("spain.geojson")
                            .bufferedReader()
                            .readText()

                        val source = GeoJsonSource("regions-source", geoJson)
                        style.addSource(source)

                        val expr = arrayOf(
                            get("name"),
                            *comunitats.flatMap {
                                listOf(
                                    literal(it),
                                    color("#ffb832".toColorInt())
                                )
                            }.toTypedArray(),
                            color("#9CA3AF".toColorInt())
                        )

                        style.addLayer(
                            FillLayer("regions-fill", "regions-source").apply {
                                setProperties(
                                    PropertyFactory.fillColor(match(*expr)),
                                    PropertyFactory.fillOpacity(0.8f)
                                )
                            }
                        )

                        style.addLayer(
                            LineLayer("regions-line", "regions-source").apply {
                                setProperties(
                                    PropertyFactory.lineColor("#FFFFFF"),
                                    PropertyFactory.lineWidth(1.2f)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}





@Composable
fun Pantalla_Perfil_Extern(navController: NavController, perfilViewModel: UserViewModel, usuariId: Long?, comunitatsViewModel: ComunitatsViewModel, paisosViewModel: PaisosViewModel) {
    var localitzacio by remember { mutableStateOf<Localitzacio?>(null) }
    var usuari by remember { mutableStateOf<Usuari?>(null) }
    var viatges by remember { mutableStateOf<List<Viatge>>(emptyList()) }
    var localitzacionsViatges by remember { mutableStateOf<Map<Long, Localitzacio>>(emptyMap()) }
    var personesPerViatge by remember { mutableStateOf<Map<Long, Int>>(emptyMap()) }
    var seguidorsUsuari by remember { mutableStateOf<Map<Long, List<Seguidor>>>(emptyMap()) }
    var segueix by remember { mutableStateOf(false) }
    var primeraFotoPerViatge by remember { mutableStateOf<Map<Long, String?>>(emptyMap()) }
    val idPrincipal: Long? = perfilViewModel.usuariId
    val scope = rememberCoroutineScope()


    LaunchedEffect(usuariId) {
        usuariId.let { id ->
            withContext(Dispatchers.IO) {
                comunitatsViewModel.carregarComunitats(id!!)
                paisosViewModel.carregarPaissos(id)
                val usuariResultat = SupabaseClient.client
                    .from("Usuari")
                    .select() {
                        filter { eq("id", id) }
                    }
                    .decodeSingleOrNull<Usuari>()


                val seguidors = SupabaseClient.client
                    .from("Seguidor")
                    .select { filter { eq("usuari_seguit_id", id) } }
                    .decodeList<Seguidor>()

                val seguint = SupabaseClient.client
                    .from("Seguidor")
                    .select { filter { eq("usuari_seguidor_id", idPrincipal!!) } }
                    .decodeList<Seguidor>()

                val segueixLocal = seguint.any {
                    it.usuari_seguit_id == id
                }
                if (usuariResultat != null) {
                    usuari = usuariResultat

                    val localitzacioResultat = SupabaseClient.client
                        .from("Localitzacio")
                        .select() {
                            filter { eq("id", usuariResultat.localitzacio_id) }
                        }
                        .decodeSingleOrNull<Localitzacio>()

                    if (localitzacioResultat != null) {
                        localitzacio = localitzacioResultat
                    }

                    val viatgesResultat = SupabaseClient.client
                        .from("Viatge")
                        .select() {
                            filter { eq("usuari_Id", id) }
                        }
                        .decodeList<Viatge>()

                    viatges = viatgesResultat
                    val locMap = mutableMapOf<Long, Localitzacio>()
                    val personesMap = mutableMapOf<Long, Int>()

                    viatgesResultat.forEach { viatge ->

                        val loc = SupabaseClient.client
                            .from("Localitzacio")
                            .select() {
                                filter { eq("id", viatge.localitzacio_id) }
                            }
                            .decodeSingleOrNull<Localitzacio>()

                        if (loc != null) {
                            locMap[viatge.id] = loc
                        }

                        val persones = SupabaseClient.client
                            .from("Viatge_Amic")
                            .select() {
                                filter { eq("viatge_Id", viatge.id) }
                            }
                            .decodeList<ViatgeAmic>()

                        personesMap[viatge.id] = persones.size
                    }
                    seguidorsUsuari = seguidors.groupBy { it.usuari_seguit_id }
                    segueix = segueixLocal

                    localitzacionsViatges = locMap
                    personesPerViatge = personesMap

                    val viatgeIds = viatgesResultat.map { it.id }
                    val fotos = if (viatgeIds.isNotEmpty()) {
                        SupabaseClient.client
                            .from("Foto")
                            .select {
                                filter { isIn("viatge_id", viatgeIds as List<Any>) }
                            }
                            .decodeList<Foto>()
                    } else emptyList()

                    primeraFotoPerViatge = fotos
                        .groupBy { it.viatge_id }
                        .mapValues { it.value.first().path }

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (!usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = usuari!!.foto_perfil,
                        contentDescription = "Foto perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (usuari != null) {
                        "${usuari?.nom} ${usuari?.cognom}"
                    } else {
                        "Cargant nom..."
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (localitzacio != null) {
                            "${localitzacio?.ciutat}, ${localitzacio?.regio}, ${localitzacio?.pais}"
                        } else {
                            "Cargant ubicació..."
                        },
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val mundial = (usuari?.paissos?.div(195f))?.times(100f)

            EstadisticaItem(valor = "${viatges.size}", etiqueta = "Viatges")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.paissos}/195"} else { "..." }, etiqueta = "Paissos")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${mundial?.toInt()}%"} else { "..." }, etiqueta = "Mundial")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigate("${Screens.Pantalla_Perfil_Estadistica_Extern.name}/${usuari?.id}")},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
            ) {
                Text(text = stringResource(R.string.estadistiques), color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            if (segueix) {
                OutlinedButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            SupabaseClient.client
                                .from("Seguidor")
                                .delete {
                                    filter {
                                        eq("usuari_seguidor_id", idPrincipal!!)
                                        eq("usuari_seguit_id", usuariId!!)
                                    }
                                }

                            withContext(Dispatchers.Main) {
                                segueix = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Text("Seguint (${seguidorsUsuari.values.flatten().size})")                }

            } else {
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {

                            SupabaseClient.client
                                .from("Seguidor")
                                .insert(
                                    Seguidor(usuari_seguidor_id = idPrincipal!!, usuari_seguit_id = usuariId!!))

                            withContext(Dispatchers.Main) {
                                segueix = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
                ) {
                    Text("Seguir", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(16.dp))

        if (viatges.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hi ha viatges encara", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height((((viatges.size + 1) / 2) * 270).dp)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viatges) { viatge ->
                    val loc = localitzacionsViatges[viatge.id]
                    val persones = personesPerViatge[viatge.id] ?: 0
                    TarjetaPerfil2(
                        info = TarjetaInfo(
                            lloc = if (loc != null) "${loc.ciutat}, ${loc.pais}" else "...",
                            persones = "${persones + 1} persones",
                            dates = "${viatge.data_inici} - ${viatge.data_final}"
                        ),
                        navController = navController,
                        viatge_Id = viatge.id,
                        fotoViatge = primeraFotoPerViatge[viatge.id]
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Pantalla_Perfil_Estadistica_Extern(navController: NavController, perfilViewModel: UserViewModel, usuariId: Long, comunitatsViewModel: ComunitatsViewModel, paisosViewModel: PaisosViewModel) {
    var localitzacio by remember { mutableStateOf<Localitzacio?>(null) }
    var usuari by remember { mutableStateOf<Usuari?>(null) }
    var seguidorsUsuari by remember { mutableStateOf<Map<Long, List<Seguidor>>>(emptyMap()) }
    var viatgestotal by remember { mutableStateOf<List<Viatge>>(emptyList()) }
    var segueix by remember { mutableStateOf(false) }
    val idPrincipal: Long? = perfilViewModel.usuariId
    val comunitats by comunitatsViewModel.llista.collectAsState()
    val paisos by paisosViewModel.llista.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(usuariId) {
        usuariId.let { id ->
            withContext(Dispatchers.IO) {
                try {
                    val usuariTemp = SupabaseClient.client
                        .from("Usuari")
                        .select { filter { eq("id", id) } }
                        .decodeSingleOrNull<Usuari>()

                    val seguidors = SupabaseClient.client
                        .from("Seguidor")
                        .select { filter { eq("usuari_seguit_id", id) } }
                        .decodeList<Seguidor>()

                    val seguint = SupabaseClient.client
                        .from("Seguidor")
                        .select { filter { eq("usuari_seguidor_id", idPrincipal!!) } }
                        .decodeList<Seguidor>()

                    val segueixLocal = seguint.any {
                        it.usuari_seguit_id == id
                    }

                    val viatges = SupabaseClient.client
                        .from("Viatge")
                        .select { filter { eq("usuari_Id", id) } }
                        .decodeList<Viatge>()

                    viatgestotal=viatges

                    val idsNecesarios = (viatges.map { it.localitzacio_id } +
                            listOfNotNull(usuariTemp?.localitzacio_id)).distinct()

                    val localitzacionsMap = if (idsNecesarios.isNotEmpty()) {
                        SupabaseClient.client
                            .from("Localitzacio")
                            .select { filter { isIn("id", idsNecesarios) } }
                            .decodeList<Localitzacio>()
                            .associateBy { it.id }
                    } else emptyMap()

                    withContext(Dispatchers.Main) {
                        usuari = usuariTemp
                        seguidorsUsuari = seguidors.groupBy { it.usuari_seguit_id }
                        segueix = segueixLocal
                        localitzacio = usuariTemp?.localitzacio_id?.let { localitzacionsMap[it] }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (!usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = usuari!!.foto_perfil,
                        contentDescription = "Foto perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (usuari != null) {
                        "${usuari?.nom} ${usuari?.cognom}"
                    } else {
                        "Cargant nom..."
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (localitzacio != null) {
                            "${localitzacio?.ciutat}, ${localitzacio?.regio}, ${localitzacio?.pais}"
                        } else {
                            "Cargant ubicació..."
                        },
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.comunitats_autonomes}/17"} else { "..." }, etiqueta = "Comunitats At")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${usuari?.paissos}/195"} else { "..." }, etiqueta = "Paissos")
            VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
            EstadisticaItem(valor = if (usuari != null) { "${viatgestotal.size}"} else { "..." }, etiqueta = "Viatges")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (usuari != null) {

                val paissos = usuari!!.paissos?.toFloat()
                val comunitats = usuari!!.comunitats_autonomes?.toFloat()
                val mundial = (paissos?.div(195f))?.times(100f)
                val europeu = (paissos?.div(50f))?.times(100f)
                val espanya = (comunitats?.div(17f))?.times(100f)

                EstadisticaItem(valor = "${mundial?.toInt()}%", etiqueta = "Mundial")
                VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
                EstadisticaItem(valor = "${europeu?.toInt()}%", etiqueta = "Europeu")
                VerticalDivider(modifier = Modifier.height(40.dp), color = Color(0xFFE0E0E0))
                EstadisticaItem(valor = "${espanya?.toInt()}%" , etiqueta = "Espanya")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${usuari?.id}")},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
            ) {
                Text(text = "Perfil", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            if (segueix) {
                OutlinedButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            SupabaseClient.client
                                .from("Seguidor")
                                .delete {
                                    filter {
                                        eq("usuari_seguidor_id", idPrincipal!!)
                                        eq("usuari_seguit_id", usuariId)
                                    }
                                }

                            withContext(Dispatchers.Main) {
                                segueix = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Text("Seguint (${seguidorsUsuari.values.flatten().size})")  }

            } else {
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {

                            SupabaseClient.client
                                .from("Seguidor")
                                .insert(
                                    Seguidor(
                                        usuari_seguidor_id = idPrincipal!!,
                                        usuari_seguit_id = usuariId
                                    )
                                )

                            withContext(Dispatchers.Main) {
                                segueix = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5E550))
                ) {
                    Text("Seguir", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(16.dp))
        Log.i("paisosVisitats", paisos.toString())
        Text(text = "Paissos Visitats", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(paisos.toList()) { paisNom ->

                val emoji = paisosViewModel.paisToEmoji(paisNom)

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEEEEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 24.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mapa de Espanya Visitat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) {
            MapaEspanya(comunitats.toList())
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mapa Mundial Visitat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) {
            MapaMundo(paisos.toList())
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }


fun formatejarPerSupabase(fecha: String): String {
    val partes = fecha.split("/")
    if (partes.size != 3) return fecha

    val dia = partes[0].padStart(2, '0')
    val mes = partes[1].padStart(2, '0')
    val any = partes[2]

    return "$any-$mes-$dia"
}

@Composable
fun EstadisticaItem(valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = etiqueta, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun TarjetaPerfil(info: TarjetaInfo, navController: NavController, viatge_Id: Long, fotoViatge: String? = null) {
    var menuExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate("${Screens.Pantalla_Viatge.name}/$viatge_Id")
        }
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFDDDDDD))
            ) {
                if (!fotoViatge.isNullOrBlank()) {
                    AsyncImage(
                        model = fotoViatge,
                        contentDescription = "Foto viatge",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.End
            ) {

                Box {
                    IconButton(onClick = {
                        menuExpanded = true
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.opcions),
                            contentDescription = "Opcions"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Eliminar viatge") },
                            onClick = {
                                menuExpanded = false

                                coroutineScope.launch {
                                    deleteViatgeComplet(viatge_Id)
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.lloc, fontSize = 15.sp, color = Color.Gray, maxLines = 1)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.persones),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.persones, fontSize = 15.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.calendari),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.dates, fontSize = 15.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TarjetaPerfil2(info: TarjetaInfo, navController: NavController, viatge_Id: Long, fotoViatge: String? = null) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate("${Screens.Pantalla_Viatge.name}/$viatge_Id")
        }
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFDDDDDD))
            ) {
                if (!fotoViatge.isNullOrBlank()) {
                    AsyncImage(
                        model = fotoViatge,
                        contentDescription = "Foto viatge",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.End
            ) {

                Box {
                    IconButton(onClick = {}) {
                        Image(
                            painter = painterResource(id = R.drawable.opcions),
                            contentDescription = "Opcions"
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ubicacio),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.lloc, fontSize = 15.sp, color = Color.Gray, maxLines = 1)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.persones),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.persones, fontSize = 15.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.calendari),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = info.dates, fontSize = 15.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
fun Pantalla_TermesCondicions() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.termes_i_condicions),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(20.dp))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "En accedir i utilitzar TravelTracker, l’usuari accepta complir aquests termes i condicions. TravelTracker és una xarxa social de viatges que permet compartir experiències, continguts i recomanacions amb altres usuaris.\n" +
                            "L’usuari es compromet a fer un ús responsable de la plataforma, evitant publicar contingut il·legal, ofensiu o que vulneri drets de tercers. TravelTracker es reserva el dret de retirar qualsevol contingut que incompleixi aquestes normes.\n" +
                            "\n" +
                            "Els continguts publicats pels usuaris són responsabilitat exclusiva dels seus autors. En publicar-los, l’usuari concedeix a TravelTracker una llicència no exclusiva per mostrar-los dins de la plataforma.\n" +
                            "TravelTracker no garanteix la disponibilitat contínua del servei i es reserva el dret de modificar-lo en qualsevol moment.\n" +
                            "Per a més informació, es recomana revisar la política de privacitat associada.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pantalla_Missatges(navController: NavController, usuariId: UserViewModel, vm: MissatgesViewModel) {
    val converses by vm.converses.collectAsState()

    LaunchedEffect(Unit) {
        usuariId.usuariId?.let { vm.carregarConverses(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Missatges",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(color = Color(0xFFE0E0E0))

        if (converses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cap conversa encara", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(converses) { feed ->
                    ItemChat(feed = feed, onClick = {
                        navController.navigate("${Screens.Pantalla_Chat.name}/${feed.conversa.id}")
                    })
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemChat(feed: ConversaFeed, onClick: () -> Unit) {
    Spacer(modifier = Modifier.width(3.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            if (!feed.usuari.foto_perfil.isNullOrBlank()) {
                AsyncImage(
                    model = feed.usuari.foto_perfil,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = feed.usuari.nom.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${feed.usuari.nom} ${feed.usuari.cognom}".trim(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feed.ultimMissatge?.text ?: "Sense missatges",
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = feed.ultimMissatge?.datahora?.let { formatarHora(it) } ?: "",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }

    HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp), color = Color(0xFFF5F5F5))
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatarHora(datahora: String): String {
    return try {
        val dt = java.time.LocalDateTime.parse(datahora)
        val ara = java.time.LocalDateTime.now()
        when {
            dt.toLocalDate() == ara.toLocalDate() ->
                "%02d:%02d".format(dt.hour, dt.minute)
            dt.toLocalDate() == ara.toLocalDate().minusDays(1) -> "Ahir"
            else -> "%02d/%02d".format(dt.dayOfMonth, dt.monthValue)
        }
    } catch (e: Exception) { "" }
}



@Composable
fun Pantalla_Notificacions(navController: NavController,usuariId: UserViewModel, notificacionsViewModel: NotificacionsViewModel) {
    val nouViatge by notificacionsViewModel.nouViatge.collectAsState()
    val seguidors by notificacionsViewModel.seguidors.collectAsState()
    val likes by notificacionsViewModel.likes.collectAsState()
    var usuari by remember { mutableStateOf<Usuari?>(null) }

    LaunchedEffect(Unit) {
        usuariId.usuariId?.let {
            notificacionsViewModel.carregarNotificacions(it)
            val usuariResultat = SupabaseClient.client
                .from("Usuari")
                .select() {
                    filter { eq("id", it) }
                }
                .decodeSingleOrNull<Usuari>()
            usuari = usuariResultat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (!usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = usuari!!.foto_perfil,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "${usuari?.nom ?: ""} ${usuari?.cognom ?: ""}".trim(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(8.dp))

        if (nouViatge.isNotEmpty()) {
            SecciоNotificacions(
                navController,
                titol = "Nous viatges",
                notificacions = nouViatge.map {
                    Notificacio(
                        idUsuari = it.usuariOrigen?.id,
                        nomUsuari = "${it.usuariOrigen?.nom ?: ""} ${it.usuariOrigen?.cognom ?: ""}".trim(),
                        missatge = "ha publicat un nou viatge",
                        tenimaImatge = it.fotoViatge != null,
                        fotoPerfil = it.usuariOrigen?.foto_perfil,
                        fotoViatge = it.fotoViatge
                    )
                }
            )
            HorizontalDivider()
        }

        if (seguidors.isNotEmpty()) {
            SecciоNotificacions(
                navController,
                titol = "Amistats",
                notificacions = seguidors.map {
                    Notificacio(
                        idUsuari = it.usuariOrigen?.id,
                        nomUsuari = "${it.usuariOrigen?.nom ?: ""} ${it.usuariOrigen?.cognom ?: ""}".trim(),
                        missatge = "ha començat a seguir-te",
                        fotoPerfil = it.usuariOrigen?.foto_perfil
                    )
                }
            )
            HorizontalDivider()
        }

        if (likes.isNotEmpty()) {
            SecciоNotificacions(
                navController,
                titol = "Reaccions",
                notificacions = likes.map {
                    Notificacio(
                        idUsuari = it.usuariOrigen?.id,
                        nomUsuari = "${it.usuariOrigen?.nom ?: ""} ${it.usuariOrigen?.cognom ?: ""}".trim(),
                        missatge = "li ha agradat el teu viatge",
                        tenimaImatge = it.fotoViatge != null,
                        fotoPerfil = it.usuariOrigen?.foto_perfil,
                        fotoViatge = it.fotoViatge
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SecciоNotificacions(navController: NavController,titol: String, notificacions: List<Notificacio>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = titol,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        notificacions.forEach { notif ->
            ItemNotificacio(navController ,notif)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ItemNotificacio(navController: NavController,notif: Notificacio) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${notif.idUsuari}") },
            contentAlignment = Alignment.Center
        ) {

            if (!notif.fotoPerfil.isNullOrBlank()) {

                AsyncImage(
                    model = notif.fotoPerfil,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            } else {

                Text(
                    text = notif.nomUsuari.firstOrNull()?.toString() ?: "?",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append(notif.nomUsuari)
                }
                append(" ")
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append(notif.missatge)
                }
            },
            fontSize = 14.sp,
            lineHeight = 18.sp
        )

        if (notif.tenimaImatge) {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFCCCCCC))
            ){
                AsyncImage(
                    model = notif.fotoViatge,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()


                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pantalla_Chat(conversaId: Long, navController: NavController, Usuari: UserViewModel, vm: MissatgesViewModel) {
    val missatges by vm.missatges.collectAsState()
    val converses by vm.converses.collectAsState()
    val textEscrit = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val feed = converses.find { it.conversa.id == conversaId }

    LaunchedEffect(conversaId) {
        Usuari.usuariId?.let { vm.carregarMissatges(conversaId, it) }
    }

    LaunchedEffect(missatges.size) {
        if (missatges.isNotEmpty()) listState.animateScrollToItem(missatges.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.out),
                contentDescription = "Tornar",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { navController.popBackStack() },
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(15.dp))

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9))
                    .clickable { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${feed?.usuari?.id}") },
            contentAlignment = Alignment.Center
            ) {
                if (!feed?.usuari?.foto_perfil.isNullOrBlank()) {
                    AsyncImage(
                        model = feed.usuari.foto_perfil,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = feed?.usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Text(
                    text = "${feed?.usuari?.nom ?: ""} ${feed?.usuari?.cognom ?: ""}".trim(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = feed?.ultimMissatge?.datahora?.let { formatarHora(it) } ?: "",
                    fontSize = 17.sp,
                    color = Color.Gray
                )
            }
        }

        HorizontalDivider(color = Color(0xFFE0E0E0))
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(missatges) { missatge ->
                ItemMissatge(missatge = missatge)
            }
        }

        HorizontalDivider(color = Color(0xFFE0E0E0))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF2F2F2))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                BasicTextField(
                    value = textEscrit.value,
                    onValueChange = { textEscrit.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 4,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (textEscrit.value.isEmpty()) {
                                Text("Escriu un missatge...", color = Color.LightGray, fontSize = 14.sp)
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (textEscrit.value.isNotBlank()) Color(0xFFB5E550)
                        else Color(0xFFF0F0F0)
                    )
                    .clickable {
                        if (textEscrit.value.isNotBlank()) {
                            vm.enviarMissatge(
                                conversaId,
                                textEscrit.value,
                                Usuari.usuariId ?: return@clickable
                            )
                            textEscrit.value = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.enviar),
                    contentDescription = "Enviar",
                    tint = if (textEscrit.value.isNotBlank()) Color.White else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ItemMissatge(missatge: Missatge2) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (missatge.esPropi == true) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        missatge.esPropi?.let {
            if (!it) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB5E550))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 240.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (missatge.esPropi == true) 16.dp else 4.dp,
                        bottomEnd = if (missatge.esPropi == true) 4.dp else 16.dp
                    )
                )
                .background(
                    if (missatge.esPropi == true) Color(0xFFB5E550) else Color.White
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            missatge.text?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = if (missatge.esPropi == true) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun Pantalla_Configuracio(navController: NavController) {
    val opcions = listOf(
        Triple(R.drawable.notificacions, "Notificacions", Screens.Pantalla_NotificacionsConfig.name),
        Triple(R.drawable.audio, "Audio de l'aplicacio", Screens.Pantalla_Audio.name),
        Triple(R.drawable.privacitat, "Privacitat", Screens.Pantalla_Privacitat.name),
        Triple(R.drawable.info, "Termes i Condicions de TravelTracker", Screens.Pantalla_TermesCondicions.name),
        Triple(R.drawable.cookies, "Politica de Cookies", Screens.Pantalla_Cookies.name),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Text(
            text = "Configuracio",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        Column(modifier = Modifier.fillMaxWidth()) {
            opcions.forEach { (icona, text, ruta) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(ruta) }
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = icona),
                        contentDescription = text,
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFF0F0F0)
                )
            }
        }
    }
}



@Composable
fun Pantalla_NotificacionsConfig() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Text(
            text = stringResource(R.string.notificacions),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
    }
}

@Composable
fun Pantalla_Audio() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Text(
            text = "Audio de l'aplicacio",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
    }
}

@Composable
fun Pantalla_Privacitat() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Text(
            text = "Privacitat",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
    }
}

@Composable
fun Pantalla_Cookies() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Política de Cookies",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(20.dp))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "TravelTracker utilitza cookies per millorar l’experiència de l’usuari, analitzar l’ús de la plataforma i oferir continguts personalitzats.\n" +
                            "Les cookies són petits fitxers que es guarden al dispositiu de l’usuari i permeten reconèixer-lo en futures visites.\n" +
                            "\n" +
                            " TravelTracker pot utilitzar cookies pròpies i de tercers amb finalitats tècniques, analítiques i de personalització.\n" +
                            "L’usuari pot acceptar, rebutjar o configurar l’ús de cookies a través de la configuració del seu navegador. Cal tenir en compte que la desactivació d’algunes cookies pot afectar el funcionament correcte de la plataforma.\n" +
                            "\n" +
                            "En continuar navegant per TravelTracker, l’usuari accepta l’ús de cookies d’acord amb aquesta política.\n" +
                            "Per a més informació, es pot consultar la configuració de cookies o contactar amb el servei d’atenció a l’usuari.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun Pantalla_Login(navController: NavController, userViewModel: UserViewModel) {
    val correu = remember { mutableStateOf("") }
    val contrasenya = remember { mutableStateOf("") }
    val errorMissatge = remember { mutableStateOf("") }
    val carregant = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    tint = Color(0xFFB5E550),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "TravelTracker",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFB5E550))
                    .padding(16.dp)
            ) {
                Text(text = "Correu", color = Color.Black, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    BasicTextField(
                        value = correu.value,
                        onValueChange = { correu.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Contrasenya", color = Color.Black, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    BasicTextField(
                        value = contrasenya.value,
                        onValueChange = { contrasenya.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (errorMissatge.value.isNotEmpty()) {
                    Text(
                        text = errorMissatge.value,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (carregant.value) Color.DarkGray else Color.Black)
                        .clickable(enabled = !carregant.value) {
                            scope.launch {
                                carregant.value = true
                                errorMissatge.value = ""
                                try {
                                    val usuari = withContext(Dispatchers.IO) {
                                        SupabaseClient.client
                                            .from("Usuari")
                                            .select() {
                                                filter {
                                                    eq("correu", correu.value)
                                                    eq("contrasenya", contrasenya.value)
                                                }
                                            }
                                            .decodeSingleOrNull<Usuari>()
                                    }
                                    if (usuari != null) {
                                        userViewModel.setUser(usuari.id)

                                        FirebaseMessaging.getInstance().token
                                            .addOnCompleteListener { task ->
                                                if (!task.isSuccessful) return@addOnCompleteListener
                                                val token = task.result
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    SupabaseClient.client
                                                        .from("Usuari")
                                                        .update(mapOf("fcm_token" to token)) {
                                                            filter { eq("id", usuari.id) }
                                                        }
                                                }
                                            }


                                        navController.navigate(Screens.Pantalla_Principal.name) {
                                            popUpTo(Screens.Pantalla_Login.name) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        errorMissatge.value = "Correu o contrasenya incorrectes"
                                    }
                                } catch (e: Exception) {
                                    errorMissatge.value = "Error de connexió: ${e.message}"
                                }
                                carregant.value = false
                            }
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (carregant.value) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text(text = "Iniciar Sessio", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Contrasenya oblidada?",
                    color = Color.Black,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFB5E550))
                    .clickable { navController.navigate(Screens.Pantalla_Registre.name) }
                    .padding(horizontal = 48.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Registrarse", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(70.dp))

        }
    }
}


fun passwordEsFuerte(password: String): Boolean {
    val teLongitud = password.length >= 8
    val teMayus = password.any { it.isUpperCase() }
    val teNum = password.any { it.isDigit() }

    return teLongitud && teMayus && teNum
}
@Composable
fun Pantalla_Registre(navController: NavController, registerViewModel: RegisterViewModel) {
    val correu = remember { mutableStateOf("") }
    val contrasenya1 = remember { mutableStateOf("") }
    val contrasenya2 = remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var carregant by remember { mutableStateOf(false) }
    val emailValid = Patterns.EMAIL_ADDRESS.matcher(correu.value).matches()
    val passwordsIguals = contrasenya1.value == contrasenya2.value && contrasenya1.value.isNotEmpty()
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                tint = Color(0xFFB5E550),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "TravelTracker",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFB5E550))
                .padding(16.dp)
        ) {

            Text(
                text = "Correu",
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {

                BasicTextField(
                    value = correu.value,
                    onValueChange = {
                        correu.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (correu.value.isEmpty()) {
                    ""
                } else if (emailValid) {
                    "Email vàlid"
                } else {
                    "Email invàlid"
                },
                color = if (emailValid) {
                    Color(0xFF1B5E20)
                } else {
                    Color.Red
                },
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Contrasenya",
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {

                BasicTextField(
                    value = contrasenya1.value,
                    onValueChange = {
                        contrasenya1.value = it
                        passwordError = if (it.isEmpty() || passwordEsFuerte(it)) {
                            ""
                        } else {
                            "La contrasenya ha de tenir mínim 8 caràcters, una majúscula i un número"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (passwordsIguals)
                            Color(0xFF1B5E20)
                        else
                            Color(0xFF8B0000)
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = if (passwordsIguals)
                        "Les contrasenyes coincideixen"
                    else
                        "Les contrasenyes no coincideixen",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Repetir contrasenya",
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {

                BasicTextField(
                    value = contrasenya2.value,
                    onValueChange = {
                        contrasenya2.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
            }

            if (errorText.isNotEmpty()) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorText,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Box(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    if (
                        emailValid &&
                        passwordsIguals &&
                        !carregant
                    )
                        Color(0xFFB5E550)
                    else
                        Color.Gray
                )
                .clickable {

                    if (!emailValid) {
                        errorText = "El correu no és vàlid"
                        return@clickable
                    }

                    if (!passwordsIguals) {
                        errorText = "Les contrasenyes no coincideixen"
                        return@clickable
                    }
                    if (!passwordEsFuerte(contrasenya1.value)) {
                        errorText = "La contrasenya no és prou segura"
                        return@clickable
                    }

                    coroutineScope.launch {

                        try {

                            carregant = true
                            errorText = ""

                            val existeixUsuari =
                                SupabaseClient.client
                                    .from("Usuari")
                                    .select {
                                        filter {
                                            eq("correu", correu.value)
                                        }
                                    }
                                    .decodeList<Usuari>()

                            if (existeixUsuari.isNotEmpty()) {

                                errorText = "Aquest correu ja existeix"
                                carregant = false
                                return@launch
                            }

                            registerViewModel.usuari =
                                registerViewModel.usuari.copy(
                                    correu = correu.value,
                                    contrasenya = contrasenya1.value
                                )

                            navController.navigate(Screens.Pantalla_Crear_Perfil.name)

                        } catch (e: Exception) {

                            errorText = e.message ?: "Error inesperat"

                        } finally {

                            carregant = false
                        }
                    }
                }
                .padding(horizontal = 48.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (carregant) "Registrant..." else "Registrarse",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}







@Composable
fun Pantalla_Crear_Perfil(navController: NavController, userViewModel: UserViewModel,registerViewModel: RegisterViewModel) {
    val nom = remember { mutableStateOf("") }
    val cognom = remember { mutableStateOf("") }
    val dataNaixament = remember { mutableStateOf("") }
    val telefon = remember { mutableStateOf("") }
    val fotoPerfil = remember { mutableStateOf<String?>(null) }
    val paisos = remember { mutableStateOf<List<String>>(emptyList()) }
    val regions = remember { mutableStateOf<List<String>>(emptyList()) }
    val ciutats = remember { mutableStateOf<List<String>>(emptyList()) }
    val paisSeleccionat = remember { mutableStateOf("Spain") }
    val regioSeleccionada = remember { mutableStateOf("") }
    val ciutatSeleccionada = remember { mutableStateOf("") }
    val expandedPais = remember { mutableStateOf(false) }
    val expandedRegio = remember { mutableStateOf(false) }
    val expandedCiutat = remember { mutableStateOf(false) }
    val estat = remember { mutableStateOf<String?>(null) }
    val carregant = remember { mutableStateOf(false) }
    val fotoUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { fotoUri.value = it }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val result = SupabaseClient.client.postgrest
                .rpc(
                    function = "get_paises",
                    parameters = Unit
                )
                .decodeList<PaisResponse>()

            paisos.value = result.map { it.pais }
        }
    }

    LaunchedEffect(paisSeleccionat.value) {
        if (paisSeleccionat.value.isNotBlank()) {
            regioSeleccionada.value = ""
            ciutatSeleccionada.value = ""
            regions.value = emptyList()
            ciutats.value = emptyList()
            withContext(Dispatchers.IO) {
                val localitzacions = SupabaseClient.client
                    .from("Localitzacio")
                    .select {
                        filter { eq("pais", paisSeleccionat.value) }
                    }
                    .decodeList<Localitzacio>()
                regions.value = localitzacions
                    .mapNotNull { it.regio }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
    }

    LaunchedEffect(regioSeleccionada.value) {
        if (regioSeleccionada.value.isNotBlank()) {
            ciutatSeleccionada.value = ""
            ciutats.value = emptyList()
            withContext(Dispatchers.IO) {
                val localitzacions = SupabaseClient.client
                    .from("Localitzacio")
                    .select {
                        filter {
                            eq("pais", paisSeleccionat.value)
                            eq("regio", regioSeleccionada.value)
                        }
                    }
                    .decodeList<Localitzacio>()
                ciutats.value = localitzacions
                    .mapNotNull { it.ciutat }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()
            }
        }
    }

    fun guardar() {
        scope.launch {
            carregant.value = true
            try {
                withContext(Dispatchers.IO) {
                    val localitzacio = SupabaseClient.client
                        .from("Localitzacio")
                        .select {
                            filter {
                                eq("pais", paisSeleccionat.value)
                                eq("regio", regioSeleccionada.value)
                                eq("ciutat", ciutatSeleccionada.value)
                            }
                        }
                        .decodeSingleOrNull<Localitzacio>()

                    val locId = localitzacio?.id?.toLong() ?: return@withContext

                    fotoUri.value?.let { uri ->
                        val bytes = context.contentResolver
                            .openInputStream(uri)?.readBytes()
                        if (bytes != null) {
                            val nomFitxer = "perfil_${System.currentTimeMillis()}.jpg"
                            SupabaseClient.client.storage
                                .from("fotos-perfil")
                                .upload(nomFitxer, bytes) {
                                    upsert = true
                                }
                            val url = SupabaseClient.client.storage
                                .from("fotos-perfil")
                                .publicUrl(nomFitxer)
                            fotoPerfil.value = url
                        }
                    }

                    registerViewModel.usuari = registerViewModel.usuari.copy(
                        nom = nom.value,
                        cognom = cognom.value,
                        data_naixament = dataNaixament.value,
                        telefon = telefon.value.toLong(),
                        localitzacio_id = locId,
                        foto_perfil = fotoPerfil.value
                    )

                    val nouUsuari = SupabaseClient.client
                        .from("Usuari")
                        .insert(registerViewModel.usuari) {
                            select()
                        }
                        .decodeSingle<Usuari>()

                    userViewModel.usuariId = nouUsuari.id

                    SupabaseClient.client
                        .from("Seguidor")
                        .insert(
                            Seguidor(usuari_seguidor_id = userViewModel.usuariId!!, usuari_seguit_id = userViewModel.usuariId!!))
                    


                }

                navController.navigate(Screens.Pantalla_Login.name) {
                    popUpTo(0) { inclusive = true }
                }

            } catch (e: Exception) {
                estat.value = "Error: ${e.message}"
                e.printStackTrace()
            }
            carregant.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(229, 255, 227))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Crea el teu Perfil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            FloatingActionButton(
                onClick = { guardar() },
                shape = CircleShape,
                containerColor = if (carregant.value) Color.Gray else Color(0xFFB5E550),
                modifier = Modifier.size(50.dp)
            ) {
                if (carregant.value) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(
                        painter = painterResource(R.drawable.afegir),
                        contentDescription = "Guardar",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Afegir foto de Perfil", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (fotoUri.value != null) {
                AsyncImage(
                    model = fotoUri.value,
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.fotos),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Afegir foto", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nom i Cognoms", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.compte),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = nom.value,
                        onValueChange = { nom.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (nom.value.isEmpty()) Text("Escriu el teu nom.....", color = Color.LightGray, fontSize = 14.sp)
                                innerTextField()
                            }
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                BasicTextField(
                    value = cognom.value,
                    onValueChange = { cognom.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (cognom.value.isEmpty()) Text("Cognom", color = Color.LightGray, fontSize = 14.sp)
                            innerTextField()
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Data Naixement", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
                        .clickable {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    val mes = (m + 1).toString().padStart(2, '0')
                                    val dia = d.toString().padStart(2, '0')
                                    dataNaixament.value = "$y-$mes-$dia"
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = dataNaixament.value.ifEmpty { "yyyy-mm-dd" },
                        color = if (dataNaixament.value.isEmpty()) Color.LightGray else Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Telèfon", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = telefon.value,
                        onValueChange = { telefon.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (telefon.value.isEmpty()) Text("6XXXXXXXX", color = Color.LightGray, fontSize = 14.sp)
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Pais", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = paisSeleccionat.value,
            opcions = paisos.value,
            placeholder = "Selecciona un país",
            expanded = expandedPais.value,
            onExpandedChange = { expandedPais.value = it },
            onSeleccionar = { paisSeleccionat.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Regio", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = regioSeleccionada.value,
            opcions = regions.value,
            placeholder = if (paisSeleccionat.value.isEmpty()) "Primer selecciona un país" else "Selecciona una regió",
            expanded = expandedRegio.value,
            onExpandedChange = { if (paisSeleccionat.value.isNotEmpty()) expandedRegio.value = it },
            onSeleccionar = { regioSeleccionada.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ciutat", color = Color(rgb(96, 106, 129)), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        ComboBox(
            valor = ciutatSeleccionada.value,
            opcions = ciutats.value,
            placeholder = if (regioSeleccionada.value.isEmpty()) "Primer selecciona una regió" else "Selecciona una ciutat",
            expanded = expandedCiutat.value,
            onExpandedChange = { if (regioSeleccionada.value.isNotEmpty()) expandedCiutat.value = it },
            onSeleccionar = { ciutatSeleccionada.value = it }
        )
        if (estat.value != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = estat.value!!, color = Color.Red, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}



@Composable
fun ComboBox(valor: String, opcions: List<String>, placeholder: String, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, onSeleccionar: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
                .clickable { onExpandedChange(!expanded) }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.buscar),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (valor.isEmpty()) placeholder else valor,
                        color = if (valor.isEmpty()) Color.LightGray else Color.Black,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.opcions),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .background(Color.White)
        ) {
            opcions.forEach { opcio ->
                DropdownMenuItem(
                    text = { Text(text = opcio, fontSize = 14.sp) },
                    onClick = {
                        onSeleccionar(opcio)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun Pantalla_Viatge(navController: NavController, userViewModel: UserViewModel, viatgeViewModel: ViatgeViewModel, missatgesViewModel: MissatgesViewModel) {
    var localitzacio by remember { mutableStateOf<Localitzacio?>(null) }
    var localitzacioViatge by remember { mutableStateOf<Localitzacio?>(null) }
    var usuari by remember { mutableStateOf<Usuari?>(null) }
    var viatge by remember { mutableStateOf<Viatge?>(null) }
    var usuaris by remember { mutableStateOf<List<Usuari>>(emptyList()) }
    val viatgeId = viatgeViewModel.viatge_Id
    var liked by remember { mutableStateOf(false) }
    var fotos by remember { mutableStateOf<List<Foto>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val userId = userViewModel.usuariId
    val context = LocalContext.current

    LaunchedEffect(viatgeId) {
        viatgeId?.let { vId ->
            withContext(Dispatchers.IO) {

                val viatgeResultat = SupabaseClient.client
                    .from("Viatge")
                    .select {
                        filter { eq("id", vId) }
                    }
                    .decodeSingleOrNull<Viatge>()

                viatge = viatgeResultat

                val usuariResultat = viatgeResultat?.let { viatge ->
                    SupabaseClient.client
                        .from("Usuari")
                        .select {
                            filter { eq("id", viatge.usuari_Id!!) }
                        }
                        .decodeSingleOrNull<Usuari>()
                }

                usuari = usuariResultat

                localitzacio = usuariResultat?.localitzacio_id?.let { locId ->
                    SupabaseClient.client
                        .from("Localitzacio")
                        .select {
                            filter { eq("id", locId) }
                        }
                        .decodeSingleOrNull<Localitzacio>()
                }

                localitzacioViatge = viatgeResultat?.localitzacio_id?.let { locId ->
                    SupabaseClient.client
                        .from("Localitzacio")
                        .select {
                            filter { eq("id", locId) }
                        }
                        .decodeSingleOrNull<Localitzacio>()
                }

                val amicsResultat = SupabaseClient.client
                    .from("Viatge_Amic")
                    .select {
                        filter { eq("viatge_Id", vId) }
                    }
                    .decodeList<ViatgeAmic>()

                val usuarisIds = amicsResultat.map { it.usuari_id }.distinct()

                usuaris = if (usuarisIds.isNotEmpty()) {
                    SupabaseClient.client
                        .from("Usuari")
                        .select {
                            filter { isIn("id", usuarisIds) }
                        }
                        .decodeList()
                } else emptyList()
                val likes = SupabaseClient.client
                    .from("Viatge_Like")
                    .select {
                        filter {
                            eq("viatge_id", viatgeId)
                            eq("usuari_id", userId!!)
                        }
                    }
                    .decodeList<ViatgeLike>()

                liked = likes.isNotEmpty()

                val fotesResultat = SupabaseClient.client
                    .from("Foto")
                    .select {
                        filter { eq("viatge_id", vId) }
                    }
                    .decodeList<Foto>()

                fotos = fotesResultat
            }
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color(229, 255, 227))
                    )
                )
                .padding(bottom = 70.dp)
        ) {

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9))
                            .clickable { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${usuari?.id}") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!usuari?.foto_perfil.isNullOrBlank()) {
                            AsyncImage(
                                model = usuari!!.foto_perfil,
                                contentDescription = "Foto perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = usuari?.nom?.firstOrNull()?.uppercase() ?: "?",
                                color = Color(0xFF4CAF50),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (usuari != null) { "${usuari?.nom} ${usuari?.cognom}" } else { "Cargant nom..." },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ubicacio),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = if (localitzacio != null) { "${localitzacio?.ciutat}, ${localitzacio?.regio}, ${localitzacio?.pais}" } else { "Cargant ubicació..." },
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.width(12.dp))

            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Infrmacio del viatge",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 150.dp, height = 120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDDDDDD)),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val pais = localitzacioViatge?.pais
                            if (pais != null) {
                                MapaMundo(listOf(pais))
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.logo), null, tint = Color.Black, modifier = Modifier.size(17.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (localitzacioViatge != null) { "${localitzacioViatge?.ciutat}, ${localitzacioViatge?.pais}" } else { "Cargant ubicació..." }, fontSize = 15.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.calendari), null, tint = Color.Black, modifier = Modifier.size(17.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (viatge != null) { "${viatge?.data_inici} - ${viatge?.data_final}" } else { "..." }, fontSize = 15.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.estrella), null, tint = Color.Black, modifier = Modifier.size(17.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (viatge != null) { "${viatge?.puntuacio}/5"} else { "..." }, fontSize = 15.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Amics del viatge",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    usuaris.forEach { usuari ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                                .clickable { navController.navigate("${Screens.Pantalla_Perfil_Extern.name}/${usuari.id}") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (!usuari.foto_perfil.isNullOrBlank()) {
                                AsyncImage(
                                    model = usuari.foto_perfil,
                                    contentDescription = "Foto perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = usuari.nom.firstOrNull()?.uppercase() ?: "?",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Fotos i videos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (fotos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sense fotos", color = Color.LightGray, fontSize = 14.sp)
                    }
                } else {
                    val pagerState = rememberPagerState(pageCount = { fotos.size })

                    Box(modifier = Modifier.fillMaxWidth()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 32.dp),
                            pageSpacing = 8.dp
                        ) { page ->
                            AsyncImage(
                                model = fotos[page].path,
                                contentDescription = "Foto viatge",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (fotos.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                fotos.forEachIndexed { index, _ ->
                                    Box(
                                        modifier = Modifier
                                            .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (index == pagerState.currentPage) Color.White
                                                else Color.White.copy(alpha = 0.5f)
                                            )
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${pagerState.currentPage + 1}/${fotos.size}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Frase Estrella",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    viatge?.frase_estrella?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripcio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    viatge?.descripcio?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                HorizontalDivider(color = Color(0xFFE0E0E0))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        scope.launch(Dispatchers.IO) {

                            if (liked) {
                                SupabaseClient.client
                                    .from("Viatge_Like")
                                    .delete {
                                        filter {
                                            eq("viatge_id", viatgeId!!)
                                            eq("usuari_id", userId!!)
                                        }
                                    }

                                withContext(Dispatchers.Main) {
                                    liked = false
                                }

                            } else {
                                SupabaseClient.client
                                    .from("Viatge_Like")
                                    .insert(
                                        ViatgeLike(
                                            viatge_id = viatgeId!!,
                                            usuari_id = userId!!
                                        )
                                    )

                                withContext(Dispatchers.Main) {
                                    liked = true
                                }
                            }
                        }
                    }) {
                        Image(
                            painter = painterResource(id = if (liked) R.drawable.likecolor else R.drawable.like),
                            contentDescription = "Like",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    IconButton(onClick = {
                        userId?.let { myId ->
                            usuari?.id?.let { altreId ->
                                missatgesViewModel.obtenirOCrearConversaINavegar(myId, altreId) { conversaId ->
                                    navController.navigate("${Screens.Pantalla_Chat.name}/$conversaId")
                                }
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.comentari),
                            contentDescription = "Comentar",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply { type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Mira este viaje: https://traveltracker/viatge/$viatgeId")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartir Viatge")) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.comparir),
                            contentDescription = "Compartir",
                            tint = Color.Black,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }
}





