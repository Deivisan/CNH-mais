package com.cnhplus.screens.instrutor

import androidx.compose.foundation.background
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.foundation.layout.*
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.foundation.lazy.LazyColumn
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.foundation.lazy.items
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.foundation.shape.RoundedCornerShape
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.material.icons.Icons
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.material.icons.filled.*
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.material3.*
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.runtime.*
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.ui.Alignment
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.ui.Modifier
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.ui.graphics.Color
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.ui.text.font.FontWeight
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.*
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.data.AulaDto
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorAgendaScreen() {
    val app = LocalAppState.current
    var aulas by remember { mutableStateOf<List<AulaDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        val userId = app.currentUser.value?.id ?: run { isLoading = false; return@LaunchedEffect }
        app.aulaRepo.getAulasByInstrutor(userId).fold(
            onSuccess = { a -> aulas = a.filter { it.status == "agendada" }.sortedBy { it.data_hora }; isLoading = false },
            onFailure = { isLoading = false }
        )
    }
    
    if (isLoading) { Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Secondary) }; return }
    
    Scaffold(topBar = { TopAppBar(title = { Text("Minha Agenda") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary, titleContentColor = Color.White)) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(Surface)) {
            if (aulas.isEmpty()) { item { Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) { Text("Nenhuma aula agendada", color = TextSecondary) } } }
            else { items(aulas) { aula -> AgendaAulaCard(aula) } }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun AgendaAulaCard(aula: AulaDto) {
    Card(Modifier.fillMaxWidth().padding(16.dp, 8.dp), RoundedCornerShape(12.dp), CardDefaults.cardColors(Color.White)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(aula.data_hora ?: "Data não definida", MaterialTheme.typography.titleMedium, FontWeight.Bold)
                    Text("${aula.duracao ?: 50} min", MaterialTheme.typography.bodySmall, TextSecondary)
                }
                Surface(Accent.copy(0.2f), RoundedCornerShape(6.dp)) { Text("Agendada", MaterialTheme.typography.labelSmall, Secondary, Modifier.padding(6.dp)) }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Row(Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, Secondary, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(aula.local_encontro ?: "Local a definir", MaterialTheme.typography.bodySmall, TextSecondary)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}, Modifier.weight(1f).height(36.dp), ButtonDefaults.buttonColors(Secondary), RoundedCornerShape(8.dp)) { Text("Confirmar") }
                OutlinedButton(onClick = {}, Modifier.weight(1f).height(36.dp), shape = RoundedCornerShape(8.dp)) { Text("Cancelar") }
            }
        }
    }
}
