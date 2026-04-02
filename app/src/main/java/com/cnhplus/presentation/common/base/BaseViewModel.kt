package com.cnhplus.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel com pattern unidirecional de estado.
 * 
 * @param UiState Tipo do estado da UI (data class imutável)
 * @param Event Tipo dos eventos de UI (sealed class)
 */
abstract class BaseViewModel<UiState, Event>(
    initialState: UiState
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /**
     * Atualiza o estado de forma atômica.
     */
    protected fun updateState(transform: UiState.() -> UiState) {
        _uiState.update(transform)
    }

    /**
     * Executa uma operação suspensa no escopo do ViewModel
     * com tratamento de loading e erro automático.
     */
    protected fun <T> launchWithState(
        loadingState: UiState.() -> UiState = { this },
        errorState: UiState.(String) -> UiState,
        block: suspend () -> Result<T>,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            updateState { loadingState() }
            
            block().fold(
                onSuccess = { result ->
                    onSuccess(result)
                },
                onFailure = { error ->
                    updateState { errorState(error.message ?: "Erro desconhecido") }
                }
            )
        }
    }

    /**
     * Processa eventos da UI.
     * Implementado por subclasses.
     */
    abstract fun onEvent(event: Event)
}

/**
 * Interface para estados de UI que suportam loading e erro.
 */
interface UiStateWithStatus {
    val isLoading: Boolean
    val error: String?
}
