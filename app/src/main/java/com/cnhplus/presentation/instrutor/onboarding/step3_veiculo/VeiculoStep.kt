package com.cnhplus.presentation.instrutor.onboarding.step3_veiculo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cnhplus.domain.model.TipoVeiculo
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Shapes

/**
 * Step 3: Veículo do Instrutor
 */
@Composable
fun VeiculoStep(
    tipoVeiculo: TipoVeiculo,
    onTipoVeiculoChange: (TipoVeiculo) -> Unit,
    modelo: String,
    onModeloChange: (String) -> Unit,
    ano: String,
    onAnoChange: (String) -> Unit,
    temPedal: Boolean,
    onTemPedalChange: (Boolean) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val opcoes = listOf(
        TipoVeiculo.CARRO_PROPRIO to "Carro Próprio",
        TipoVeiculo.CARRO_ALUGADO to "Carro Alugado",
        TipoVeiculo.CARRO_ALUNO to "Carro do Aluno"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        LinearProgressIndicator(
            progress = { 0.6f },
            modifier = Modifier.fillMaxWidth(),
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Veículo (3/5)",
            style = MaterialTheme.typography.titleLarge,
            color = Primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Informe os dados do seu veículo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tipo de veículo
        Text(
            text = "Tipo de Veículo *",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(modifier = Modifier.selectableGroup()) {
            opcoes.forEach { (tipo, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = tipoVeiculo == tipo,
                            onClick = { onTipoVeiculoChange(tipo) },
                            role = Role.RadioButton
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoVeiculo == tipo,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = label)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Modelo
        OutlinedTextField(
            value = modelo,
            onValueChange = onModeloChange,
            label = { Text("Modelo") },
            leadingIcon = { Icon(Icons.Default.DirectionsCar, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Ano
        OutlinedTextField(
            value = ano,
            onValueChange = { if (it.length <= 4) onAnoChange(it) },
            label = { Text("Ano") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes.medium,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tem pedal?
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = temPedal,
                onCheckedChange = onTemPedalChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Possui pedal de emergência")
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = Shapes.large
            ) {
                Icon(Icons.Default.ArrowBack, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voltar")
            }
            
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                shape = Shapes.large
            ) {
                Text("Próximo")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
