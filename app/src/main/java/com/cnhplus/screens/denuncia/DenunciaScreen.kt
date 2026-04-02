package com.cnhplus.screens.denuncia

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.SurfaceColor
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.TextPrimary
import com.cnhplus.ui.theme.Error

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.data.DenunciaDto
import kotlinx.coroutines.launch

/**
 * Tela para abrir denúncia contra instrutor ou candidato.
 * Permite selecionar motivo, escrever descrição e anexar mídia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DenunciaScreen(
    aulaId: String,
    denunciadoId: String,
    denunciadoNome: String,
    onBack: () -> Unit
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    
    var motivoSelecionado by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var sucesso by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }
    
    val userId = app.currentUser.value?.id ?: ""
    
    val motivos = listOf(
        "Assédio",
        "Comportamento inadequado",
        "Aula não realizada",
        "Cobrança indevida",
        "Atraso excessivo",
        "Veículo em más condições",
        "Outro"
    )
    
    if (sucesso) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Denúncia Enviada!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sua denúncia foi registrada e será analisada pela nossa equipe em até 48h.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Voltar")
                }
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Abrir Denúncia") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SurfaceColor),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Info box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Error.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Denunciando: $denunciadoNome",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Error
                        )
                    }
                }
            }
            
            item {
                Text(
                    text = "Motivo da Denúncia *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(motivos) { motivo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { motivoSelecionado = motivo }
                        .background(
                            if (motivoSelecionado == motivo) Secondary.copy(alpha = 0.1f) else Color.White,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = motivoSelecionado == motivo,
                        onClick = { motivoSelecionado = motivo }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = motivo,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            item {
                Text(
                    text = "Descrição Detalhada *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { if (it.length <= 1000) descricao = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Descreva o que aconteceu...") },
                    minLines = 4,
                    maxLines = 8,
                    shape = RoundedCornerShape(12.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${descricao.length}/1000",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            
            item {
                Text(
                    text = "Anexar Evidência (opcional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: abrir câmera */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Câmera")
                    }
                    OutlinedButton(
                        onClick = { /* TODO: abrir galeria */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Galeria")
                    }
                }
                Text(
                    text = "Fotos, vídeos ou áudios como evidência",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            
            item {
                erro?.let {
                    Text(
                        text = it,
                        color = Error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                Button(
                    onClick = {
                        if (motivoSelecionado.isEmpty()) {
                            erro = "Selecione um motivo"
                            return@Button
                        }
                        if (descricao.isBlank()) {
                            erro = "Escreva uma descrição"
                            return@Button
                        }
                        
                        isSubmitting = true
                        scope.launch {
                            val denuncia = DenunciaDto(
                                aula_id = aulaId,
                                denunciante_id = userId,
                                motivo = motivoSelecionado,
                                descricao = descricao.trim(),
                                status = "aberta"
                            )
                            
                            app.denunciaRepo.createDenuncia(denuncia).fold(
                                onSuccess = { sucesso = true },
                                onFailure = { e -> erro = e.message ?: "Erro ao enviar denúncia" }
                            )
                            isSubmitting = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enviar Denúncia", fontSize = 16.sp)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
