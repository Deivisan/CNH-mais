package com.cnhplus.domain.repository

import com.cnhplus.domain.model.Instrutor
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório de instrutores.
 */
interface InstrutorRepository {
    suspend fun getInstrutor(id: String): Result<Instrutor?>
    suspend fun getActiveInstrutores(): Result<List<Instrutor>>
    suspend fun getAllInstrutores(): Result<List<Instrutor>>
    suspend fun createInstrutor(instrutor: Instrutor): Result<Instrutor>
    suspend fun updateInstrutor(id: String, fields: Map<String, Any?>): Result<Unit>
    suspend fun updateDisponibilidade(id: String, disponibilidade: com.cnhplus.domain.model.Disponibilidade): Result<Unit>
    suspend fun updateVeiculo(id: String, veiculo: com.cnhplus.domain.model.Veiculo): Result<Unit>
    fun observeInstrutor(id: String): Flow<Instrutor?>
}
