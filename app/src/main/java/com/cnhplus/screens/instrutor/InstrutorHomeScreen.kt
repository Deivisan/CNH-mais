package com.cnhplus.screens.instrutor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.*
import com.cnhplus.data.InstrutorDto
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorHomeScreen(
    onNavigateToPerfil: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToAulas: () -> Unit = {},
    onNavigateToFinanceiro: () -> Unit = {}
) {
    val app = LocalAppState.current
    
    var instrutor by remember { mutableStateOf<InstrutorDto?>(null) }
    var aulasHoje by remember { mutableStateOf(0) }
    var ganhosDia by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Fetch instrutor e dados de dashboard
    LaunchedEffect(Unit) {
        try {
            val userId = app.currentUser.value?.id ?: run {
                isLoading = false
                return@LaunchedEffect
            }
            
            app.instrutorRepo.getInstrutor(userId).fold(
                onSuccess = { inst ->
                    instrutor = inst
                    
                    // Fetch aulas de hoje
                    app.aulaRepo.getAulasByInstrutor(userId).fold(
                        onSuccess = { aulas ->
                            val hoje = java.time.LocalDate.now().toString()
                            aulasHoje = aulas.count { it.data_hora?.startsWith(hoje) == true }
                            ganhosDia = aulas
                                .filter { it.data_hora?.startsWith(hoje) == true && it.status == "concluida" }
                                .sumOf { it.valor ?: 0.0 }
                            isLoading = false
                        },
                        onFailure = {
                            isLoading = false
                        }
                    )
                },
                onFailure = {
                    isLoading = false
                }
            )
        } catch (e: Exception) {
            isLoading = false
        }
    }
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Secondary)
        }
        return
    }
    
    val instrutorName = instrutor?.biografia?.take(20) ?: app.currentUser.value?.email?.split("@")?.get(0) ?: "Instrutor"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Olá, ${instrutorName.split(" ").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "Instrutor"}!") },
                actions = {
                    IconButton(onClick = onNavigateToPerfil) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                // Stats Card — Dashboard
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Primary)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Resumo Hoje",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$aulasHoje",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Accent
                                )
                                Text(
                                    text = "Aulas hoje",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "R$ ${String.format("%.2f", ganhosDia)}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Accent
                                )
                                Text(
                                    text = "Ganhos hoje",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${instrutor?.nota_media ?: 0.0}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Accent
                                )
                                Text(
                                    text = "Avaliação",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ações Rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                InstrutorMenuCard(
                    icon = Icons.Default.CalendarMonth,
                    title = "Minha Agenda",
                    subtitle = "Ver e gerenciar aulas agendadas",
                    onClick = onNavigateToAgenda
                )
            }
            
            item {
                InstrutorMenuCard(
                    icon = Icons.Default.School,
                    title = "Minhas Aulas",
                    subtitle = "Aulas realizadas e avaliações",
                    onClick = onNavigateToAulas
                )
            }
            
            item {
                InstrutorMenuCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Financeiro",
                    subtitle = "Ganhos e repasses",
                    onClick = onNavigateToFinanceiro
                )
            }
            
            item {
                InstrutorMenuCard(
                    icon = Icons.Default.Person,
                    title = "Meu Perfil",
                    subtitle = "Editar informações profissionais",
                    onClick = onNavigateToPerfil
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InstrutorMenuCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Secondary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}
