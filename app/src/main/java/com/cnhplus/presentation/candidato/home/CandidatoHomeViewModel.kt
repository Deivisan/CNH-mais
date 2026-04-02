package com.cnhplus.presentation.candidato.home

import androidx.lifecycle.viewModelScope
import com.cnhplus.domain.model.Candidato
import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.repository.SessionRepository
import com.cnhplus.domain.usecase.candidato.GetCandidatoUseCase
import com.cnhplus.domain.usecase.instrutor.GetInstrutorUseCase
import com.cnhplus.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para CandidatoHomeScreen usando Hilt DI.
 */
@HiltViewModel
class CandidatoHomeViewModel @Inject constructor(
    private val getCandidatoUseCase: GetCandidatoUseCase,
    private val getInstrutorUseCase: GetInstrutorUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel<CandidatoHomeUiState, CandidatoHomeEvent>(
    CandidatoHomeUiState()
) {

    init {
        onEvent(CandidatoHomeEvent.LoadData)
    }

    override fun onEvent(event: CandidatoHomeEvent) {
        when (event) {
            is CandidatoHomeEvent.LoadData -> loadData()
            is CandidatoHomeEvent.Refresh -> loadData()
            else -> { /* Outros eventos são tratados na UI */ }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            val userId = sessionRepository.getUserId()
            if (userId == null) {
                updateState { copy(isLoading = false, error = "Usuário não autenticado") }
                return@launch
            }

            // Carrega candidato
            val candidatoResult = getCandidatoUseCase(userId)
            val candidato = candidatoResult.getOrNull()

            // Carrega instrutor se houver vínculo
            val instrutor = candidato?.instrutorId?.let { instrutorId ->
                getInstrutorUseCase(instrutorId).getOrNull()
            }

            updateState {
                copy(
                    isLoading = false,
                    candidato = candidato,
                    instrutor = instrutor,
                    aulasHoje = calcularAulasHoje(candidato),
                    aulasRestantes = candidato?.pacote?.aulasRestantes ?: 0,
                    aulasFeitas = candidato?.aulasFeitas ?: 0,
                    progresso = candidato?.progresso ?: 0f,
                    error = candidatoResult.exceptionOrNull()?.message
                )
            }
        }
    }

    private fun calcularAulasHoje(candidato: Candidato?): Int {
        // TODO: Implementar lógica real quando tiver repositório de aulas
        return 0
    }
}
