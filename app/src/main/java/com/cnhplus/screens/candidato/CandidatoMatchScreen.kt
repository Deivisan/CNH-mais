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
import androidx.compose.foundation.lazy.items
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
import com.cnhplus.data.BannerDto
import com.cnhplus.data.CandidatoDto
import com.cnhplus.data.InstrutorDto
import com.cnhplus.ui.components.BannerCarrossel
import com.cnhplus.ui.components.FooterBanners
import com.cnhplus.ui.theme.LocalAppState
import kotlin.math.pow
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatoMatchScreen() {
    val app = LocalAppState.current
    
    var instrutoresComScore by remember { mutableStateOf<List<Pair<InstrutorDto, Double>>>(emptyList()) }
    var candidato by remember { mutableStateOf<CandidatoDto?>(null) }
    var banners by remember { mutableStateOf<List<BannerDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    // Fetch instrutores, candidato e banners
    LaunchedEffect(Unit) {
        // Fetch banners
        app.bannerRepo.getActiveBanners().fold(
            onSuccess = { banners = it },
            onFailure = { /* silently fail */ }
        )
        
        try {
            val userId = app.currentUser.value?.id ?: run {
                errorMsg = "Usuário não autenticado"
                isLoading = false
                return@LaunchedEffect
            }
            
            // Fetch candidato
            app.candidatoRepo.getCandidato(userId).fold(
                onSuccess = { c ->
                    if (c == null) {
                        errorMsg = "Dados do candidato não encontrados"
                        isLoading = false
                        return@fold
                    }
                    candidato = c
                    
                    // Fetch todos os instrutores
                    app.instrutorRepo.getAllInstrutores().fold(
                        onSuccess = { instrutores ->
                            // Calcular score para cada instrutor
                            val scored = instrutores
                                .filter { it.status == "ativo" || it.status == "aprovado" }
                                .map { instrutor ->
                                    val score = calcularScoreMatch(c, instrutor)
                                    instrutor to score
                                }
                                .sortedByDescending { it.second }
                            
                            instrutoresComScore = scored
                            isLoading = false
                        },
                        onFailure = {
                            errorMsg = "Erro ao carregar instrutores"
                            isLoading = false
                        }
                    )
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Encontrar Instrutor") },
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Accent.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Match Inteligente: Encontramos os melhores instrutores para seu perfil!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
            
            if (instrutoresComScore.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum instrutor disponível",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                items(instrutoresComScore) { (instrutor, score) ->
                    InstrutorMatchCard(instrutor, score)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Algoritmo de Match Scoring conforme SPEC.md
 * score = (especialidade_match * 30) +
 *         (nota_media * 20) +
 *         (pontualidade * 20) +
 *         (1 - cancelamentos_normalizado * 10) +
 *         (horas_experiencia_normalizado * 10) +
 *         (distancia_normalizado * 10)
 */
fun calcularScoreMatch(candidato: CandidatoDto, instrutor: InstrutorDto): Double {
    var score = 0.0
    
    // 1. Especialidade match (30 pontos)
    val perfil = candidato.getPerfil()
    val especialidadeMatch = when {
        perfil.ansiedade == "alta" && instrutor.especialidades.contains("ansioso") -> 30.0
        perfil.reprovou && instrutor.especialidades.contains("reprovado") -> 30.0
        perfil.experiencia == "nunca" && instrutor.especialidades.contains("iniciante") -> 30.0
        else -> 15.0
    }
    score += especialidadeMatch
    
    // 2. Nota média (20 pontos)
    val notaScore = (instrutor.nota_media ?: 0.0) / 5.0 * 20.0
    score += notaScore
    
    // 3. Pontualidade (20 pontos)
    val pontualidadeScore = (instrutor.pontualidade ?: 100.0) / 100.0 * 20.0
    score += pontualidadeScore
    
    // 4. Cancelamentos (10 pontos negativos)
    val cancelamentosScore = when (instrutor.cancelamentos) {
        "baixo" -> 10.0
        "medio" -> 5.0
        else -> 0.0
    }
    score += cancelamentosScore
    
    // 5. Horas de experiência (10 pontos)
    val horasNormalizadas = minOf((instrutor.horas_trabalhadas ?: 0) / 100.0, 1.0)
    score += horasNormalizadas * 10.0
    
    // 6. Distância (10 pontos) — simplificado: mesma cidade = 10 pontos
    val distanciaScore = if (instrutor.cidade == candidato.cidade) 10.0 else 5.0
    score += distanciaScore
    
    return score
}

@Composable
fun InstrutorMatchCard(instrutor: InstrutorDto, score: Double) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Secondary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = instrutor.id.take(2).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = instrutor.biografia?.take(20) ?: "Instrutor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Warning,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${instrutor.nota_media ?: 0.0}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
                
                // Match Score badge
                Surface(
                    color = when {
                        score >= 80.0 -> Success.copy(alpha = 0.2f)
                        score >= 60.0 -> Accent.copy(alpha = 0.2f)
                        else -> Warning.copy(alpha = 0.2f)
                    },
                    shape = CircleShape
                ) {
                    Text(
                        text = "${score.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            score >= 80.0 -> Success
                            score >= 60.0 -> Secondary
                            else -> Warning
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cidade",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = instrutor.cidade ?: "N/A",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column {
                    Text(
                        text = "Horas",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "${instrutor.horas_trabalhadas ?: 0}h",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column {
                    Text(
                        text = "Alunos",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "${instrutor.alunos_atendidos ?: 0}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary)
            ) {
                Text("Ver Perfil")
            }
        }
    }
}
