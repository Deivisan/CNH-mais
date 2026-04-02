package com.cnhplus.data.repository

import com.cnhplus.data.remote.mappers.InstrutorMapper
import com.cnhplus.domain.model.Instrutor
import com.cnhplus.domain.model.Veiculo
import com.cnhplus.domain.model.Disponibilidade
import com.cnhplus.domain.repository.InstrutorRepository as InstrutorRepositoryInterface
import com.cnhplus.network.SupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstrutorRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : InstrutorRepositoryInterface {

    private val table = "instrutores"

    override suspend fun getInstrutor(id: String): Result<Instrutor?> {
        return try {
            val result = supabaseClient.getById<com.cnhplus.data.InstrutorDto>(table, id)
            result.map { dto ->
                dto?.let { InstrutorMapper.toDomain(it) }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getActiveInstrutores(): Result<List<Instrutor>> {
        return try {
            val result = supabaseClient.get<com.cnhplus.data.InstrutorDto>(table, mapOf("status" to "eq.ativo"))
            result.map { dtos ->
                dtos.map { InstrutorMapper.toDomain(it) }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllInstrutores(): Result<List<Instrutor>> {
        return try {
            val result = supabaseClient.get<com.cnhplus.data.InstrutorDto>(table, emptyMap())
            result.map { dtos ->
                dtos.map { InstrutorMapper.toDomain(it) }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createInstrutor(instrutor: Instrutor): Result<Instrutor> {
        return Result.failure(NotImplementedError("Use DTO diretamente para create"))
    }

    override suspend fun updateInstrutor(id: String, fields: Map<String, Any?>): Result<Unit> {
        return supabaseClient.update(table, "id", id, fields)
    }

    override suspend fun updateDisponibilidade(id: String, disponibilidade: Disponibilidade): Result<Unit> {
        val disponibilidadeDto = com.cnhplus.data.DiasTurnos(
            dias = disponibilidade.dias.map { it.name.lowercase() },
            turnos = disponibilidade.turnos.map { it.name.lowercase() }
        )
        val json = com.cnhplus.core.di.JsonConfig.default.encodeToString(disponibilidadeDto)
        return supabaseClient.update(table, "id", id, mapOf("disponibilidade" to json))
    }

    override suspend fun updateVeiculo(id: String, veiculo: Veiculo): Result<Unit> {
        val veiculoDto = com.cnhplus.data.Veiculo(
            tipo = veiculo.tipo.name.lowercase(),
            modelo = veiculo.modelo,
            ano = veiculo.ano,
            temPedal = veiculo.temPedal
        )
        val json = com.cnhplus.core.di.JsonConfig.default.encodeToString(veiculoDto)
        return supabaseClient.update(table, "id", id, mapOf("veiculo" to json))
    }

    override fun observeInstrutor(id: String): Flow<Instrutor?> = flow {
        val instrutor = getInstrutor(id).getOrNull()
        emit(instrutor)
    }
}
