# CNH+ - Especificação Completa

## Visão Geral
**Nome:** CNH+ | **Plataforma:** Android | **Versão:** 0.1.0

Plataforma que conecta candidatos à CNH com instrutores de direção através de **match inteligente**.

---

## Cores (Degradê)
- Primary: #1E3A5F (Azul escuro)
- Secondary: #4A90D9 (Azul)
- Accent: #87CEEB (Celeste)

---

## Fluxo: CANDIDATO

### 1.1 Acesso Inicial
- Login via Google (Gmail)
- Aceite de termos
- Captura: Nome, foto, e-mail

### 1.2 Cadastro
- CPF, Celular, Cidade/Bairro
- RENACH (manual)

### 1.3 Perfil Comportamental
| Pergunta | Opções |
|----------|--------|
| Já abriu processo? | Sim/Não |
| Já passou na teórica? | Sim/Não |
| Já dirigiu? | Nunca/Poucas vezes/Frequente |
| Ansiedade? | Baixa/Média/Alta |
| Já reprovou? | Sim/Não |
| Maior dificuldade? | Baliza/Controle/Velocidade/Rotatórias |
| Objetivo? | Aprender com calma/Passar rápido/Ficar bom |
| Tem carro? | Sim/Não/Às vezes |
| Horários? | Manhã/Tarde/Noite |

### 1.4 Recomendação
- Sistema calcula: quantidade ideal de aulas
- Tipo: carro instrutor / carro com pedal / carro do aluno

### 1.5 Pagamento
- PIX, Cartão crédito, Cartão débito
- Dinheiro retido no app

### 1.6 Match
- Sistema escolhe instrutor automaticamente

### 1.7 Primeira Aula
- Troca só após 1ª aula
- Depois vínculo fixo

### 1.8 Home
- Próximas aulas, progresso, indicações

---

## Fluxo: INSTRUTOR

### 2.1 Cadastro
- Dados pessoais + documentos
- Perfil (bio, especialidades)
- Veículo (próprio/alugado/aluno)
- Termo de responsabilidade

### 2.2 Abas do App
| Aba | Descrição |
|-----|-----------|
| Dashboard | Visão geral, financeiro |
| Perfil | Público (editável) |
| Mídia | Vídeos (máx 5, 60s) |
| Resultados | Taxa conclusão/cancelamento |
| Financeiro | Saldo, repasses |
| Dados Bancários | Conta/Pix |
| Aulas & Disputas | Histórico |
| Carro/Aluguel | Veículo |
| Comunidade | Entre instrutores |
| Suporte | Chat, FAQ |

### 2.3 Agenda
- Calendário interativo
- Horário: disponível/bloqueado/agendado/concluído
- Por horário: define modo (carro próprio/aluno/ambos)
- Projeção de ganhos (hoje/semana/mês)

---

## Algoritmo de MATCH

### Dados Candidato
- Ansiedade, experiência, reprovações, objetivo, dificuldade, carro, disponibilidade, localização

### Dados Instrutor
- Especialidades, estilo, horas, alunos, nota, veículo, pedal, regiões, pontualidade, cancelamentos

### Regras
| Regra | Ação |
|-------|------|
| Iniciante | Priorizar instrutor com carro + pedal |
| Ansioso | Priorizar "ansioso" |
| Sem horário | Excluir |
| Suspenso | Excluir |

### Score
```
score = (especialidade * 30) + (nota * 20) + (pontualidade * 20) + (cancelamentos * 10) + (experiência * 10) + (distância * 10)
```

---

## Bonificações

### Candidato
| Bonificação | Requisito | Benefício |
|-------------|-----------|-----------|
| Aula grátis | 10 indicações | 1 aula |
| Pós-CNH | Finalizar pacote | Direção defensiva |
| Seguro grátis | 10 ind. + carro alg. | 1 mês |
| Bônus rede | Postar app | 1 aula |

### Instrutor
| Bonificação | Requisito | Benefício |
|-------------|-----------|-----------|
| Seguro grátis | Performance alta | 1-3 meses |
| Manutenção | Parceria | Óleo, revisão |
| Mais alunos | Top rating | +fluxo |
| Taxa reduzida | Nota >4.5 | -1% a -2% |

---

## Arquitetura

### Frontend
- Kotlin + Jetpack Compose + Material 3
- ViewModel + StateFlow

### Backend
- Bun + Express/Hono
- PostgreSQL (Supabase)
- Auth: Supabase Auth

### Serviços
- Mapas: Mapbox/OSMDroid
- Pagamentos: Mercado Pago
- Notificações: FCM

---

## Status

| Fase | Status |
|------|--------|
| Especificação | 🔄 80% |
| Design UI/UX | ⏳ 0% |
| Frontend | ⏳ 0% |
| Backend | ⏳ 0% |
| Lançamento | ⏳ 0% |

---

**Última atualização:** 30/03/2026
**Autor:** Deivison Santana (@deivisan)