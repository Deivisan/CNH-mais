package com.cnhplus.navigation

sealed class Screen(val route: String) {
    // Main
    object SelectRole : Screen("select_role")
    
    // Admin Screens
    object AdminHome : Screen("admin_home")
    object AdminInstrutores : Screen("admin_instrutores")
    object AdminAlunos : Screen("admin_alunos")
    object AdminAulas : Screen("admin_aulas")
    object AdminFinanceiro : Screen("admin_financeiro")
    object AdminConfig : Screen("admin_config")
    
    // Candidato Screens
    object CandidatoHome : Screen("candidato_home")
    object CandidatoPerfil : Screen("candidato_perfil")
    object CandidatoAulas : Screen("candidato_aulas")
    object CandidatoMatch : Screen("candidato_match")
    object CandidatoPagamento : Screen("candidato_pagamento")
    
    // Instrutor Screens
    object InstrutorHome : Screen("instrutor_home")
    object InstrutorPerfil : Screen("instrutor_perfil")
    object InstrutorAgenda : Screen("instrutor_agenda")
    object InstrutorAulas : Screen("instrutor_aulas")
    object InstrutorFinanceiro : Screen("instrutor_financeiro")
}
