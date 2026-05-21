package com.example.traveltracker

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.traveltracker.EstadistiquesViewModel
import com.example.traveltracker.model.Pantalla_Afegir
import com.example.traveltracker.model.Pantalla_Audio
import com.example.traveltracker.model.Pantalla_Chat
import com.example.traveltracker.model.Pantalla_Configuracio
import com.example.traveltracker.model.Pantalla_Cookies
import com.example.traveltracker.model.Pantalla_Crear_Perfil
import com.example.traveltracker.model.Pantalla_Estadistiques
import com.example.traveltracker.model.Pantalla_Login
import com.example.traveltracker.model.Pantalla_Missatges
import com.example.traveltracker.model.Pantalla_Notificacions
import com.example.traveltracker.model.Pantalla_NotificacionsConfig
import com.example.traveltracker.model.Pantalla_Perfil
import com.example.traveltracker.model.Pantalla_Perfil_Estadistica
import com.example.traveltracker.model.Pantalla_Perfil_Estadistica_Extern
import com.example.traveltracker.model.Pantalla_Perfil_Extern
import com.example.traveltracker.model.Pantalla_Principal
import com.example.traveltracker.model.Pantalla_Privacitat
import com.example.traveltracker.model.Pantalla_Registre
import com.example.traveltracker.model.Pantalla_TermesCondicions
import com.example.traveltracker.model.Pantalla_Viatge
import com.example.traveltracker.model.Screens
import com.example.traveltracker.ui.theme.TravelTrackerTheme
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            val userViewModel: UserViewModel = viewModel()
            val comunitatsViewModel: ComunitatsViewModel = viewModel()
            val paisosViewModel: PaisosViewModel = viewModel()
            val viatgeViewModel: ViatgeViewModel = viewModel()
            val principalViewModel: PrincipalViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
            val missatgesViewModel: MissatgesViewModel = viewModel()
            val estadistiquesViewModel: EstadistiquesViewModel = viewModel()
            val notificacionsViewModel: NotificacionsViewModel = viewModel()


            val mostrarBars = currentRoute != Screens.Pantalla_Login.name && currentRoute != Screens.Pantalla_Registre.name && currentRoute != Screens.Pantalla_Crear_Perfil.name
            val context = LocalContext.current
            val iconsUnselected = listOf(
                painterResource(R.drawable.principal_unselect),
                painterResource(R.drawable.stats_unselect),
                painterResource(R.drawable.misatge_unselect),
                painterResource(R.drawable.perfil_unselect)
            )
            val iconsSelected = listOf(
                painterResource(R.drawable.principal_selected),
                painterResource(R.drawable.stats_selected),
                painterResource(R.drawable.misatge_selected),
                painterResource(R.drawable.perfil_selected)
            )
            val bottomScreens = listOf(
                Screens.Pantalla_Principal,
                Screens.Pantalla_Estadistiques,
                Screens.Pantalla_Missatges,
                Screens.Pantalla_Perfil
            )

            TravelTrackerTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = false,
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            ) {
                                Spacer(modifier = Modifier.width(50.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.width(10.dp))

                                    Icon(
                                        painter = painterResource(id = R.drawable.logo),
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("  TravelTracker")
                                }
                                Spacer(Modifier.height(30.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(20.dp))
                                NavigationDrawerItem(
                                    label = { Text("Configuració") },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.configuracio),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {navController.navigate(Screens.Pantalla_Configuracio.name)
                                        scope.launch { drawerState.close() }
                                    }
                                )
                                Spacer(Modifier.height(20.dp))
                                NavigationDrawerItem(
                                    label = { Text("Compte") },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.compte),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = { navController.navigate(Screens.Pantalla_Perfil.name)
                                        scope.launch { drawerState.close() }

                                    }
                                )
                                Spacer(Modifier.height(20.dp))
                                NavigationDrawerItem(
                                    label = { Text("Termes i condicions") },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.info),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = { navController.navigate(Screens.Pantalla_TermesCondicions.name)
                                        scope.launch { drawerState.close() }

                                    }
                                )
                                Spacer(Modifier.height(20.dp))
                                NavigationDrawerItem(
                                    label = { Text("Tancar Sessió") },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.tancar_sessio),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        userViewModel.logout()

                                        navController.navigate(Screens.Pantalla_Login.name) {
                                            popUpTo(0)
                                        }
                                    }
                                )
                                Spacer(Modifier.height(20.dp))
                                NavigationDrawerItem(
                                    label = { Text("Fora de l'app") },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.fora_app),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        (context as Activity).finish()
                                    }
                                )
                                Spacer(Modifier.height(30.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                NavigationDrawerItem(
                                    label = { Text(stringResource(R.string.tancar_menu)) },
                                    selected = false,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.close),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                    }
                                )
                            }
                        }
                    }
                ) {

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),

                        topBar = {
                            if (mostrarBars) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White,
                                    titleContentColor = Color.Black,
                                ),
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(65.dp))

                                        Icon(
                                            painter = painterResource(id = R.drawable.logo),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text("  TravelTracker")
                                    }
                                },

                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.menu),
                                            contentDescription = "Menu",
                                            modifier = Modifier.size(35.dp)
                                        )
                                    }
                                },

                                actions = {
                                    IconButton(onClick = { navController.navigate(Screens.Pantalla_Notificacions.name) }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.notifications),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                }
                            )
                        }
                        },

                        bottomBar = {
                            if(mostrarBars){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .background(Color(229, 255, 227))
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CurvaNavigationBar())
                                        .background(Color.Black)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 20.dp)
                                        .offset(y = (-20).dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    bottomScreens.forEachIndexed { index, screen ->

                                        if (index == 2) {
                                            Spacer(modifier = Modifier.width(40.dp))
                                        }
                                        IconButton(
                                            onClick = {
                                                navController.navigate(screen.name) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        ){
                                            Icon(
                                                painter = if (screen.name == currentRoute)
                                                    iconsSelected[index]
                                                else
                                                    iconsUnselected[index],
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .offset(y = (-5).dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        }

                    ) { innerPadding ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            ActivityCompat.requestPermissions(
                                this as Activity,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                1
                            )
                        }
                        NavHost(navController = navController, startDestination = Screens.Pantalla_Login.name, modifier = if (mostrarBars) Modifier.padding(innerPadding) else Modifier
                        ) {
                            composable(Screens.Pantalla_Principal.name) {
                                Pantalla_Principal(navController,userViewModel,principalViewModel )
                            }
                            composable(Screens.Pantalla_Estadistiques.name) {
                                Pantalla_Estadistiques(navController, userViewModel, estadistiquesViewModel)
                            }
                            composable(Screens.Pantalla_Missatges.name) {
                                Pantalla_Missatges(navController, userViewModel, missatgesViewModel)
                            }
                            composable(Screens.Pantalla_Perfil.name) {
                                Pantalla_Perfil(navController,userViewModel,comunitatsViewModel,paisosViewModel)
                            }
                            composable(route = Screens.Pantalla_Login.name) {
                                Pantalla_Login(navController, userViewModel)
                            }
                            composable(route = Screens.Pantalla_Registre.name) {
                                Pantalla_Registre(navController,registerViewModel)
                            }
                            composable(route = Screens.Pantalla_Crear_Perfil.name) {
                                Pantalla_Crear_Perfil(navController,userViewModel,registerViewModel)
                            }
                            composable(Screens.Pantalla_Afegir.name) {
                                Pantalla_Afegir(navController,userViewModel)
                            }
                            composable(Screens.Pantalla_Notificacions.name) {
                                Pantalla_Notificacions(navController ,userViewModel, notificacionsViewModel)
                            }
                            composable(route = "${Screens.Pantalla_Chat.name}/{conversaId}") { backStackEntry ->
                                val conversaId = backStackEntry.arguments?.getString("conversaId")?.toLongOrNull() ?: return@composable
                                Pantalla_Chat(conversaId, navController, userViewModel, missatgesViewModel)
                            }
                            composable(route = Screens.Pantalla_TermesCondicions.name) {
                                Pantalla_TermesCondicions()
                            }
                            composable(route = Screens.Pantalla_Configuracio.name) {
                                Pantalla_Configuracio(navController)
                            }
                            composable(route = Screens.Pantalla_NotificacionsConfig.name) {
                                Pantalla_NotificacionsConfig()
                            }
                            composable(route = Screens.Pantalla_Audio.name) {
                                Pantalla_Audio()
                            }
                            composable(route = Screens.Pantalla_Privacitat.name) {
                                Pantalla_Privacitat()
                            }
                            composable(route = Screens.Pantalla_Cookies.name) {
                                Pantalla_Cookies()
                            }
                            composable(route = "${Screens.Pantalla_Viatge.name}/{viatgeId}") {
                                backStackEntry -> val viatgeId = backStackEntry.arguments?.getString("viatgeId")?.toLong()
                                viatgeViewModel.viatge_Id = viatgeId
                                Pantalla_Viatge(navController,userViewModel, viatgeViewModel,missatgesViewModel)
                            }
                            composable(route = Screens.Pantalla_Perfil_Estadistica.name) {
                                Pantalla_Perfil_Estadistica( navController,userViewModel,comunitatsViewModel,paisosViewModel)
                            }
                            composable("${Screens.Pantalla_Perfil_Extern.name}/{usuariId}", arguments = listOf(navArgument("usuariId") { type = NavType.LongType })
                            ) { backStackEntry -> val id = backStackEntry.arguments?.getLong("usuariId")?: 0L
                                Pantalla_Perfil_Extern(navController,userViewModel ,id,comunitatsViewModel,paisosViewModel)
                            }
                            composable("${Screens.Pantalla_Perfil_Estadistica_Extern.name}/{usuariId}", arguments = listOf(navArgument("usuariId") { type = NavType.LongType })
                            ) { backStackEntry -> val id = backStackEntry.arguments?.getLong("usuariId")?: 0L
                                Pantalla_Perfil_Estadistica_Extern(navController, userViewModel, id,comunitatsViewModel,paisosViewModel)
                            }
                        }

                    }
                }
            }
        }
    }

}


