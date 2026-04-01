package com.cnhplus.screens.candidato

import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.TextSecondary
import com.cnhplus.ui.theme.Success
import com.cnhplus.ui.theme.Warning
import com.cnhplus.ui.theme.Error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cnhplus.*
import com.cnhplus.data.*
import com.cnhplus.ui.theme.LocalAppState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.cnhplus.ui.theme.PrimaryLight
import com.cnhplus.ui.theme.TextPrimary

/**
 * Perfil Comportamental - 10 perguntas para alimentar o algoritmo de match.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilComportamentalScreen(
    onComplete: () -> Unit
) {
    val state = LocalAppState.current
    var step by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    // State for 10 questions
    var abriuProcesso by remember { mutableStateOf<Boolean?>(null) }
    var passouTeorica by remember { mutableStateOf<Boolean?>(null) }
    var experiencia by remember { mutableStateOf<String?>(null) }
    var ansiedade by remember { mutableStateOf<String?>(null) }
    var reprovou by remember { mutableStateOf<Boolean?>(null) }
    var maiorDificuldade by remember { mutableStateOf<MutableSet<String>>(mutableSetOf()) }
    var objetivo by remember { mutableStateOf<String?>(null) }
    var temCarro by remember { mutableStateOf<String?>(null) }
    var turnos by remember { mutableStateOf<MutableSet<String>>(mutableSetOf()) }
    var dias by remember { mutableStateOf<MutableSet<String>>(mutableSetOf()) }

    val userId = state.currentUser.value?.id

    val progress = (step + 1) / 10f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(Primary, PrimaryLight, Secondary))
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Accent,
            trackColor = Color.White.copy(alpha = 0.3f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pergunta ${step + 1} de 10",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                when (step) {
                    0 -> Question0(abriuProcesso) { abriuProcesso = it }
                    1 -> Question1(passouTeorica) { passouTeorica = it }
                    2 -> Question2(experiencia) { experiencia = it }
                    3 -> Question3(ansiedade) { ansiedade = it }
                    4 -> Question4(reprovou) { reprovou = it }
                    5 -> Question5(maiorDificuldade) { maiorDificuldade = it.toMutableSet() }
                    6 -> Question6(objetivo) { objetivo = it }
                    7 -> Question7(temCarro) { temCarro = it }
                    8 -> Question8(turnos) { turnos = it.toMutableSet() }
                    9 -> Question9(dias) { dias = it.toMutableSet() }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step > 0) {
                        OutlinedButton(
                            onClick = { step-- },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Voltar")
                        }
                    }
                    
                    Button(
                        onClick = {
                            // Validate current step
                            val valid = when (step) {
                                0 -> abriuProcesso != null
                                1 -> passouTeorica != null
                                2 -> experiencia != null
                                3 -> ansiedade != null
                                4 -> reprovou != null
                                5 -> maiorDificuldade.isNotEmpty()
                                6 -> objetivo != null
                                7 -> temCarro != null
                                8 -> turnos.isNotEmpty()
                                9 -> dias.isNotEmpty()
                                else -> false
                            }
                            if (!valid) {
                                error = "Selecione uma opção"
                                return@Button
                            }
                            
                            if (step < 9) {
                                step++
                                error = null
                            } else {
                                // Submit all
                                loading = true
                                val perfil = PerfilCandidato(
                                    abriuProcesso = abriuProcesso ?: false,
                                    passouTeorica = passouTeorica ?: false,
                                    experiencia = experiencia ?: "nunca",
                                    ansiedade = ansiedade ?: "baixa",
                                    reprovou = reprovou ?: false,
                                    maiorDificuldade = maiorDificuldade.toList(),
                                    objetivo = objetivo ?: "aprender_calma",
                                    temCarro = temCarro ?: "nao",
                                    disponibilidade = DiasTurnos(
                                        dias = dias.toList(),
                                        turnos = turnos.toList()
                                    )
                                )
                                
                                // Calculate recommended lessons
                                var recommended = 20
                                if (experiencia == "nunca") recommended += 5
                                if (ansiedade == "alta") recommended += 3
                                if (reprovou == true) recommended += 5
                                
                                val pacote = PacoteCandidato(
                                    aulasRecomendadas = recommended,
                                    aulasCompradas = 0,
                                    aulasRestantes = 0
                                )
                                
                                userId?.let { uid ->
                                    val candidato = CandidatoDto(id = uid)
                                        .withPerfil(perfil)
                                        .withPacote(pacote)
                                    
                                    state.candidatoRepo.updateCandidato(uid, mapOf(
                                        "perfil" to jsonEncodePerfil(perfil),
                                        "pacote" to jsonEncodePacote(pacote)
                                    )).fold(
                                        onSuccess = {
                                            loading = false
                                            onComplete()
                                        },
                                        onFailure = { e ->
                                            loading = false
                                            error = e.message ?: "Erro ao salvar"
                                        }
                                    )
                                } ?: run {
                                    loading = false
                                    error = "Usuário não autenticado"
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Text(if (step < 9) "Próximo" else "Concluir")
                        }
                    }
                }
            }
        }
    }
}

// Helper - simple JSON encode using Json.encodeToString with serializer
private fun jsonEncodePerfil(perfil: PerfilCandidato): String {
    val json = Json { encodeDefaults = true }
    return json.encodeToString(perfil)
}

private fun jsonEncodePacote(pacote: PacoteCandidato): String {
    val json = Json { encodeDefaults = true }
    return json.encodeToString(pacote)
}

@Composable
private fun Question0(value: Boolean?, onSelect: (Boolean) -> Unit) {
    QuestionTitle("Você já abriu o processo para tirar a CNH?")
    BooleanOptions(value, onSelect)
}

@Composable
private fun Question1(value: Boolean?, onSelect: (Boolean) -> Unit) {
    QuestionTitle("Você já passou na prova teórica?")
    BooleanOptions(value, onSelect)
}

@Composable
private fun Question2(value: String?, onSelect: (String) -> Unit) {
    QuestionTitle("Você já dirigiu antes?")
    SingleOptions(
        options = listOf("Nunca dirigiu", "Poucas vezes", "Dirijo com frequência"),
        values = listOf("nunca", "poucas_vezes", "frequente"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question3(value: String?, onSelect: (String) -> Unit) {
    QuestionTitle("Você sente ansiedade ao dirigir?")
    SingleOptions(
        options = listOf("Baixa", "Média", "Alta"),
        values = listOf("baixa", "media", "alta"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question4(value: Boolean?, onSelect: (Boolean) -> Unit) {
    QuestionTitle("Você já reprovou no exame prático?")
    BooleanOptions(value, onSelect)
}

@Composable
private fun Question5(value: Set<String>, onSelect: (List<String>) -> Unit) {
    QuestionTitle("Qual sua maior dificuldade? (pode marcar mais de uma)")
    MultiOptions(
        options = listOf("Baliza", "Controle do carro", "Velocidade", "Rotatórias", "Todas acima"),
        values = listOf("baliza", "controle", "velocidade", "rotatorias", "todas"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question6(value: String?, onSelect: (String) -> Unit) {
    QuestionTitle("Qual seu objetivo?")
    SingleOptions(
        options = listOf("Aprender com calma", "Passar rápido na prova", "Ficar muito bom (pós-CNH)"),
        values = listOf("aprender_calma", "passar_rapido", "ficar_bom"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question7(value: String?, onSelect: (String) -> Unit) {
    QuestionTitle("Você tem carro para treinar?")
    SingleOptions(
        options = listOf("Sim", "Não", "Às vezes"),
        values = listOf("sim", "nao", "as_vezes"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question8(value: Set<String>, onSelect: (List<String>) -> Unit) {
    QuestionTitle("Quais turnos você está disponível?")
    MultiOptions(
        options = listOf("Manhã", "Tarde", "Noite"),
        values = listOf("manha", "tarde", "noite"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun Question9(value: Set<String>, onSelect: (List<String>) -> Unit) {
    QuestionTitle("Quais dias da semana?")
    MultiOptions(
        options = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"),
        values = listOf("segunda", "terca", "quarta", "quinta", "sexta", "sabado", "domingo"),
        selected = value,
        onSelect = onSelect
    )
}

@Composable
private fun QuestionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun BooleanOptions(value: Boolean?, onSelect: (Boolean) -> Unit) {
    SingleOptions(
        options = listOf("Sim", "Não"),
        values = listOf("true", "false"),
        selected = value?.toString(),
        onSelect = { onSelect(it == "true") }
    )
}

@Composable
private fun SingleOptions(
    options: List<String>,
    values: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEachIndexed { index, label ->
            val isSelected = selected == values[index]
            Card(
                onClick = { onSelect(values[index]) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Primary else Color(0xFFF5F7FA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) Color.White else TextPrimary
                )
            }
        }
    }
}

@Composable
private fun MultiOptions(
    options: List<String>,
    values: List<String>,
    selected: Set<String>,
    onSelect: (List<String>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEachIndexed { index, label ->
            val isSelected = selected.contains(values[index])
            Card(
                onClick = {
                    val newSet = selected.toMutableSet()
                    if (isSelected) newSet.remove(values[index]) else newSet.add(values[index])
                    onSelect(newSet.toList())
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Primary else Color(0xFFF5F7FA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSelected) "☑" else "☐",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) Color.White else TextSecondary
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) Color.White else TextPrimary
                    )
                }
            }
        }
    }
}
