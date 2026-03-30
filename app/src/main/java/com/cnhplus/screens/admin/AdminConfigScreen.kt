package com.cnhplus.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminConfigScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Geral",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ConfigItem(icon = Icons.Default.Notifications, title = "Notificações", subtitle = "Gerenciar notificações push")
            ConfigItem(icon = Icons.Default.Email, title = "E-mails", subtitle = "Configurar templates de e-mail")
            ConfigItem(icon = Icons.Default.Security, title = "Segurança", subtitle = "Autenticação e permissões")
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sistema",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ConfigItem(icon = Icons.Default.Payment, title = "Pagamentos", subtitle = "Configurações do Mercado Pago")
            ConfigItem(icon = Icons.Default.Map, title = "Mapas", subtitle = "API de mapas")
            ConfigItem(icon = Icons.Default.Cloud, title = "Firebase", subtitle = "Configurações de nuvem")
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sobre",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ConfigItem(icon = Icons.Default.Info, title = "Versão", subtitle = "1.0.0")
            ConfigItem(icon = Icons.Default.Description, title = "Termos de Uso", subtitle = "Ver documento")
            ConfigItem(icon = Icons.Default.PrivacyTip, title = "Privacidade", subtitle = "Política de privacidade")
        }
    }
}

@Composable
fun ConfigItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}
