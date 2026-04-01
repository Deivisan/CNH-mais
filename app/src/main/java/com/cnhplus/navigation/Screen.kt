package com.cnhplus.navigation

/**
 * Sealed class com todas as rotas do app.
 */
sealed class Screen(val route: String) {
    // Welcome & Onboarding First Run
    object Welcome : Screen("welcome")
    
    // Auth
    object Login : Screen("login")
    object Register : Screen("register")
    object RegisterSuccess : Screen("register_success")
    object SelectRole : Screen("select_role")
    
    // Candidato Onboarding
    object OnboardingCandidato : Screen("onboarding_candidato")
    object PerfilComportamental : Screen("perfil_comportamental")
    object RecomendacaoAulas : Screen("recomendacao_aulas")
    
    // Candidato Main
    object CandidatoHome : Screen("candidato_home")
    object CandidatoPerfil : Screen("candidato_perfil")
    object CandidatoAulas : Screen("candidato_aulas")
    object CandidatoMatch : Screen("candidato_match")
    object CandidatoPagamento : Screen("candidato_pagamento")
    
    // Instrutor Main
    object InstrutorHome : Screen("instrutor_home")
    object InstrutorPerfil : Screen("instrutor_perfil")
    object InstrutorAgenda : Screen("instrutor_agenda")
    object InstrutorAulas : Screen("instrutor_aulas")
    object InstrutorFinanceiro : Screen("instrutor_financeiro")
    
    // Admin (mantido mas não prioridade)
    object AdminHome : Screen("admin_home")
    object AdminInstrutores : Screen("admin_instrutores")
    object AdminAlunos : Screen("admin_alunos")
    object AdminAulas : Screen("admin_aulas")
    object AdminFinanceiro : Screen("admin_financeiro")
    object AdminConfig : Screen("admin_config")
}
