package com.cnhplus.screens.instrutor

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.Error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.*
import com.cnhplus.data.AulaDto
import com.cnhplus.data.BannerDto
import com.cnhplus.ui.components.BannerCarrossel
import com.cnhplus.ui.components.FooterBanners
import com.cnhplus.ui.theme.LocalAppState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorAulasScreen(
    onNavigateToChat: (aulaId: String, receiverId: String, receiverName: String) -> Unit = { _, _, _ -> }
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    var todasAulas by remember { mutableStateOf<List<AulaDto>>(emptyList()) }
    var banners by remember { mutableStateOf<List<BannerDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    var showConfirmDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    
    // Carregar aulas e banners
    LaunchedEffect(Unit) {
        // Fetch banners
        app.bannerRepo.getActiveBanners().fold(
            onSuccess = { banners = it },
            onFailure = { /* silently fail */ }
        )
        
        val userId = app.currentUser.value?.id ?: run { isLoading = false; return@LaunchedEffect }
        app.aulaRepo.getAulasByInstrutor(userId).fold(
            onSuccess = { a -> 
                todasAulas = a.sortedBy { it.data_hora }
                isLoading = false 
            },
            onFailure = { isLoading = false }
        )
    }
    
    // Filtrar por tab
    val aulasAguardando = todasAulas.filter { it.status == "agendada" }
    val aulasEmCurso = todasAulas.filter { it.status == "em_andamento" }
    val aulasRealizadas = todasAulas.filter { it.status == "concluida" }
    
    val tabs = listOf("Aguardando", "Em Curso", "Realizadas")
    val aulasAtivas = when(selectedTab) {
        0 -> aulasAguardando
        1 -> aulasEmCurso
        2 -> aulasRealizadas
        else -> emptyList()
    }
    
    // Dialog de confirmação
    showConfirmDialog?.let { (aulaId, acao) ->
        AlertDialog(
            onDismissRequest = { showConfirmDialog = null },
            title = { Text(if (acao == "iniciar") "Iniciar Aula?" else "Concluir Aula?") },
            text = { Text(if (acao == "iniciar") "A aula será marcada como em andamento." else "A aula será marcada como concluída e o aluno será notificado para confirmar.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val result = if (acao == "iniciar") {
                                app.aulaRepo.updateAula(aulaId, mapOf("status" to "em_andamento"))
                            } else {
                                app.aulaRepo.completeAula(aulaId)
                            }
                            result.fold(
                                onSuccess = {
                                    // Recarregar aulas
                                    val userId = app.currentUser.value?.id ?: return@launch
                                    app.aulaRepo.getAulasByInstrutor(userId).fold(
                                        onSuccess = { todasAulas = it.sortedBy { a -> a.data_hora } },
                                        onFailure = {}
                                    )
                                },
                                onFailure = {}
                            )
                            showConfirmDialog = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (acao == "iniciar") Success else Primary)
                ) {
                    Text(if (acao == "iniciar") "Iniciar" else "Concluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    if (isLoading) { 
        Box(Modifier.fillMaxSize(), Alignment.Center) { 
            CircularProgressIndicator(color = Secondary) 
        }
        return 
    }
    
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Minhas Aulas") }, 
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary, 
                    titleContentColor = Color.White
                )
            ) 
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Primary,
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge
                            ) 
                        }
                    )
                }
            }
            
            // Lista de aulas
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (aulasAtivas.isEmpty()) { 
                    item { 
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(32.dp), 
                            Alignment.Center
                        ) { 
                            Text(
                                text = when(selectedTab) {
                                    0 -> "Nenhuma aula aguardando"
                                    1 -> "Nenhuma aula em andamento"
                                    2 -> "Nenhuma aula concluída"
                                    else -> "Sem aulas"
                                },
                                color = TextSecondary
                            ) 
                        } 
                    }
                } else { 
                    items(aulasAtivas) { aula -> 
                        if (aula.id != null) {
                            AulaCard(
                                aula = aula,
                                showActions = selectedTab < 2, // Apenas Aguardando e Em Curso
                                onIniciar = { showConfirmDialog = aula.id to "iniciar" },
                                onConcluir = { showConfirmDialog = aula.id to "concluir" },
                                onChatClick = { onNavigateToChat(aula.id!!, aula.candidato_id ?: "", "Candidato") }
                            )
                        }
                    } 
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun AulaCard(
    aula: AulaDto,
    showActions: Boolean,
    onIniciar: () -> Unit,
    onConcluir: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = aula.data_hora ?: "N/A",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${aula.duracao ?: 50}min • ${aula.tipo ?: "Prática urbana"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Surface(
                    color = when(aula.status) {
                        "agendada" -> Warning.copy(0.2f)
                        "em_andamento" -> Accent.copy(0.2f)
                        "concluida" -> Success.copy(0.2f)
                        else -> TextSecondary.copy(0.2f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = when(aula.status) {
                            "agendada" -> "Agendada"
                            "em_andamento" -> "Em Curso"
                            "concluida" -> "Concluída"
                            else -> aula.status ?: "N/A"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when(aula.status) {
                            "agendada" -> Warning
                            "em_andamento" -> Accent
                            "concluida" -> Success
                            else -> TextSecondary
                        },
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Warning,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "R$ ${String.format("%.2f", aula.valor ?: 0.0)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            
            // Botões de ação
            if (showActions && aula.id != null) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botão Chat (sempre visível em agendada/em_andamento)
                    OutlinedButton(
                        onClick = onChatClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Secondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Chat")
                    }
                    
                    if (aula.status == "agendada") {
                        Button(
                            onClick = onIniciar,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Success)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Iniciar Aula")
                        }
                    }
                    
                    if (aula.status == "em_andamento") {
                        Button(
                            onClick = onConcluir,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Concluir Aula")
                        }
                    }
                }
            }
        }
    }
}
