package com.cnhplus.screens.candidato

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.PrimaryLight
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.SurfaceColor
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.TextPrimary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.Error

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
import com.cnhplus.data.CandidatoDto
import com.cnhplus.data.InstrutorDto
import com.cnhplus.ui.theme.LocalAppState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatoHomeScreen(
    onNavigateToPerfil: () -> Unit,
    onNavigateToAulas: () -> Unit,
    onNavigateToMatch: () -> Unit,
    onNavigateToPagamento: () -> Unit
) {
    val app = LocalAppState.current
    
    var candidato by remember { mutableStateOf<CandidatoDto?>(null) }
    var instrutor by remember { mutableStateOf<InstrutorDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    // Fetch candidato e instrutor de verdade via Supabase
    LaunchedEffect(Unit) {
        try {
            val userId = app.currentUser.value?.id ?: run {
                errorMsg = "Usuário não autenticado"
                isLoading = false
                return@LaunchedEffect
            }
            
            // Fetch candidato
            app.candidatoRepo.getCandidato(userId).fold(
                onSuccess = { c ->
                    candidato = c
                    // Se tem instrutor_id, buscar instrutor real
                    c?.instrutor_id?.let { instrutorId ->
                        app.instrutorRepo.getInstrutor(instrutorId).fold(
                            onSuccess = { i ->
                                instrutor = i
                                isLoading = false
                            },
                            onFailure = {
                                isLoading = false
                            }
                        )
                    } ?: run {
                        isLoading = false
                    }
                },
                onFailure = { e ->
                    errorMsg = e.message ?: "Erro ao carregar dados"
                    isLoading = false
                }
            )
        } catch (e: Exception) {
            errorMsg = e.message
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
    
    if (errorMsg != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Erro: ${errorMsg}", color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { isLoading = true; errorMsg = null }) {
                    Text("Tentar novamente")
                }
            }
        }
        return
    }
    
    val candidatoName = candidato?.let { app.currentUser.value?.email?.split("@")?.get(0) ?: "Usuário" } ?: "Usuário"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Olá, ${candidatoName.split(".").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "Usuário"}!") },
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
                .background(SurfaceColor)
        ) {
            item {
                // Progress Card — usando dados reais do candidato
                candidato?.let { cand ->
                    val pacote = cand.getPacote()
                    val aulasFeitas = pacote.aulasCompradas - pacote.aulasRestantes
                    val progressValue = if (pacote.aulasCompradas > 0) {
                        aulasFeitas.toFloat() / pacote.aulasCompradas
                    } else {
                        0f
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Seu Progresso",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            LinearProgressIndicator(
                                progress = { progressValue },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = Secondary,
                                trackColor = SurfaceColor
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$aulasFeitas aulas feitas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${pacote.aulasRestantes} restantes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            // Info resumida do pacote (feedback cliente — Áudio 6)
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Resumo do Pacote",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Total de aulas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${pacote.aulasCompradas}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Recomendadas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${pacote.aulasRecomendadas}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Instrutor Card — usar instrutor real ou placeholder
                instrutor?.let { inst ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Primary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Accent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = inst.id.take(2).uppercase(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Seu Instrutor",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = inst.biografia?.take(30) ?: "Instrutor",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Warning,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${inst.nota_media ?: 0.0}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White
                                    )
                                }
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                } ?: run {
                    // Placeholder se não tiver instrutor vinculado
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Accent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    tint = Primary,
                                    contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sem instrutor",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Encontre um novo instrutor",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            IconButton(onClick = onNavigateToMatch) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                CandidatoMenuCard(
                    icon = Icons.Default.CalendarMonth,
                    title = "Minhas Aulas",
                    subtitle = "Ver aulas agendadas e históricas",
                    onClick = onNavigateToAulas
                )
            }
            
            item {
                CandidatoMenuCard(
                    icon = Icons.Default.Search,
                    title = "Encontrar Instrutor",
                    subtitle = "Buscar novo instrutor",
                    onClick = onNavigateToMatch
                )
            }
            
            item {
                CandidatoMenuCard(
                    icon = Icons.Default.Payment,
                    title = "Comprar Aulas",
                    subtitle = "Adquirir pacote de aulas",
                    onClick = onNavigateToPagamento
                )
            }
            
            item {
                CandidatoMenuCard(
                    icon = Icons.Default.Person,
                    title = "Meu Perfil",
                    subtitle = "Editar informações pessoais",
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
fun CandidatoMenuCard(
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
