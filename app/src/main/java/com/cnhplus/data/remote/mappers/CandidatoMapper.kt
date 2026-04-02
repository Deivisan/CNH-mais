package com.cnhplus.data.remote.mappers

import com.cnhplus.data.CandidatoDto
import com.cnhplus.data.DiasTurnos
import com.cnhplus.data.PacoteCandidato
import com.cnhplus.data.PerfilCandidato
import com.cnhplus.domain.model.Ansiedade
import com.cnhplus.domain.model.Candidato
import com.cnhplus.domain.model.DiaSemana
import com.cnhplus.domain.model.Dificuldade
import com.cnhplus.domain.model.Disponibilidade
import com.cnhplus.domain.model.Experiencia
import com.cnhplus.domain.model.Objetivo
import com.cnhplus.domain.model.PacoteCandidato as DomainPacote
import com.cnhplus.domain.model.PerfilCandidato as DomainPerfil
import com.cnhplus.domain.model.TemCarro
import com.cnhplus.domain.model.Turno

/**
 * Mapper para converter entre DTO e Domain Model.
 */
object CandidatoMapper {

    fun toDomain(dto: CandidatoDto, nome: String? = null, email: String? = null): Candidato {
        val perfilDto = dto.getPerfil()
        val pacoteDto = dto.getPacote()

        return Candidato(
            id = dto.id,
            nome = nome,
            email = email,
            cpf = dto.cpf,
            celular = dto.celular,
            cidade = dto.cidade,
            bairro = dto.bairro,
            renach = dto.renach,
            perfil = toDomainPerfil(perfilDto),
            pacote = toDomainPacote(pacoteDto),
            instrutorId = dto.instrutor_id,
            fotoUrl = null
        )
    }

    private fun toDomainPerfil(dto: PerfilCandidato): DomainPerfil {
        return DomainPerfil(
            abriuProcesso = dto.abriuProcesso,
            passouTeorica = dto.passouTeorica,
            experiencia = Experiencia.valueOf(dto.experiencia.uppercase()),
            ansiedade = Ansiedade.valueOf(dto.ansiedade.uppercase()),
            reprovou = dto.reprovou,
            maiorDificuldade = dto.maiorDificuldade.map { 
                Dificuldade.valueOf(it.uppercase()) 
            },
            objetivo = Objetivo.valueOf(dto.objetivo.uppercase()),
            temCarro = TemCarro.valueOf(dto.temCarro.uppercase()),
            disponibilidade = toDomainDisponibilidade(dto.disponibilidade)
        )
    }

    private fun toDomainPacote(dto: PacoteCandidato): DomainPacote {
        return DomainPacote(
            aulasCompradas = dto.aulasCompradas,
            aulasRestantes = dto.aulasRestantes,
            aulasRecomendadas = dto.aulasRecomendadas
        )
    }

    private fun toDomainDisponibilidade(dto: DiasTurnos): Disponibilidade {
        return Disponibilidade(
            dias = dto.dias.map { dia ->
                when(dia.lowercase()) {
                    "segunda" -> DiaSemana.SEGUNDA
                    "terca" -> DiaSemana.TERCA
                    "quarta" -> DiaSemana.QUARTA
                    "quinta" -> DiaSemana.QUINTA
                    "sexta" -> DiaSemana.SEXTA
                    "sabado" -> DiaSemana.SABADO
                    "domingo" -> DiaSemana.DOMINGO
                    else -> DiaSemana.SEGUNDA
                }
            },
            turnos = dto.turnos.map { turno ->
                when(turno.lowercase()) {
                    "manha" -> Turno.MANHA
                    "tarde" -> Turno.TARDE
                    "noite" -> Turno.NOITE
                    else -> Turno.MANHA
                }
            }
        )
    }
}
