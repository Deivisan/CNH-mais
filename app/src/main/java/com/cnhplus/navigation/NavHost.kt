package com.cnhplus.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cnhplus.screens.auth.LoginScreen
import com.cnhplus.screens.auth.RegisterScreen
import com.cnhplus.screens.auth.RegisterSuccessScreen
import com.cnhplus.screens.auth.SelectRoleScreen
import com.cnhplus.screens.candidato.*
import com.cnhplus.screens.instrutor.*
import com.cnhplus.screens.WelcomeScreen
import com.cnhplus.screens.chat.ChatScreen
import com.cnhplus.screens.denuncia.DenunciaScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun CNHNavHost(
    context: Context,
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ===== WELCOME =====
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onComplete = { navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }}
            )
        }

        // ===== AUTH =====
        composable(Screen.Login.route) {
            LoginScreen(
                context = context,
                onLoginSuccess = { role -> navigateByRole(navController, role) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegistered = { navController.navigate(Screen.RegisterSuccess.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.RegisterSuccess.route) {
            RegisterSuccessScreen(
                onContinue = { navController.navigate(Screen.SelectRole.route) {
                    popUpTo(Screen.RegisterSuccess.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.SelectRole.route) {
            SelectRoleScreen(
                onRoleSelected = { role -> navigateByRole(navController, role) }
            )
        }

        // ===== CANDIDATO ONBOARDING FLOW =====
        composable(Screen.PerfilCompleto.route) {
            PerfilCompletoScreen(
                onComplete = { navController.navigate(Screen.OnboardingCandidato.route) {
                    popUpTo(Screen.PerfilCompleto.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.OnboardingCandidato.route) {
            OnboardingCandidatoScreen(
                onComplete = { navController.navigate(Screen.PerfilComportamental.route) }
            )
        }

        composable(Screen.PerfilComportamental.route) {
            PerfilComportamentalScreen(
                onComplete = { navController.navigate(Screen.RecomendacaoAulas.route) }
            )
        }

        composable(Screen.RecomendacaoAulas.route) {
            RecomendacaoAulasScreen(
                onAccept = { navController.navigate(Screen.CandidatoHome.route) {
                    popUpTo(Screen.OnboardingCandidato.route) { inclusive = true }
                }}
            )
        }

        // ===== CANDIDATO MAIN (with bottom nav) =====
        composable(Screen.CandidatoHome.route) {
            CandidatoScaffold(navController, Screen.CandidatoHome.route) {
                CandidatoHomeScreen(
                    onNavigateToPerfil = { navController.navigate(Screen.CandidatoPerfil.route) },
                    onNavigateToAulas = { navController.navigate(Screen.CandidatoAulas.route) },
                    onNavigateToMatch = { navController.navigate(Screen.CandidatoMatch.route) },
                    onNavigateToPagamento = { navController.navigate(Screen.CandidatoPagamento.route) }
                )
            }
        }

        composable(Screen.CandidatoAulas.route) {
            CandidatoScaffold(navController, Screen.CandidatoAulas.route) {
                CandidatoAulasScreen(
                    onNavigateToChat = { aulaId, receiverId, receiverName ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("receiverId", receiverId)
                            set("receiverName", receiverName)
                        }
                        navController.navigate(Screen.Chat.createRoute(aulaId))
                    },
                    onNavigateToDenuncia = { aulaId, denunciadoId, denunciadoNome ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("denunciadoId", denunciadoId)
                            set("denunciadoNome", denunciadoNome)
                        }
                        navController.navigate(Screen.Denuncia.createRoute(aulaId))
                    }
                )
            }
        }

        composable(Screen.CandidatoMatch.route) {
            CandidatoScaffold(navController, Screen.CandidatoMatch.route) {
                CandidatoMatchScreen()
            }
        }

        composable(Screen.CandidatoPagamento.route) {
            CandidatoScaffold(navController, Screen.CandidatoPagamento.route) {
                CandidatoPagamentoScreen()
            }
        }

        composable(Screen.CandidatoPerfil.route) {
            CandidatoScaffold(navController, Screen.CandidatoPerfil.route) {
                CandidatoPerfilScreen()
            }
        }


        // ===== INSTRUTOR ONBOARDING =====
        composable(Screen.PerfilInstrutor.route) {
            PerfilInstrutorScreen(
                onComplete = { navController.navigate(Screen.InstrutorHome.route) {
                    popUpTo(Screen.PerfilInstrutor.route) { inclusive = true }
                }},
                onSkip = { navController.navigate(Screen.InstrutorHome.route) {
                    popUpTo(Screen.PerfilInstrutor.route) { inclusive = true }
                }}
            )
        }

        // ===== INSTRUTOR MAIN (with bottom nav) =====
        composable(Screen.InstrutorHome.route) {
            InstrutorScaffold(navController, Screen.InstrutorHome.route) {
                InstrutorHomeScreen()
            }
        }

        composable(Screen.InstrutorAgenda.route) {
            InstrutorScaffold(navController, Screen.InstrutorAgenda.route) {
                InstrutorAgendaScreen()
            }
        }

        composable(Screen.InstrutorAulas.route) {
            InstrutorScaffold(navController, Screen.InstrutorAulas.route) {
                InstrutorAulasScreen(
                    onNavigateToChat = { aulaId, receiverId, receiverName ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("receiverId", receiverId)
                            set("receiverName", receiverName)
                        }
                        navController.navigate(Screen.Chat.createRoute(aulaId))
                    }
                )
            }
        }

        composable(Screen.InstrutorFinanceiro.route) {
            InstrutorScaffold(navController, Screen.InstrutorFinanceiro.route) {
                InstrutorFinanceiroScreen()
            }
        }

        composable(Screen.InstrutorPerfil.route) {
            InstrutorScaffold(navController, Screen.InstrutorPerfil.route) {
                InstrutorPerfilScreen()
            }
        }

        // ===== CHAT & DENÚNCIA (shared) =====
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("aulaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val aulaId = backStackEntry.arguments?.getString("aulaId") ?: ""
            // Get params from previous navigation
            ChatScreen(
                aulaId = aulaId,
                receiverId = backStackEntry.savedStateHandle.get<String>("receiverId") ?: "",
                receiverName = backStackEntry.savedStateHandle.get<String>("receiverName") ?: "Chat",
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Denuncia.route,
            arguments = listOf(navArgument("aulaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val aulaId = backStackEntry.arguments?.getString("aulaId") ?: ""
            DenunciaScreen(
                aulaId = aulaId,
                denunciadoId = backStackEntry.savedStateHandle.get<String>("denunciadoId") ?: "",
                denunciadoNome = backStackEntry.savedStateHandle.get<String>("denunciadoNome") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun navigateByRole(navController: NavHostController, role: String) {
    when (role) {
        "candidato" -> navController.navigate(Screen.PerfilCompleto.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
        "instrutor" -> navController.navigate(Screen.PerfilInstrutor.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }
}

// ==================== BOTTOM NAV SCFOLDS ====================

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

@Composable
private fun CandidatoScaffold(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    val tabs = listOf(
        TabItem(Screen.CandidatoHome.route, "Home", Icons.Default.Home),
        TabItem(Screen.CandidatoAulas.route, "Aulas", Icons.Default.School),
        TabItem(Screen.CandidatoMatch.route, "Match", Icons.Default.AutoAwesome),
        TabItem(Screen.CandidatoPerfil.route, "Perfil", Icons.Default.Person)
    )
    Scaffold(
        bottomBar = {
            BottomAppBar {
                tabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navController.navigate(tab.route) {
                                launchSingleTop = true
                                popUpTo(Screen.CandidatoHome.route)
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        content()
    }
}

@Composable
private fun InstrutorScaffold(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    val tabs = listOf(
        TabItem(Screen.InstrutorHome.route, "Home", Icons.Default.Home),
        TabItem(Screen.InstrutorAgenda.route, "Agenda", Icons.Default.CalendarMonth),
        TabItem(Screen.InstrutorAulas.route, "Aulas", Icons.Default.School),
        TabItem(Screen.InstrutorFinanceiro.route, "Financeiro", Icons.Default.Build),
        TabItem(Screen.InstrutorPerfil.route, "Perfil", Icons.Default.Person)
    )
    Scaffold(
        bottomBar = {
            BottomAppBar {
                tabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navController.navigate(tab.route) {
                                launchSingleTop = true
                                popUpTo(Screen.InstrutorHome.route)
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        content()
    }
}
