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

### Fase 1: Foundation (Week 1-2) — EM ANDAMENTO 🚧
- [ ] **Kotlin 2.1.20** — upgrade toolchain
- [ ] **Gradle 8.12** — performance build  
- [ ] **Hilt DI** — injeção de dependência
- [ ] **JsonConfig** — singleton para serialização
- [ ] **Estrutura core/data/domain/presentation**

### Fase 2: Architecture (Week 3-4)
- [ ] BaseViewModel pattern
- [ ] Use Cases implementation
- [ ] Repository interfaces
- [ ] Migração LocalAppState → Hilt

### Fase 3: UI/UX Premium (Week 5-6)
- [ ] Design System completo
- [ ] Animações de transição
- [ ] Shimmer loading
- [ ] Screens < 250 linhas

### Fase 4: Features (Week 7-8)
- [ ] Wizard Instrutor refatorado
- [ ] Componentes removidos recriados
- [ ] Agenda interativa

### Fase 5: Quality (Week 9-10)
- [ ] Testes unitários > 60%
- [ ] CI/CD pipeline
- [ ] APK otimizado < 25MB

---

## 🏗️ Nova Arquitetura (Fase 2+)

```
core/         → DI + Network + Utils
data/         → Local + Remote + Repository
domain/       → Models + Use Cases (NOVO!)
presentation/ → UI organizada por fluxo
```

### Padrões
- **MVVM**: ViewModels injetados via Hilt
- **Use Cases**: Cada operação de negócio = 1 use case
- **Repository**: Interfaces + implementações
- **State**: Unidirecional (UI → Event → ViewModel → State → UI)

---

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

## 🔐 Supabase Config (REAL — NÃO trocar)

| Item | Valor |
|------|-------|
| Projeto | `ibyngfqddoefatqtojfj` (DeiviTech, sa-east-1) |
| Anon Key | `sb_publishable_SvWV-VYW4WbqLVoPL_VTTg_SZGF3e2P` |
| URL | `https://ibyngfqddoefatqtojfj.supabase.co` |
| Trigger | `handle_new_user` — auto-cria profiles em `profiles` no signup |
| Email confirm | **DESATIVADA** para dev (Providers → Email) |
| SHA-1 debug | `77:91:24:95:44:76:40:F7:98:13:E0:3A:25:23:52:15:7E:42:9B:5B` |
| MCP | ✅ Configurado em opencode.jsonc — project_ref=ibyngfqddoefatqtojfj |

### Conta de Teste
| Email | Senha | Role |
|-------|-------|------|
| `deivilsantana@outlook.com` | `33484@Cnh.` | candidato |

### API Key (banco-api.md — repo Banco-Api)
`sbp_v0_87ec6d65d35c877881e75f96a396fdd5b412c574` — Access Token para Supabase MCP

---

## 🚀 GSD Workflow (Get Shit Done)

### 🔴 Regras de Ouro (NUNCA ignorar)
1. **Gradle Build Cache é INIMIGO #1**: `clean` NÃO limpa .dex caches. SEMPRE usar `rm -rf app/build && ./gradlew --no-build-cache --rerun-tasks assembleDebug`
2. **Verificar APK**: `unzip -p app-debug.apk classes.dex | strings | grep -i "ibyngfq"` — deve retornar a key real, NUNCA "placeholder" ou "REPLACE"
3. **Compile ANTES de build APK**: `./gradlew compileDebugKotlin` primeiro (~1min). Só `assembleDebug` quando BUILD SUCCESSFUL.
4. **Fail fast**: Corrigir erro específico sem reescrever arquivo inteiro.
5. **Escopo cirúrgico**: Foco 100% em `app/src/`. Landing/demo ficam em branch `web`.
6. **SHA256 ANTES de release**: comparar hash APK novo vs anterior. Hash igual = NÃO release.

### Waves Sequenciais (CADA wave DEVE compilar antes de avançar)
1. **Wave 1**: infra/models/datastore
2. **Wave 2**: repos/network (SupabaseClient)
3. **Wave 3**: screens/UI/auth flow
4. **Wave 4**: verify/compile → APK → release

### Regras Técnicas
6. **Zero tolerância com inline/private**: `public inline fun` NUNCA acessa `private` members. Use `@PublishedApi internal`.
7. **`LocalAppState.current` é ÚNICA fonte de verdade**: Zero `LocalAppSession`, zero ViewModels.
8. **Compose brace matching**: `Row(...) { content }` — NUNCA `Row(...)` sem `{`.
9. **Commits atômicos**: 1 batch de fixes = 1 commit descritivo.
10. **2 branches máximo**: `master` (APK) + `web` (Landing/Demo). Sem auxiliares.

