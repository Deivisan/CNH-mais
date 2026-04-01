package com.cnhplus.data.repository

import com.cnhplus.data.RepasseDto
import com.cnhplus.network.SupabaseClient

class RepasseRepository(private val supabase: SupabaseClient) {

    fun getRepassesByInstrutor(instrutorId: String): Result<List<RepasseDto>> {
        return supabase.get(
            table = "repasses",
            params = mapOf("instrutor_id" to "eq.$instrutorId")
        )
    }

    fun getRepassesByAula(aulaId: String): Result<RepasseDto?> {
        return supabase.getByColumn(
            table = "repasses",
            column = "aula_id",
            value = aulaId
        )
    }

    fun createRepasse(repasse: RepasseDto): Result<RepasseDto> {
        return supabase.insert(table = "repasses", item = repasse)
    }

    fun updateRepasseStatus(repasseId: String, status: String): Result<Unit> {
        return supabase.update(
            table = "repasses",
            column = "id",
            value = repasseId,
            fields = mapOf("status" to status)
        )
    }

    fun getTotalGanhos(instrutorId: String): Result<List<RepasseDto>> {
        return supabase.get(
            table = "repasses",
            params = mapOf("instrutor_id" to "eq.$instrutorId", "status" to "eq.pago")
        )
    }
}
