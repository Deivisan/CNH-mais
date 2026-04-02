package com.cnhplus.presentation.instrutor.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnhplus.domain.model.Disponibilidade
import com.cnhplus.domain.model.Especialidade
import com.cnhplus.domain.model.EstiloEnsino
import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.model.TipoVeiculo
import com.cnhplus.domain.model.Veiculo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para o Wizard de Onboarding do Instrutor.
 * Gerencia estado dos 5 steps.
 */
@HiltViewModel
class InstrutorWizardViewModel @Inject constructor(
    private val createInstrutorUseCase: com.cnhplus.domain.usecase.instrutor.CreateInstrutorUseCase
) : ViewModel() {

    // Estado do step atual
    var currentStep by mutableStateOf(1)
        private set

    // Dados do instrutor
    var nome by mutableStateOf("")
    var cpf by mutableStateOf("")
    var telefone by mutableStateOf("")
    var cidade by mutableStateOf("")
    var biografia by mutableStateOf("")
    var fotoBytes by mutableStateOf<ByteArray?>(null)

    // Documentos
    var cnhUri by mutableStateOf<android.net.Uri?>(null)
    var crlvUri by mutableStateOf<android.net.Uri?>(null)

    // Veículo
    var tipoVeiculo by mutableStateOf(TipoVeiculo.CARRO_PROPRIO)
    var modelo by mutableStateOf("")
    var ano by mutableStateOf("")
    var temPedal by mutableStateOf(true)

    // Disponibilidade
    var disponibilidade by mutableStateOf(Disponibilidade())

    // Especialidades
    val estilo = mutableStateListOf<EstiloEnsino>()
    val especialidades = mutableStateListOf<Especialidade>()

    // Estado de loading
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    // Navegação entre steps
    fun nextStep() {
        if (currentStep < 5 && validateCurrentStep()) {
            currentStep++
        }
    }

    fun previousStep() {
        if (currentStep > 1) {
            currentStep--
        }
    }

    fun toggleEstilo(estiloItem: EstiloEnsino) {
        if (estiloItem in estilo) estilo.remove(estiloItem) else estilo.add(estiloItem)
    }

    fun toggleEspecialidade(esp: Especialidade) {
        if (esp in especialidades) especialidades.remove(esp) else especialidades.add(esp)
    }

    // Validar step atual
    private fun validateCurrentStep(): Boolean {
        error = null
        return when (currentStep) {
            1 -> {
                when {
                    nome.isBlank() -> { error = "Nome é obrigatório"; false }
                    cpf.filter { it.isDigit() }.length != 11 -> { error = "CPF inválido"; false }
                    telefone.filter { it.isDigit() }.length < 10 -> { error = "Telefone inválido"; false }
                    cidade.isBlank() -> { error = "Cidade é obrigatória"; false }
                    else -> true
                }
            }
            2 -> {
                if (cnhUri == null || crlvUri == null) {
                    error = "CNH e CRLV são obrigatórios"; false
                } else true
            }
            4 -> {
                when {
                    disponibilidade.dias.isEmpty() -> { error = "Selecione pelo menos um dia"; false }
                    disponibilidade.turnos.isEmpty() -> { error = "Selecione pelo menos um turno"; false }
                    else -> true
                }
            }
            5 -> {
                when {
                    estilo.isEmpty() -> { error = "Selecione pelo menos um estilo"; false }
                    especialidades.isEmpty() -> { error = "Selecione pelo menos uma especialidade"; false }
                    else -> true
                }
            }
            else -> true
        }
    }

    // Finalizar cadastro
    fun finalizarCadastro(userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null

            val instrutor = Instrutor(
                id = userId,
                cidade = cidade,
                biografia = biografia,
                estilo = estilo.toList(),
                especialidades = especialidades.toList(),
                disponibilidade = disponibilidade,
                veiculo = Veiculo(
                    tipo = tipoVeiculo,
                    modelo = modelo.takeIf { it.isNotBlank() },
                    ano = ano.takeIf { it.isNotBlank() },
                    temPedal = temPedal
                ),
                telefone = telefone,
                cpf = cpf
            )

            createInstrutorUseCase(instrutor).fold(
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onFailure = { e ->
                    isLoading = false
                    error = e.message ?: "Erro ao cadastrar instrutor"
                }
            )
        }
    }
}
