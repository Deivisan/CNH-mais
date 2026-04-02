package com.cnhplus.screens.chat

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.SurfaceColor
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.TextPrimary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cnhplus.ui.theme.LocalAppState
import com.cnhplus.data.MensagemDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Tela de Chat entre candidato e instrutor.
 * Usada quando a aula está "agendada" ou "em_andamento".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    aulaId: String,
    receiverId: String,
    receiverName: String,
    onBack: () -> Unit
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    
    var mensagens by remember { mutableStateOf<List<MensagemDto>>(emptyList()) }
    var textoInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    
    val listState = rememberLazyListState()
    val userId = app.currentUser.value?.id ?: ""
    
    // Carregar mensagens
    LaunchedEffect(aulaId) {
        app.mensagemRepo.getMensagens(aulaId).fold(
            onSuccess = { msgs ->
                mensagens = msgs
                isLoading = false
                // Scroll para última mensagem
                if (msgs.isNotEmpty()) {
                    delay(100)
                    listState.animateScrollToItem(msgs.size - 1)
                }
            },
            onFailure = { isLoading = false }
        )
    }
    
    // Polling a cada 3 segundos para novas mensagens
    LaunchedEffect(aulaId) {
        while (true) {
            delay(3000)
            app.mensagemRepo.getMensagens(aulaId).fold(
                onSuccess = { msgs ->
                    if (msgs.size != mensagens.size) {
                        mensagens = msgs
                        if (msgs.isNotEmpty()) {
                            listState.animateScrollToItem(msgs.size - 1)
                        }
                    }
                },
                onFailure = {}
            )
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Chat", fontSize = 16.sp)
                        Text(receiverName, fontSize = 12.sp, color = Color.White.copy(0.7f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textoInput,
                        onValueChange = { textoInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Mensagem...") },
                        maxLines = 3,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Secondary,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textoInput.isNotBlank()) {
                                scope.launch {
                                    app.mensagemRepo.createMensagem(
                                        MensagemDto(
                                            aula_id = aulaId,
                                            sender_id = userId,
                                            receiver_id = receiverId,
                                            texto = textoInput.trim(),
                                            lida = false
                                        )
                                    ).fold(
                                        onSuccess = {
                                            textoInput = ""
                                            // Recarregar mensagens
                                            app.mensagemRepo.getMensagens(aulaId).fold(
                                                onSuccess = { msgs ->
                                                    mensagens = msgs
                                                    if (msgs.isNotEmpty()) {
                                                        listState.animateScrollToItem(msgs.size - 1)
                                                    }
                                                },
                                                onFailure = {}
                                            )
                                        },
                                        onFailure = {}
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .background(Secondary, RoundedCornerShape(50))
                            .padding(4.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(SurfaceColor),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (mensagens.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma mensagem ainda.\nCombine o local de encontro com ${receiverName.split(" ").first()}!",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                items(mensagens) { msg ->
                    val isMe = msg.sender_id == userId
                    MensagemBubble(
                        mensagem = msg,
                        isMe = isMe
                    )
                }
            }
        }
    }
}

@Composable
fun MensagemBubble(
    mensagem: MensagemDto,
    isMe: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isMe) Secondary else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = mensagem.texto ?: "",
                color = if (isMe) Color.White else TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatTime(mensagem.created_at),
                color = if (isMe) Color.White.copy(0.7f) else TextSecondary,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

private fun formatTime(timestamp: String?): String {
    if (timestamp == null) return ""
    return try {
        val parts = timestamp.split("T")
        if (parts.size > 1) parts[1].substring(0, 5) else ""
    } catch (e: Exception) {
        ""
    }
}
