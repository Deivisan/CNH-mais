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
├── index.html                  # Landing Page (GitHub Pages)
├── demo-pwa/                   # Demo PWA navegável (estático)
├── AGENTS.md                   # Contexto para agentes IA
├── PROJECT_README.md           # Documentação do projeto
├── .nojekyll                   # GitHub Pages config
├── app/src/main/               # App Android (futuro)
│   └── java/com/cnhplus/
│
├── backend/src/                # Backend API (futuro)
│
└── docs/                       # Documentação
    ├── SPEC.md
    ├── PERFIL-INSTRUTOR.md
    ├── PAINEL-INSTRUTOR.md
    ├── AGENDA-INSTRUTOR.md
    ├── BACKOFFICE.md
    ├── VISAO-CANDIDATO.md
    ├── SISTEMA-MATCH.md
    ├── BONIFICACOES.md
    ├── ROADMAP-DEMO-PWA-20DIAS.md
    ├── CHECKLIST-VALIDACAO-DEMO-PWA.md
    └── ESPEC-COBERTURA-DEMO-PWA.md
```

---

## 🌐 Estratégia Web Atual (Landing + Demo PWA)

### 1) Landing principal (`/index.html`)
- Mantida como vitrine do APK e posicionamento do produto
- Status atual: **aguardando aprovação de visual e designer**
- Deve permanecer simples e objetiva para comunicação institucional

### 2) Demo PWA (`/demo-pwa/`)
- Ambiente navegável completo para validação de fluxo ponta-a-ponta
- 100% estático (compatível com GitHub Pages)
- Inclui:
  - login/cadastro dos 3 perfis (emulado)
  - fluxo candidato/instrutor/admin
  - chat emulado
  - mapas emulados com OpenStreetMap embed
  - painel admin com tela de gerenciamento do instrutor (12 blocos)

### 3) Limitações técnicas do Pages (assumidas)
- Sem backend/server-side
- Sem autenticação real segura no servidor
- Sem banco de dados real
- Sem websocket real

### 4) Documentos de governança da demo
- `docs/ROADMAP-DEMO-PWA-20DIAS.md`
- `docs/CHECKLIST-VALIDACAO-DEMO-PWA.md`
- `docs/ESPEC-COBERTURA-DEMO-PWA.md`

Esses arquivos são a referência oficial de progresso, validação e aderência à especificação.

## 🌐 Landing Page (index.html)

### Dados Emulados (ativos atualmente)
| Dado | Valor | Tooltip ao clicar |
|------|-------|-------------------|
| Instrutores | 500+ 🚗 | 500+ em 12 estados, taxa aprovação 87% |
| Alunos | 10.000+ 🎓 | 10.000+ formados, 92% passaram na 1ª |
| Nota média | 4.8 ⭐ | 4.8/5.0, 2.500+ avaliações |

### Instrutores de Exemplo (Landing Page)
| Nome | Local | Especialidade |
|------|-------|---------------|
| Carlos Silva | Cuiabá - MT | Iniciantes, ansiedade |
| Maria Santos | **Feira de Santana - BA** | Baliza, provas práticas |
| Pedro Oliveira | Belo Horizonte - MG | Carro próprio |

### Contato (Footer)
- **WhatsApp:** (71) 8300-0722
- **Email:** suporte@cnhmais.com.br
- **Endereço:** Rua das Flores, 123 - Feira de Santana, BA

---

## ✅ Checklists de Desenvolvimento

### Fase 1: Especificação (100%) ✅
- [x] Visão geral do projeto
- [x] Fluxo candidato (8 etapas)
- [x] Fluxo instrutor (10 abas)
- [x] Sistema de agenda
- [x] Backoffice (12 blocos)
- [x] Algoritmo de match
- [x] Sistema de bonificações

### Fase 2: Landing Page (100%) ✅
- [x] Design responsivo
- [x] Cores CNH+ (azul/celeste)
- [x] Seções: Hero, Sobre, Como Funciona, Diferenciais, Instrutores, CTA, Footer
- [x] Dados emulados com tooltips interativos
- [x] Hospedagem GitHub Pages

### Fase 3: App Android (100%) ✅
- [x] Setup Android (Kotlin + Compose)
- [x] Tema com cores CNH+
- [x] Telas de candidato
- [x] Telas de instrutor
- [x] Telas admin (stubs)
- [x] Navegação
- [x] Integração Supabase API
- [x] APK compilado v0.0.5 (BUILD SUCCESSFUL)

### Fase 4: Backend (0%)
- [ ] Setup Bun + Express
- [ ] API REST
- [ ] Database Supabase
- [ ] Sistema de match
- [ ] Mercado Pago
- [ ] FCM

### Fase 5: Testes (0%)
- [ ] Unitários
- [ ] Integração
- [ ] UI

### Fase 6: Lançamento (0%)
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

- **Landing Page:** https://deivisan.github.io/CNH-mais/
- **Repo:** https://github.com/Deivisan/CNH-mais
- **Docs:** ./docs/

---

## 🚀 GSD Workflow (Get Shit Done)

### Regras de Execução
1. **Compile ANTES de build APK**: Sempre `./gradlew compileDebugKotlin` primeiro (~1min). Só roda `./gradlew assembleDebug` quando BUILD SUCCESSFUL.
2. **Waves sequenciais**: Wave 1 = infra/models → Wave 2 = repos/network → Wave 3 = screens/UI → Wave 4 = verify/ship. Cada wave deve compilar zerado antes de avançar.
3. **Commits atômicos**: Cada batch de fixes = 1 commit descritivo. `git add . && git commit -m "fix: description"` após cada wave valida.
4. **Zero tolerância com inline/private**: `public inline fun` NUNCA acessa `private` members. Use `@PublishedApi internal` nos campos.
5. **LocalAppState.current é a ÚNICA fonte de verdade**: Zero `LocalAppSession`, zero ViewModels. Tudo via CompositionLocal.

### Stack Confirmada
- **Kotlin** 1.9.24 + **Compose** 1.6.0
- **Supabase** (PostgreSQL + Auth + Realtime)
- **OkHttp** raw HTTP (sem Retrofit)
- **kotlinx.serialization** 1.6.3
- **Jetpack Compose** Material 3
- **DataStore** para persistência local

---

**Última atualização:** 01/04/2026
**Autor:** Deivison Santana (@deivisan)
**Versão:** 0.0.5 - APK Android compilado, zero erros
