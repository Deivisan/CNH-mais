# AGENTS.md - CNH+ Contexto para Agentes IA

## 📱 Projeto: CNH+

**Marketplace Inteligente de Aulas Práticas de Direção**

---

## 🎯 Visão do Projeto

Plataforma mobile Android que conecta candidatos à CNH com instrutores de direção de forma segura, transparente e inteligente através de **match automático**.

---

## 🎨 Identidade Visual

### Cores (Degradê)
| Cor | Hex | Uso |
|-----|-----|-----|
| Primary | `#1E3A5F` | Azul escuro - topo do app |
| Primary Light | `#2E5A8F` | Azul médio |
| Secondary | `#4A90D9` | Azul claro |
| Accent | `#87CEEB` | Celeste - base do degradê |

### Aplicação
- **Header/AppBar:** Azul escuro (#1E3A5F)
- **Degradê:** De azul escuro para celeste
- **Botões principais:** Azul (#4A90D9)
- **Acentos:** Celeste (#87CEEB)

---

## 📊 Estrutura de Dados (Modelos)

### Candidato
```typescript
interface Candidato {
  id: string
  nome: string
  email: string
  foto?: string
  cpf: string
  celular: string
  cidade: string
  renach?: string
  perfil: {
    abriuProcesso: boolean
    passouTeorica: boolean
    experiencia: 'nunca' | 'poucas_vezes' | 'frequente'
    ansiedade: 'baixa' | 'media' | 'alta'
    reprovou: boolean
    maiorDificuldade: string[]
    objetivo: 'aprender_calma' | 'passar_rapido' | 'ficar_bom'
    temCarro: 'sim' | 'nao' | 'as_vezes'
    disponibilidade: { dias: string[], turnos: string[] }
  }
  pacote: {
    aulasCompradas: number
    aulasRestantes: number
    aulasRecomendadas: number
  }
  instrutorId?: string  // após match
}
```

### Instrutor
```typescript
interface Instrutor {
  id: string
  nome: string
  email: string
  foto: string
  cpf: string
  telefone: string
  cidade: string
  biografia: string  // máx 300 caracteres
  estilo: string[]  // calmo, objetivo, rigor, motivador
  especialidades: string[]  // iniciante, ansioso, reprovado, baliza, etc.
  regioes: string[]
  disponibilidade: { dias: string[], turnos: string[] }
  veiculo: {
    tipo: 'carro_proprio' | 'carro_alugado' | 'carro_aluno' | 'moto'
    modelo?: string
    ano?: string
    temPedal?: boolean
  }
  // Métricas (calculadas pelo sistema)
  horasTrabalhadas: number
  alunosAtendidos: number
  pontualidade: number  // %
  cancelamentos: 'baixo' | 'medio' | 'alto'
  notaMedia: number
  verificado: boolean
  status: 'pendente' | 'aprovado' | 'ativo' | 'suspenso' | 'bloqueado'
  // Financeiro
  saldoDisponivel: number
  saldoPendente: number
  frequenciaRepasse: 'diario' | 'semanal' | 'mensal'
  dadosBancarios: { banco: string, tipo: 'pix' | 'conta', titular: string }
  // Currículo
  autoescolas: { nome: string, cidade: string, periodo?: string }[]
}
```

### Aula
```typescript
interface Aula {
  id: string
  candidatoId: string
  instrutorId: string
  dataHora: Date
  duracao: number  // minutos
  tipoVeiculo: 'carro_instrutor' | 'carro_aluno'
  status: 'agendada' | 'em_andamento' | 'concluida' | 'cancelada' | 'em_disputa'
  valor: number
  confirmacaoCandidato: boolean
  disputa?: { motivo: string, versaoCandidato: string, versaoInstrutor: string, decisao: string }
}
```

---

## 🔄 Fluxos Principais

### Fluxo Candidato
1. Login Google → 2. Cadastro (CPF, celular, cidade) → 3. Perfil comportamental → 4. Recomendação de aulas → 5. Pagamento → 6. Match automático → 7. Primeira aula → 8. Home (acompanhamento)

### Fluxo Instrutor
1. Cadastro + documentos → 2. Aprovação admin → 3. Preencher perfil → 4. Configurar agenda → 5. Receber alunos → 6. Dar aulas → 7. Receber pagamento

### Fluxo de Aula
Instrutor recebe → Aula acontece → Instrutor marca concluída → Candidato confirma (OK) → Dinheiro liberado (D+1)

---

## 🧠 Algoritmo de Match

```
score = (especialidade_match * 30) +
        (nota_media * 20) +
        (pontualidade * 20) +
        (1 - cancelamentos_normalizado * 10) +
        (horas_experiencia_normalizado * 10) +
        (distancia_normalizado * 10)
```

### Regras de Priority
- Iniciante → instrutor com carro + pedal
- Ansioso → instrutor especialidade "ansioso"
- Sem horário disponível → excluir
- Suspenso/bloqueado → excluir
- Candidato sem carro → instrutor com carro
- Candidato com carro → qualquer instrutor

---

## 🎁 Sistema de Bonificações

### Candidato
- 10 indicações = 1 aula grátis
- Pós-CNH: direção defensiva, baliza, estrada
- 10 ind + carro algalugado = 1 mês seguro
- Postar app = 1 aula bônus
- >10k seguidores = pacote grátis

### Instrutor
- Performance alta = 1-3 meses seguro grátis
- Parceria = manutenção grátis
- Top rating = +fluxo no match
- Nota >4.5 = -1% a -2% taxa

---

## 🏗️ Arquitetura Técnica

### Frontend
- **Linguagem:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Estado:** ViewModel + StateFlow
- **Imagens:** Coil

### Backend
- **Runtime:** Bun
- **Framework:** Express ou Hono
- **Database:** PostgreSQL (Supabase)
- **Auth:** Supabase Auth
- **Realtime:** Supabase Realtime

### Serviços Externos
- **Mapas:** Mapbox ou OSMDroid
- **Pagamentos:** Mercado Pago
- **Notificações:** Firebase Cloud Messaging

---

## 📁 Estrutura de Pastas

```
CNH-mais/
├── app/src/main/
│   ├── java/com/cnhplus/
│   │   ├── ui/
│   │   │   ├── screens/        # Telas
│   │   │   ├── components/     # Componentes reutilizáveis
│   │   │   └── theme/          # Tema (cores, tipografia)
│   │   ├── domain/
│   │   │   ├── model/          # Modelos de dados
│   │   │   ├── repository/     # Repositórios
│   │   │   └── usecase/        # Casos de uso
│   │   ├── data/
│   │   │   ├── remote/         # API calls
│   │   │   ├── local/          # Local storage
│   │   │   └── repository/     # Implementações
│   │   ├── di/                 # Injeção de dependência
│   │   └── util/               # Utilitários
│   └── res/                    # Recursos Android
│
├── backend/src/
│   ├── routes/                 # Endpoints API
│   ├── middleware/             # Intermediadores
│   ├── services/               # Lógica de negócio
│   └── utils/                  # Utilitários
│
└── docs/                       # Documentação
    ├── SPEC.md                 # Este arquivo
    ├── PERFIL-INSTRUTOR.md     # Perfil público
    ├── PAINEL-INSTRUTOR.md     # Área do instrutor
    ├── AGENDA-INSTRUTOR.md     # Sistema de agenda
    ├── BACKOFFICE.md           # Painel admin
    ├── VISAO-CANDIDATO.md      # Fluxo candidato
    ├── SISTEMA-MATCH.md        # Algoritmo
    └── BONIFICACOES.md         # Recompensas
```

---

## ✅ Checklists de Desenvolvimento

### Fase 1: Especificação (80%)
- [x] Visão geral do projeto
- [x] Fluxo candidato (8 etapas)
- [x] Fluxo instrutor (10 abas)
- [x] Sistema de agenda
- [x] Backoffice (12 blocos)
- [x] Algoritmo de match
- [x] Sistema de bonificações

### Fase 2: Design UI/UX
- [ ] Wireframes das telas
- [ ] Componentes visuais
- [ ] Protótipos

### Fase 3: Desenvolvimento Frontend
- [ ] Setup Android (Kotlin + Compose)
- [ ] Tema com cores CNH+
- [ ] Telas de candidato
- [ ] Telas de instrutor
- [ ] Navegação
- [ ] Integração API

### Fase 4: Desenvolvimento Backend
- [ ] Setup Bun + Express
- [ ] API REST
- [ ] Database Supabase
- [ ] Sistema de match
- [ ] Mercado Pago
- [ ] FCM

### Fase 5: Testes
- [ ] Unitários
- [ ] Integração
- [ ] UI

### Fase 6: Lançamento
- [ ] Play Store
- [ ] Marketing

---

## 📝 Regras de Negócio Importantes

1. **Dinheiro retido:** Pagamento do candidato fica no app até confirmação
2. **Vínculo fixo:** Após 1ª aula, vínculo é fixo até final do pacote
3. **Disputas:** Candidato pode abrir disputa se não confirmar aula
4. **Termo de responsabilidade:** Obrigatório para usar carro próprio
5. **Validação bancária:** Alteração de dados bancários leva 24-48h
6. **Indicações:** Apenas cadastros + pagamentos válidos contam

---

## 🔗 Links Úteis

- Repo: https://github.com/Deivisan/CNH-mais
- Docs: ./docs/

---

**Última atualização:** 30/03/2026
**Autor:** Deivison Santana (@deivisan)
**Versão:** 0.1.0