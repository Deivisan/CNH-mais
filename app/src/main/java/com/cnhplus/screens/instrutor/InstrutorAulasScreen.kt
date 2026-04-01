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
import com.cnhplus.data.AulaDto
import com.cnhplus.ui.theme.LocalAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorAulasScreen() {
    val app = LocalAppState.current
    var aulas by remember { mutableStateOf<List<AulaDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        val userId = app.currentUser.value?.id ?: run { isLoading = false; return@LaunchedEffect }
        app.aulaRepo.getAulasByInstrutor(userId).fold(
            onSuccess = { a -> aulas = a.filter { it.status == "concluida" }.sortedByDescending { it.data_hora }; isLoading = false },
            onFailure = { isLoading = false }
        )
    }
    
    if (isLoading) { Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Secondary) }; return }
    
    Scaffold(topBar = { TopAppBar(title = { Text("Aulas Realizadas") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary, titleContentColor = Color.White)) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(Surface)) {
            if (aulas.isEmpty()) { item { Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) { Text("Nenhuma aula concluída", color = TextSecondary) } } }
            else { items(aulas) { aula -> AulaRealizadaCard(aula) } }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun AulaRealizadaCard(aula: AulaDto) {
    Card(Modifier.fillMaxWidth().padding(16.dp, 8.dp), RoundedCornerShape(12.dp), CardDefaults.cardColors(Color.White)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(aula.data_hora ?: "N/A", MaterialTheme.typography.titleSmall, FontWeight.Bold)
                    Text("${aula.duracao ?: 50}min", MaterialTheme.typography.bodySmall, TextSecondary)
                }
                Surface(Success.copy(0.2f), RoundedCornerShape(6.dp)) { Text("Concluída", MaterialTheme.typography.labelSmall, Success, Modifier.padding(6.dp)) }
            }
            Spacer(Modifier.height(12.dp))
            Row(Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, Warning, Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("${aula.valor ?: 0.0} R$", MaterialTheme.typography.bodySmall, TextSecondary)
            }
        }
    }
}
