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
import com.cnhplus.ui.theme.LocalAppState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatoPerfilScreen() {
    val app = LocalAppState.current
    
    var candidato by remember { mutableStateOf<CandidatoDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    // Fetch candidato profile
    LaunchedEffect(Unit) {
        try {
            val userId = app.currentUser.value?.id ?: run {
                errorMsg = "Usuário não autenticado"
                isLoading = false
                return@LaunchedEffect
            }
            
            app.candidatoRepo.getCandidato(userId).fold(
                onSuccess = { c ->
                    candidato = c
                    isLoading = false
                },
                onFailure = { e ->
                    errorMsg = e.message ?: "Erro ao carregar perfil"
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
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
                // Avatar e info básica
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Primary)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = candidato?.let { app.currentUser.value?.email?.take(1)?.uppercase() ?: "U" } ?: "U",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = app.currentUser.value?.email?.split("@")?.get(0) ?: "Usuário",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = candidato?.cidade ?: "Cidade não definida",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Informações Pessoais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                candidato?.let { cand ->
                    // CPF
                    ProfileField(
                        icon = Icons.Default.Badge,
                        label = "CPF",
                        value = cand.cpf?.mask() ?: "Não informado"
                    )
                    
                    // Telefone
                    ProfileField(
                        icon = Icons.Default.Phone,
                        label = "Celular",
                        value = cand.celular ?: "Não informado"
                    )
                    
                    // Email
                    ProfileField(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = app.currentUser.value?.email ?: "Não informado"
                    )
                    
                    // Cidade
                    ProfileField(
                        icon = Icons.Default.LocationOn,
                        label = "Cidade",
                        value = cand.cidade ?: "Não informado"
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Perfil Comportamental",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                candidato?.let { cand ->
                    val perfil = cand.getPerfil()
                    
                    // Experiência
                    ProfileField(
                        icon = Icons.Default.School,
                        label = "Experiência",
                        value = when (perfil.experiencia) {
                            "nunca" -> "Nunca dirigi"
                            "poucas_vezes" -> "Poucas vezes"
                            else -> "Frequente"
                        }
                    )
                    
                    // Ansiedade
                    ProfileField(
                        icon = Icons.Default.Favorite,
                        label = "Nível de Ansiedade",
                        value = perfil.ansiedade.replaceFirstChar { it.uppercase() }
                    )
                    
                    // Objetivo
                    ProfileField(
                        icon = Icons.Default.Flag,
                        label = "Objetivo",
                        value = when (perfil.objetivo) {
                            "aprender_calma" -> "Aprender com calma"
                            "passar_rapido" -> "Passar rápido"
                            else -> "Ficar bom na direção"
                        }
                    )
                    
                    // Carro próprio
                    ProfileField(
                        icon = Icons.Default.DirectionsCar,
                        label = "Tem carro?",
                        value = when (perfil.temCarro) {
                            "sim" -> "Sim"
                            "nao" -> "Não"
                            else -> "Às vezes"
                        }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Pacote de Aulas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                candidato?.let { cand ->
                    val pacote = cand.getPacote()
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Accent.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Total Comprado",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${pacote.aulasCompradas} aulas",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Utilizadas",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${pacote.aulasCompradas - pacote.aulasRestantes}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Restantes",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "${pacote.aulasRestantes}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        app.logout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sair")
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

fun String.mask(): String {
    return this.replace(Regex(".{1}(?=.{4})"), "*")
}
