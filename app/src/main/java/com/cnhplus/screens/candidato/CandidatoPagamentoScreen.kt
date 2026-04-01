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
import com.cnhplus.data.PagamentoDto
import com.cnhplus.ui.theme.LocalAppState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatoPagamentoScreen() {
    val app = LocalAppState.current
    
    var selectedPackage by remember { mutableIntStateOf(0) }
    var isProcessing by remember { mutableStateOf(false) }
    var successMsg by remember { mutableStateOf<String?>(null) }
    
    val packages = listOf(
        Triple("Básico", 10, 99.0),
        Triple("Completo", 20, 179.0),
        Triple("Premium", 30, 239.0)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprar Aulas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SurfaceColor)
                .padding(16.dp)
        ) {
            Text(
                text = "Escolha seu Pacote",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Compre aulas e encontre seu instrutor ideal",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (successMsg != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Success.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Success
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Compra Realizada!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = successMsg ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            packages.forEachIndexed { index, (name, qty, price) ->
                Card(
                    onClick = { selectedPackage = index },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPackage == index) Secondary.copy(alpha = 0.1f) else Color.White
                    ),
                    border = if (selectedPackage == index) CardDefaults.outlinedCardBorder() else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPackage == index,
                            onClick = { selectedPackage = index },
                            colors = RadioButtonDefaults.colors(selectedColor = Secondary)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$qty aulas • R$ ${String.format("%.2f", price / qty)}/aula",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        Text(
                            text = "R$ ${String.format("%.2f", price)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    isProcessing = true
                    // Criar pagamento via Supabase (Mercado Pago ref)
                    val (name, qty, price) = packages[selectedPackage]
                    val userId = app.currentUser.value?.id ?: return@Button
                    
                    val pagamento = PagamentoDto(
                        candidato_id = userId,
                        valor = price,
                        status = "pendente",
                        tipo = "pacote",
                        pacotes = qty
                    )
                    
                    app.pagamentoRepo.createPagamento(pagamento).fold(
                        onSuccess = {
                            successMsg = "Pacote de $qty aulas adicionado! Aguardando confirmação de pagamento."
                            isProcessing = false
                        },
                        onFailure = {
                            successMsg = "Erro ao processar pagamento"
                            isProcessing = false
                        }
                    )
                },
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comprar com Segurança",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Pagamento processado pelo Mercado Pago",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
