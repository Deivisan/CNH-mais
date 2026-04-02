package com.cnhplus.screens.avaliacao

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.SurfaceColor
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.data.AvaliacaoDto
import kotlinx.coroutines.launch

/**
 * Tela de Avaliação pós-aula.
 * Candidato avalia instrutor com nota 1-5 + comentário.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliacaoScreen(
    aulaId: String,
    instrutorId: String,
    instrutorNome: String,
    onBack: () -> Unit
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    
    var nota by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var sucesso by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }
    
    val userId = app.currentUser.value?.id ?: ""
    
    if (sucesso) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Avaliação Enviada!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Obrigado por avaliar $instrutorNome. Sua opinião ajuda outros alunos!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Voltar")
                }
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliar Instrutor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
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
                .background(SurfaceColor),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Como foi sua aula com",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                    Text(
                        text = instrutorNome,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Sua Nota",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Star rating
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= nota) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "$star estrelas",
                                tint = if (star <= nota) Color(0xFFFFC107) else Color.LightGray,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { nota = star }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when(nota) {
                            1 -> "Péssimo 😞"
                            2 -> "Ruim 😕"
                            3 -> "Regular 😐"
                            4 -> "Bom 🙂"
                            5 -> "Excelente 🤩"
                            else -> "Toque para avaliar"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (nota > 0) Success else TextSecondary
                    )
                }
            }
            
            item {
                Column {
                    Text(
                        text = "Comentário (opcional)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { if (it.length <= 500) comentario = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Conte como foi sua experiência...") },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text(
                        text = "${comentario.length}/500",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
            
            item {
                erro?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Button(
                    onClick = {
                        if (nota == 0) {
                            erro = "Selecione uma nota"
                            return@Button
                        }
                        
                        isSubmitting = true
                        scope.launch {
                            val avaliacao = AvaliacaoDto(
                                aula_id = aulaId,
                                candidato_id = userId,
                                instrutor_id = instrutorId,
                                nota = nota,
                                comentario = comentario.trim().takeIf { it.isNotEmpty() }
                            )
                            
                            app.avaliacaoRepo.createAvaliacao(avaliacao).fold(
                                onSuccess = { sucesso = true },
                                onFailure = { e -> erro = e.message ?: "Erro ao enviar avaliação" }
                            )
                            isSubmitting = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enviar Avaliação", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
