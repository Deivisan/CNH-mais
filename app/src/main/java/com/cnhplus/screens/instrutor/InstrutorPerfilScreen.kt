package com.cnhplus.screens.instrutor

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
import com.cnhplus.data.InstrutorDto
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Accent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrutorPerfilScreen() {
    val app = LocalAppState.current
    var instrutor by remember { mutableStateOf<InstrutorDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        val userId = app.currentUser.value?.id ?: run { isLoading = false; return@LaunchedEffect }
        app.instrutorRepo.getInstrutor(userId).fold(
            onSuccess = { i -> instrutor = i; isLoading = false },
            onFailure = { isLoading = false }
        )
    }
    
    if (isLoading) { 
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Secondary) }
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
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Primary)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = instrutor?.id?.take(1)?.uppercase() ?: "I", 
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold, 
                            color = Primary
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = instrutor?.biografia?.take(30) ?: "Instrutor", 
                        style = MaterialTheme.typography.headlineSmall, 
                        fontWeight = FontWeight.Bold, 
                        color = Color.White
                    )
                    Text(
                        text = instrutor?.cidade ?: "Cidade não definida", 
                        style = MaterialTheme.typography.bodyMedium, 
                        color = Color.White.copy(0.8f)
                    )
                }
            }
            
            item { 
                Column {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Informações Profissionais", 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Bold, 
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
            
            item {
                instrutor?.let { inst ->
                    Column {
                        ProfileField(Icons.Default.Badge, "Status", inst.status?.replaceFirstChar { it.uppercase() } ?: "Pendente")
                        ProfileField(Icons.Default.School, "Horas Trabalhadas", "${inst.horas_trabalhadas ?: 0}h")
                        ProfileField(Icons.Default.People, "Alunos Atendidos", "${inst.alunos_atendidos ?: 0}")
                        ProfileField(Icons.Default.Star, "Avaliação Média", "${inst.nota_media ?: 0.0} ⭐")
                        ProfileField(Icons.Default.CheckCircle, "Pontualidade", "${inst.pontualidade ?: 100}%")
                    }
                }
            }
            
            item { 
                Column {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Veículo", 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Bold, 
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
            
            item {
                instrutor?.let { inst ->
                    val veiculo = inst.getVeiculo()
                    Column {
                        ProfileField(Icons.Default.DirectionsCar, "Tipo", veiculo.tipo.replace("_", " ").replaceFirstChar { it.uppercase() })
                        ProfileField(Icons.Default.Info, "Modelo", veiculo.modelo ?: "Não informado")
                        ProfileField(Icons.Default.DateRange, "Ano", veiculo.ano ?: "Não informado")
                    }
                }
            }
            
            item { 
                Column {
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = { app.logout() }, 
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.error)
                    ) { 
                        Icon(Icons.Filled.Close, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Sair") 
                    }
                    Spacer(Modifier.height(32.dp)) 
                }
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
            .padding(16.dp, 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Secondary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Column { 
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