### Checklist Release (OBRIGATÓRIO)
- [ ] `git diff --stat origin/master` tem mudanças reais em `.kt` ou recursos?
- [ ] SHA256 APK novo ≠ SHA256 APK anterior (verificação anti-duplicação)
- [ ] `unzip -p app-debug.apk classes.dex | strings | grep -c "ibyngfq"` → key presente?
- [ ] Asset NA RELEASE renomeado pra `cnhmais.apk` (genérico, sem versão)
- [ ] Landing: link aponta pra `latest/download/cnhmais.apk` (já configurado)

### Stack Confirmada
- **Kotlin** 1.9.24 + **Compose** 1.6.0
- **Supabase** (PostgreSQL 17.6 + Auth + Realtime)
- **OkHttp** raw HTTP (sem Retrofit)
- **kotlinx.serialization** 1.6.3
- **Jetpack Compose** Material 3
- **DataStore** para persistência local

---

### 🤖 Prompts para Próximos Agentes de IA

**AO INICIAR TRABALHO:**
```
1. Ler .claude/napkin.md PRIMEIRO — contém regras NEVER-AGAIN
2. Ler AGENTS.md seção "Supabase Config" — key e URL reais
3. Verificar se APK tem key real: strings classes.dex | grep ibyngfq
4. Testar auth com conta existente: deivilsantana@outlook.com / 33484@Cnh.
5. PRIMEIRO compilar: ./gradlew compileDebugKotlin
6. SÓ DEPOIS build APK: ./gradlew --no-build-cache --rerun-tasks assembleDebug
```

**PRIORIDADE DE TRABALHO:**
```
Wave 1: Auth + State Management (✅ ESTABILIZADO v0.06)
  - ✅ Login com deivilsantana@outlook.com funciona
  - ✅ Register NÃO crasha mais (fix saveSession race condition)
  - ✅ WelcomeScreen com 3 slides + animações
  - ✅ RegisterSuccessScreen com feedback visual
  - ✅ Session persistence via DataStore OK
  - ⬜ OnboardingCandidatoScreen (perfil comportamental real)

Wave 2: Perfil + Dados do Candidato (✅ COMPLETO v0.07)
  - ✅ PerfilCompletoScreen com upload foto (camera + galeria)
  - ✅ RLS policies verificadas e corrigidas
  - ✅ Supabase Storage buckets criados (avatars, documentos)
  - ✅ Permissões CAMERA + READ_MEDIA no AndroidManifest
  - ✅ ImagePicker reutilizável + PermissionsHandler

Wave 3: Instrutor + Perfil (✅ COMPLETO v0.07)
  - ✅ PerfilInstrutorScreen com upload foto + form + docs
  - ✅ Tela de seleção de role envia instrutores para PerfilInstrutor
  - ✅ RLS policies para instrutores verificadas
  - ⬜ Upload documentos (CRLV, CNH) — próxima etapa
  - ⬜ Tela de agenda

Wave 4: Match + Features
  - ⬜ Algoritmo de match
  - ⬜ Pagamento
  - ⬜ Chat
```

---

## 📋 Changelog — v0.06 (2026-04-01)

### ✅ Fixes Críticos
1. **AppState.register()**: Corrigido crash após signup
   - Era: `saveSession` antes do profile existir → race condition
   - Agora: retry 3x com backoff (1s, 2s, 3s), fallback create profile, depois save session
   - Smart cast workaround: `val finalProfile = profile!!`
   
2. **Navigation Flow**: Welcome → Register → Success → SelectRole → Onboarding
   - Adicionado `Screen.Welcome` e `Screen.RegisterSuccess`
   - startDestination alterado de `Login` para `Welcome`
   - Registro agora mostra tela de sucesso animada antes de SelectRole

### 🎨 Novas Telas (100% funcionais)
1. **WelcomeScreen.kt**
   - HorizontalPager nativo do Compose (Foundation API)
   - 3 slides com ícones animados
   - Indicadores de página (dots)
   - Botão "Pular" / "Próximo" / "Começar"

2. **RegisterSuccessScreen**
   - Icon CheckCircle com animação de scale infinita
   - Gradiente verde (Success → Accent)
   - Mensagem personalizada com nome do usuário
   - Botão "Continuar" leva para SelectRole

### 🔧 Stack Updates
- **Compose Foundation Pager**: Substituído accompanist-pager (deprecated) por API nativa
- **Animações**: `rememberInfiniteTransition` para feedback visual
- **Navigation**: Rotas organizadas com popUpTo + inclusive para limpar backstack

---

## 📋 Changelog — v0.07 (2026-04-01)

### ✅ Perfis + Permissões + Storage
1. **PerfilCompletoScreen.kt** — Tela completa de perfil após registro
   - ImagePickerButton (camera + galeria)
   - CPF formatado (000.000.000-00)
   - Celular formatado ((00) 00000-0000)
   - Upload foto para Supabase Storage
   - Salva profile + candidato simultaneamente

