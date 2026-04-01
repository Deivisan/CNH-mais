package com.cnhplus.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.TextPrimary
import com.cnhplus.ui.theme.SurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    onNavigateToInstrutores: () -> Unit,
    onNavigateToAlunos: () -> Unit,
    onNavigateToAulas: () -> Unit,
    onNavigateToFinanceiro: () -> Unit,
    onNavigateToConfig: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel Admin") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SurfaceColor)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Menu Principal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                AdminMenuCard(
                    icon = Icons.Default.People,
                    title = "Instrutores",
                    subtitle = "Gerenciar instrutores cadastrados",
                    onClick = onNavigateToInstrutores
                )
            }

            item {
                AdminMenuCard(
                    icon = Icons.Default.School,
                    title = "Alunos",
                    subtitle = "Gerenciar candidatos",
                    onClick = onNavigateToAlunos
                )
            }

            item {
                AdminMenuCard(
                    icon = Icons.Default.CalendarMonth,
                    title = "Aulas",
                    subtitle = "Visualizar todas as aulas",
                    onClick = onNavigateToAulas
                )
            }

            item {
                AdminMenuCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Financeiro",
                    subtitle = "Receitas e estatísticas",
                    onClick = onNavigateToFinanceiro
                )
            }

            item {
                AdminMenuCard(
                    icon = Icons.Default.Settings,
                    title = "Configurações",
                    subtitle = "Configurações do sistema",
                    onClick = onNavigateToConfig
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "⚠️ Módulo Admin depriorizado — stubs funcionais, sem dados emulados",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun AdminMenuCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Primary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}
