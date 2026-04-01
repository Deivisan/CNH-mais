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
import com.cnhplus.*
import com.cnhplus.data.RepasseDto
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning

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
    
    if (isLoading) { Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Secondary) }; return }
    
    Scaffold(topBar = { TopAppBar(title = { Text("Financeiro") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary, titleContentColor = Color.White)) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(Surface)) {
            item {
                Card(Modifier.fillMaxWidth().padding(16.dp), RoundedCornerShape(16.dp), CardDefaults.cardColors(Primary)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Resumo de Ganhos", MaterialTheme.typography.titleMedium, Color.White.copy(0.8f))
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                            Column(Alignment.CenterHorizontally) {
                                Text("R$ ${"%.2f".format(totalPendente)}", MaterialTheme.typography.headlineSmall, FontWeight.Bold, Accent)
                                Text("Pendente", MaterialTheme.typography.bodySmall, Color.White.copy(0.7f))
                            }
                            Column(Alignment.CenterHorizontally) {
                                Text("R$ ${"%.2f".format(totalPago)}", MaterialTheme.typography.headlineSmall, FontWeight.Bold, Accent)
                                Text("Pago", MaterialTheme.typography.bodySmall, Color.White.copy(0.7f))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {}, Modifier.fillMaxWidth(), ButtonDefaults.buttonColors(Accent)) {
                            Icon(Icons.Default.AttachMoney, null, Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Solicitar Saque (8 dias)")
                        }
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(24.dp))
                Text("Histórico de Repasses", MaterialTheme.typography.titleMedium, FontWeight.Bold, Modifier.padding(16.dp, 0.dp))
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
    Card(Modifier.fillMaxWidth().padding(16.dp, 8.dp), RoundedCornerShape(12.dp), CardDefaults.cardColors(Color.White)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("R$ ${repasse.valor?.let { "%.2f".format(it) } ?: "0.00"}", MaterialTheme.typography.titleSmall, FontWeight.Bold)
                Text(repasse.created_at ?: "N/A", MaterialTheme.typography.bodySmall, TextSecondary)
            }
            Surface(
                if (repasse.status == "pago") Success.copy(0.2f) else Warning.copy(0.2f),
                RoundedCornerShape(6.dp)
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
