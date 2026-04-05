package com.cnhplus.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.*

/**
 * Card para mostrar localização da aula com opção de abrir no Maps
 */
@Composable
fun LocalizacaoAulaCard(
    endereco: String,
    latitude: Double?,
    longitude: Double?,
    referencia: String? = null,
    onEditarLocal: () -> Unit = {}
) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Local da Aula",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            
            // Endereço
            Text(
                text = endereco,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Referência (opcional)
            referencia?.takeIf { it.isNotBlank() }?.let { ref ->
                Text(
                    text = "Referência: $ref",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } ?: Spacer(modifier = Modifier.height(16.dp))
            
            // Botões
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botão Abrir no Maps
                if (latitude != null && longitude != null) {
                    OutlinedButton(
                        onClick = {
                            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Map, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Abrir Maps")
                    }
                }
                
                // Botão Editar
                OutlinedButton(
                    onClick = onEditarLocal,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Alterar")
                }
            }
        }
    }
}

/**
 * Versão compacta do card de localização (para listas)
 */
@Composable
fun LocalizacaoAulaCompact(
    endereco: String,
    latitude: Double?,
    longitude: Double?,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    
    ListItem(
        headline = { Text("Local da Aula") },
        supporting = { Text(endereco, maxLines = 2) },
        leading = {
            Icon(
                Icons.Default.LocationOn,
                null,
                tint = Primary
            )
        },
        trailing = {
            IconButton(
                onClick = {
                    if (latitude != null && longitude != null) {
                        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    } else {
                        onClick()
                    }
                }
            ) {
                Icon(Icons.Default.OpenInNew, null)
            }
        },
        modifier = Modifier.clickable { onClick() }
    )
}
