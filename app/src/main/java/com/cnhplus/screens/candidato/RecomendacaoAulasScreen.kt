package com.cnhplus.screens.candidato

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.PrimaryLight
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.TextPrimary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.Error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.cnhplus.*
import com.cnhplus.data.PacoteCandidato
import com.cnhplus.ui.theme.LocalAppState
/**
 * Tela de recomendação de aulas - mostra quantas aulas o sistema recomenda
 * e permite ao candidato aceitar ou ajustar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecomendacaoAulasScreen(
    onAccept: (aulas: Int) -> Unit
) {
    val state = LocalAppState.current
    var aulasSelecionadas by remember { mutableStateOf(20) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val userId = state.currentUser.value?.id

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(Primary, PrimaryLight, Secondary))
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        Text(
            text = "📊",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Recomendação do Sistema",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recomendamos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                
                Text(
                    text = "$aulasSelecionadas aulas",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                
                Text(
                    text = "para o seu perfil",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                HorizontalDivider()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Options for lesson count
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val options = listOf(
                        "Básico" to 12,
                        "Recomendado" to 20,
                        "Completo" to 30
                    )
                    
                    options.forEach { (label, count) ->
                        val isSelected = aulasSelecionadas == count
                        Card(
                            onClick = { aulasSelecionadas = count },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Primary else Color(0xFFF5F7FA)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                                    Text(
                                        text = "$count aulas de 50 min",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextSecondary
                                    )
                                if (isSelected) {
                                    Text(
                                        text = "✓",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "💡 Dica: Você pode ajustar depois, mas recomendamos seguir a sugestão para melhores resultados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        loading = true
                        val pacote = PacoteCandidato(
                            aulasCompradas = 0,
                            aulasRestantes = 0,
                            aulasRecomendadas = aulasSelecionadas
                        )
                        userId?.let { uid ->
                            state.candidatoRepo.updateCandidato(uid, mapOf(
                                "pacote" to Json.encodeToString(pacote)
                            )).fold(
                                onSuccess = {
                                    loading = false
                                    onAccept(aulasSelecionadas)
                                },
                                onFailure = { e ->
                                    loading = false
                                    error = e.message ?: "Erro ao salvar"
                                }
                            )
                        } ?: run {
                            loading = false
                            error = "Usuário não autenticado"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Prosseguir para Pagamento", style = MaterialTheme.typography.titleMedium)
                    }
                }
                
                error?.let { errMsg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errMsg,
                        color = Error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