2. **PerfilInstrutorScreen.kt** — Onboarding completo para instrutores
   - ImagePickerButton (camera + galeria)
   - CPF formatado (000.000.000-00)
   - Telefone formatado ((00) 00000-0000)
   - Biografia (max 300 chars com contador)
   - Upload foto para Supabase Storage
   - Salva profile + instrutor simultaneamente
   - Botão "Preencher depois" (skip)
   - Placeholder para upload de CRLV + CNH (próxima etapa)

3. **PermissionsHandler.kt** — Hooks declarativos para runtime permissions
   - `rememberPermissionState()` — permissão única
   - `rememberMultiplePermissionsState()` — múltiplas
   - `getPhotoPermissions()` — Android 13+ (READ_MEDIA_IMAGES) vs legacy (READ_EXTERNAL_STORAGE)

4. **ImagePicker.kt** — Componente reutilizável
   - ModalBottomSheet: Câmera OU Galeria
   - FileProvider para camera captures
   - Compressão JPEG automática (max 1024KB)

5. **SupabaseClient.kt** — Storage methods
   - `uploadFile(bucket, path, bytes, contentType)` → URL pública
   - `deleteFile(bucket, path)` → Result<Unit>
   - `getPublicUrl(bucket, path)` → String

6. **Supabase SQL Migrations**
   - Storage buckets: `avatars` (público, 5MB), `documentos` (privado, 10MB)
   - RLS policies para profiles, candidatos, instrutores, avatares, storage
   - `handle_new_user` search_path fixado
   - Indexes de performance

7. **Deprecated Fixes**
   - `Divider()` → `HorizontalDivider()` em todas telas
   - `Icons.Default.Logout` → `Icons.Filled.Close`

### 🔄 Navigation Flow Completa
```
Primeira vez:
  Welcome → Register → Success → SelectRole ─┐
                                             ├→ Candidato: PerfilCompleto → OnboardingCandidato → CandidatoHome
                                             │
  Login → SelectRole ────────────────────────┤
                                             ├→ Instrutor: PerfilInstrutor → InstrutorHome
                                             │
                                              └→ Instrutor: PerfilInstrutor → InstrutorHome
```

### 📦 APK
- Tamanho: 19MB
- Build: `--no-build-cache --rerun-tasks`
- Supabase Storage habilitado
- Todas permissões no AndroidManifest
- Pronto para build (compileDebugKotlin: ✅ BUILD SUCCESSFUL)

---

## 📋 Changelog — v0.08 Alpha (2026-04-01)

### ✅ Novas Funcionalidades
1. **ChatScreen.kt** — Chat entre candidato e instrutor
   - Polling a cada 3 segundos para novas mensagens
   - Bubbles com distinção visual (enviada vs recebida)
   - Scroll automático para última mensagem
   - Integração com MensagemRepository

2. **DenunciaScreen.kt** — Sistema de denúncias
   - 7 motivos pré-definidos (assédio, comportamento inadequado, etc.)
   - Campo de descrição com contador (max 1000 chars)
   - Placeholder para upload de mídia (câmera/galeria)
   - Tela de sucesso com feedback visual
   - Integração com DenunciaRepository

3. **Upload Documentos (PerfilInstrutorScreen)**
   - File picker para CNH (ActivityResultContracts.GetContent)
   - File picker para CRLV
   - Upload para bucket 'documentos' no Supabase Storage
   - Feedback visual: card verde quando documento enviado
   - Helper readBytesFromUri para leitura de arquivos

4. **Botões de Ação nas Aulas**
   - CandidatoAulasScreen: Chat + Denunciar (para aulas agendada/em_andamento)
   - InstrutorAulasScreen: Chat (sempre visível) + Iniciar/Concluir Aula

### 🗑️ Remoções
- **Módulo Admin completo**: 6 telas deletadas (AdminHome, AdminInstrutores, AdminAlunos, AdminAulas, AdminFinanceiro, AdminConfig)
- Rotas admin removidas do NavHost.kt
- Screen.Admin* removidos do Screen.kt
- Case "admin" removido do navigateByRole e MainActivity

### 🔧 Navegação
- Screen.Chat com rota dinâmica: `chat/{aulaId}`
- Screen.Denuncia com rota dinâmica: `denuncia/{aulaId}`
- SavedStateHandle para passar receiverId/receiverName/denunciadoId/denunciadoNome

### 📦 Release
- **v0.08-alpha**: https://github.com/Deivisan/CNH-mais/releases/tag/v0.08-alpha
- Asset: `cnhmais.apk` (19MB)
- SHA256: `a57585b85345efb0071da854ebad1bc4d5ab5f53f862f729269d05219191d7bb`

---

**Última atualização:** 01/04/2026 — v0.08 Alpha
**Autor:** Deivison Santana (@deivisan)
**Versão:** 0.08 Alpha — Chat + Denúncias + Upload Documentos + Admin removido
**Asset naming:** `cnhmais.apk` (genérico, sem versão) → landing usa `latest/download/cnhmais.apk`
**Supabase MCP:** ✅ Ativo em opencode.jsonc — project_ref=ibyngfqddoefatqtojfj
