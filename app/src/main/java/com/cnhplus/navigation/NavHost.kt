package com.cnhplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cnhplus.screens.admin.*
import com.cnhplus.screens.candidato.*
import com.cnhplus.screens.instrutor.*
import com.cnhplus.screens.SelectRoleScreen

@Composable
fun CNHNavHost(
    navController: NavHostController,
    startDestination: String = Screen.SelectRole.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ===== ROLE SELECTION =====
        composable(Screen.SelectRole.route) {
            SelectRoleScreen(
                onSelectAdmin = { navController.navigate(Screen.AdminHome.route) },
                onSelectCandidato = { navController.navigate(Screen.CandidatoHome.route) },
                onSelectInstrutor = { navController.navigate(Screen.InstrutorHome.route) }
            )
        }
        
        // ===== ADMIN SCREENS =====
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onNavigateToInstrutores = { navController.navigate(Screen.AdminInstrutores.route) },
                onNavigateToAlunos = { navController.navigate(Screen.AdminAlunos.route) },
                onNavigateToAulas = { navController.navigate(Screen.AdminAulas.route) },
                onNavigateToFinanceiro = { navController.navigate(Screen.AdminFinanceiro.route) },
                onNavigateToConfig = { navController.navigate(Screen.AdminConfig.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminInstrutores.route) {
            AdminInstrutoresScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminAlunos.route) {
            AdminAlunosScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminAulas.route) {
            AdminAulasScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminFinanceiro.route) {
            AdminFinanceiroScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AdminConfig.route) {
            AdminConfigScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        // ===== CANDIDATO SCREENS =====
        composable(Screen.CandidatoHome.route) {
            CandidatoHomeScreen(
                onNavigateToPerfil = { navController.navigate(Screen.CandidatoPerfil.route) },
                onNavigateToAulas = { navController.navigate(Screen.CandidatoAulas.route) },
                onNavigateToMatch = { navController.navigate(Screen.CandidatoMatch.route) },
                onNavigateToPagamento = { navController.navigate(Screen.CandidatoPagamento.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CandidatoPerfil.route) {
            CandidatoPerfilScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CandidatoAulas.route) {
            CandidatoAulasScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CandidatoMatch.route) {
            CandidatoMatchScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CandidatoPagamento.route) {
            CandidatoPagamentoScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        // ===== INSTRUTOR SCREENS =====
        composable(Screen.InstrutorHome.route) {
            InstrutorHomeScreen(
                onNavigateToPerfil = { navController.navigate(Screen.InstrutorPerfil.route) },
                onNavigateToAgenda = { navController.navigate(Screen.InstrutorAgenda.route) },
                onNavigateToAulas = { navController.navigate(Screen.InstrutorAulas.route) },
                onNavigateToFinanceiro = { navController.navigate(Screen.InstrutorFinanceiro.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.InstrutorPerfil.route) {
            InstrutorPerfilScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.InstrutorAgenda.route) {
            InstrutorAgendaScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.InstrutorAulas.route) {
            InstrutorAulasScreen(
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.InstrutorFinanceiro.route) {
            InstrutorFinanceiroScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
