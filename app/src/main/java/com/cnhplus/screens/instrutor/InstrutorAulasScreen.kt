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
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background)) {
            if (aulas.isEmpty()) { item { Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) { Text("Nenhuma aula concluída", color = TextSecondary) } } }
            else { items(aulas) { aula -> AulaRealizadaCard(aula) } }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun AulaRealizadaCard(aula: AulaDto) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
                        text = "${aula.duracao ?: 50}min",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Surface(
                    color = Success.copy(0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Concluída",
                        style = MaterialTheme.typography.labelSmall,
                        color = Success,
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
        }
    }
}
