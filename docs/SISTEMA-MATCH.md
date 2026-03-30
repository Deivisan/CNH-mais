# Sistema de Match - Algoritmo de Conexão

## Visão Geral

O sistema de match é o coração do aplicativo. Decide automaticamente qual instrutor será responsável por qual candidato, baseado em múltiplos fatores técnicos, comportamentais e operacionais.

---

## 2.1 - Perfil Inicial do Instrutor

O instrutor informa:

### Dados Pessoais
- [ ] Nome completo
- [ ] CPF
- [ ] Telefone
- [ ] Endereço
- [ ] Foto
- [ ] Mini-bio
- [ ] Experiência

### Especialidades
- [ ] Iniciante
- [ ] Ansioso
- [ ] Reprovado
- [ ] etc.

### Veículo
**Se possui carro próprio:**
- [ ] Sim → detalhar modelo, ano, pedal presente ou não
- [ ] Não → opção de alugar carro pelo app

### Termos e Regras
- [ ] Carro sob responsabilidade do instrutor
- [ ] Seguro obrigatório (plano básico obrigatório)
- [ ] Possibilidade de ganhar seguro grátis por bonificação

---

## 2.2 - Dados Operacionais do Instrutor

- [ ] Horários disponíveis
- [ ] Regiões que atende
- [ ] Tipo de aula:
  - [ ] Com carro próprio
  - [ ] Com carro algalugado do app
  - [ ] Com carro do aluno (se permitido)

---

## 2.3 - O MATCH (Algoritmo)

### Dados do Candidato (Input)

| Dado | Tipo |
|------|------|
| Ansiedade | baixa/media/alta |
| Experiência | nunca/poucas_vezes/frequente |
| Reprovações | boolean |
| Objetivo | aprender_calma/passar_rapido/ficar_bom |
| Maior dificuldade | baliza/controle/velocidade/rotatorias |
| Tem carro | boolean |
| Disponibilidade | dias/turnos |
| Localização | cidade/bairro |

### Dados do Instrutor (Input)

| Dado | Tipo |
|------|------|
| Especialidades | lista |
| Estilo | calmo/objetivo/rigor/motivador |
| Horas trabalhadas | número |
| Alunos atendidos | número |
| Nota média | 1-5 |
| Tem carro próprio | boolean |
| Tipo veículo | manual/automatico |
| Tem pedal | boolean |
| Aluga carro | boolean |
| Regiões | lista |
| Dias/turnos | lista |
| Pontualidade | % |
| Cancelamentos | baixo/medio/alto |

### Regras Fixas

| Regra | Condição | Ação |
|-------|----------|------|
| Iniciante + Carro | Candidato nunca dirigiu | Priorizar instrutor com carro + pedal |
| Sem carro + Sem carro | Instrutor sem carro E candidato sem carro | Excluir da lista |
| Ansioso | Candidato ansiedade alta | Priorizar instrutor com especialidade "ansioso" |
| Reprovado | Candidato reprovou | Priorizar especialidade "reprovado" |
| Sem horário | Instrutor sem agenda disponível | Excluir |
| Status | Instrutor suspenso/bloqueado | Excluir |

### Cálculo de Score

```
score = (especialidade_match * 30) +
        (nota_media * 20) +
        (pontualidade * 20) +
        (1 - cancelamentos_normalizado * 10) +
        (horas_experiencia_normalizado * 10) +
        (distancia_normalizado * 10)
```

---

## 2.4 - Distribuição Inteligente

O app também controla:

| Cenário | Ação |
|---------|------|
| Instrutor novato | Sistema manda mais candidatos (para subir rápido) |
| Instrutor top | Sistema aumenta fluxo |
| Instrutor com punições | Sistema diminui fluxo |
| Instrutor com nota alta | Sistema aumenta fluxo |
| Instrutor agenda cheia | Sistema tira ele da rotação |
| Instrutor sem carro + candidato sem carro | Não mostrar |
| Baixa ocupação | Incentivar mais horários |

### Controle de Exposição
- [ ] Instrutores top não podem receber >30% dos alunos
- [ ] Instrutores novatos recebem pelo menos 10% se disponíveis
- [ ] Rotação para evitar dependência
- [ ] Badges influenciam visualização

---

## 2.5 - Funcionamento das Aulas

1. [ ] Instrutor recebe o aluno
2. [ ] Dá a aula
3. [ ] Marca a aula como concluída
4. [ ] Candidato valida (OK)
5. [ ] App libera o repasse no D+1

---

## Tipos de Match

### Match Inicial
- Quando candidato compra pacote pela primeira vez
- Algoritmo decide melhor instrutor

### Rematch
- Após primeira aula, candidato pode solicitar troca
- Só permitido após conclusão da primeira aula
- Algoritmo recalcula com feedback

### Match de Emergência
- Quando instrutor cancela em cima da hora
- Sistema procura substituto rapidamente
- Prioriza instrutores próximos

---

## Feedback Loop

### Após Cada Aula

O sistema coleta:
- [ ] Avaliação do candidato (estrelas)
- [ ] Comentário (opcional)
- [ ] Flags de problema (opcional)

### Atualização de Perfis

| Feedback | Ação no Instrutor |
|----------|-------------------|
| Nota alta | Aumenta fluxo |
| Nota baixa | Diminui fluxo |
| Cancelamento | Marca no histórico |
| Queixa grave | Suspende temporariamente |
| Problema com carro | Revisa termo |

---

## Métricas de Qualidade

- [ ] Taxa de Sucesso: Aulas iniciadas / Aulas agendadas
- [ ] Taxa de Retenção: Candidatos que completam pacote
- [ ] Satisfação Média: Nota média das avaliações
- [ ] Tempo de Match: Tempo entre pagamento e primeira aula

---

## Resumo para TI

> "O app faz o match automático entre candidato e instrutor baseado no perfil psicológico e operacional do aluno, nas características e desempenho do instrutor, e nas regras internas de segurança, carro e pedal. O algoritmo garante distribuição justa, prioriza segurança e cria incentivos para performance."

---

**Última atualização:** 30/03/2026
**Projeto:** CNH+