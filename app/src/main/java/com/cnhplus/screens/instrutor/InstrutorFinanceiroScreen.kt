package com.cnhplus.screens.instrutor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.cnhplus.data.RepasseDto
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorFinanceiroScreen() {
    val app = LocalAppState.current
    var repasses by remember { mutableStateOf<List<RepasseDto>>(emptyList()) }
    var totalPendente by remember { mutableStateOf(0.0) }
    var totalPago by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        val userId = app.currentUser.value?.id ?: run { isLoading = false; return@LaunchedEffect }
        app.repasseRepo.getRepassesByInstrutor(userId).fold(
            onSuccess = { r ->
                repasses = r.sortedByDescending { it.created_at }
                totalPendente = r.filter { it.status == "pendente" }.sumOf { it.valor ?: 0.0 }
                totalPago = r.filter { it.status == "pago" }.sumOf { it.valor ?: 0.0 }
                isLoading = false
            },
            onFailure = { isLoading = false }
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
                title = { Text("Financeiro") }, 
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary, 
                    titleContentColor = Color.White
                )
            ) 
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
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
                            "Resumo de Ganhos", 
                            style = MaterialTheme.typography.titleMedium, 
                            color = Color.White.copy(0.8f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "R$ ${"%.2f".format(totalPendente)}", 
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ), 
                                    color = Accent
                                )
                                Text(
                                    "Pendente", 
                                    style = MaterialTheme.typography.bodySmall, 
                                    color = Color.White.copy(0.7f)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "R$ ${"%.2f".format(totalPago)}", 
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ), 
                                    color = Accent
                                )
                                Text(
                                    "Pago", 
                                    style = MaterialTheme.typography.bodySmall, 
                                    color = Color.White.copy(0.7f)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Accent)
                        ) {
                            Icon(Icons.Default.AttachMoney, null, Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Solicitar Saque (8 dias)")
                        }
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Histórico de Repasses", 
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
            
            items(repasses) { repasse ->
                RepasseCard(repasse)
            }
            
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RepasseCard(repasse: RepasseDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "R$ ${repasse.valor?.let { "%.2f".format(it) } ?: "0.00"}", 
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    repasse.created_at ?: "N/A", 
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Surface(
                color = if (repasse.status == "pago") Success.copy(0.2f) else Warning.copy(0.2f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = repasse.status?.replaceFirstChar { it.uppercase() } ?: "N/A",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (repasse.status == "pago") Success else Warning,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
