# Documentação [NOME PENDENTE] - Funções e Componentes

> ⚠️ **VERSÃO:** 0.03 Pre-Alpha (05/04/2026)  
> 🔧 **STATUS:** Código implementado, aguardando build e testes  
> 📝 **NOTA:** O nome "CNH+" já existe no mercado e será alterado para uma marca não patentada.

## Visão Geral

Esta documentação descreve as principais funções, componentes e fluxos do aplicativo (atualmente chamado CNH+), um marketplace que conecta candidatos à CNH com instrutores de direção.

**Versão atual:** v0.03  
**Estado:** Implementação completa, código pronto para build

---

## 📱 Arquitetura do App

### Estrutura de Pacotes

```
app/src/main/java/com/cnhplus/
├── screens/           # Telas do aplicativo
│   ├── candidato/     # Fluxo do candidato
│   ├── instrutor/     # Fluxo do instrutor
│   ├── auth/          # Autenticação
│   └── ...
├── ui/components/     # Componentes reutilizáveis
├── data/             # Modelos e repositórios
├── navigation/         # Navegação
└── app/              # Estado global do app
```

---

## 🎯 Fluxos Principais

### Fluxo Candidato

```
Welcome → Login → Register → RegisterSuccess → SelectRole → PerfilCompleto → OnboardingCandidato → PerfilComportamental → RecomendacaoAulas → CandidatoHome
```

**Funções Principais:**

#### `CandidatoHomeScreen`
- **Arquivo:** `screens/candidato/CandidatoHomeScreen.kt`
- **Função:** Dashboard principal do candidato
- **Componentes:** BannerCarrossel, Progress Card, Instrutor Card, Menu Cards, FooterBanners
- **Dados:** Exibe progresso de aulas, instrutor vinculado, menu de ações

#### `PerfilComportamentalScreen`
- **Arquivo:** `screens/candidato/PerfilComportamentalScreen.kt`
- **Função:** Questionário de perfil para algoritmo de match
- **Perguntas:** Experiência, ansiedade, histórico de reprovação, dificuldades, objetivo, disponibilidade

#### `CandidatoMatchScreen`
- **Arquivo:** `screens/candidato/CandidatoMatchScreen.kt`
- **Função:** Lista de instrutores ordenados por score de compatibilidade
- **Algoritmo:** `calcularScoreMatch()` - pondera especialidades, nota, pontualidade, cancelamentos, experiência, distância

---

### Fluxo Instrutor

```
Welcome → Login → Register → RegisterSuccess → SelectRole → Wizard 5 Steps → InstrutorHome
```

**Wizard 5 Steps:**
1. **DadosPessoaisStep** - Nome, CPF, telefone, cidade, biografia, foto
2. **DocumentosStep** - Upload CNH e CRLV (para Supabase Storage)
3. **VeiculoStep** - Tipo de veículo, modelo, ano, pedal de emergência
4. **DisponibilidadeStep** - Dias e turnos disponíveis
5. **EspecialidadesStep** - Estilo de ensino e especialidades

**Funções Principais:**

#### `InstrutorOnboardingScreen`
- **Arquivo:** `presentation/instrutor/onboarding/InstrutorOnboardingScreen.kt`
- **Função:** Orquestra os 5 steps do wizard
- **ViewModel:** `InstrutorWizardViewModel` - mantém estado entre steps

#### `InstrutorHomeScreen`
- **Arquivo:** `screens/instrutor/InstrutorHomeScreen.kt`
- **Função:** Dashboard do instrutor
- **Componentes:** Stats Card (aulas hoje, ganhos, avaliação), Menu Cards, BannerCarrossel, FooterBanners

#### `InstrutorAgendaScreen`
- **Arquivo:** `screens/instrutor/InstrutorAgendaScreen.kt`
- **Função:** Visualização de aulas agendadas
- **Filtros:** Aulas do dia, próximas aulas, histórico

---

## 🧩 Componentes Reutilizáveis

### BannerCarrossel

**Arquivo:** `ui/components/BannerCarrossel.kt`

```kotlin
@Composable
fun BannerCarrossel(
    banners: List<BannerDto>,
    onBannerClick: (BannerDto) -> Unit,
    modifier: Modifier = Modifier
)
```

**Função:** Carrossel de banners promocionais com auto-scroll
**Uso:** Telas principais (Home, Aulas, Agenda, etc.)
**Integração:** Fetch de `bannerRepo.getActiveBanners()`

---

### FooterBanners

**Arquivo:** `ui/components/FooterBanners.kt`

```kotlin
@Composable
fun FooterBanners(
    onGasolinaClick: () -> Unit,
    onSeguroClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

**Função:** Banners de parceiros no rodapé (gasolina e seguro)
**Uso:** Telas com scroll (Home, Aulas, Perfil)

---

### LocalizacaoAulaCard

**Arquivo:** `ui/components/LocalizacaoAulaCard.kt`

```kotlin
@Composable
fun LocalizacaoAulaCard(
    endereco: String,
    latitude: Double?,
    longitude: Double?,
    referencia: String? = null,
    onEditarLocal: () -> Unit = {}
)
```

**Função:** Mostra local da aula com botão para abrir Google Maps
**Ações:**
- Abrir Maps: Lança Intent com geo:latitude,longitude
- Alterar: Callback para edição do local

---

### DenunciaDialog

**Arquivo:** `ui/components/DenunciaDialog.kt`

```kotlin
@Composable
fun DenunciaDialog(
    aulaId: String,
    denunciadoNome: String = "",
    onDismiss: () -> Unit,
    onSubmit: (motivo: String, descricao: String, midiaUri: Uri?) -> Unit
)
```

**Função:** Formulário para reportar problemas em aulas
**Campos:**
- Motivo (dropdown): Comportamento inadequado, segurança, qualidade, etc.
- Descrição: Texto detalhado do ocorrido
- Mídia: Upload opcional de foto/vídeo

---

## 🔧 Repositórios e Data Layer

### InstrutorRepository

**Arquivo:** `data/repository/InstrutorRepository.kt`

**Funções:**
- `getInstrutor(id)` - Busca instrutor por ID
- `getActiveInstrutores()` - Lista instrutores ativos
- `createInstrutor(dto)` - Cria novo instrutor
- `updateInstrutor(id, fields)` - Atualiza campos específicos
- `updateDisponibilidade(id, disponibilidade)` - Atualiza agenda
- `updateVeiculo(id, veiculo)` - Atualiza dados do veículo
- `uploadDocumento(userId, tipo, bytes)` - **NOVO** Upload CNH/CRLV para Supabase Storage

---

### SupabaseClient

**Arquivo:** `network/SupabaseClient.kt`

**Funções de Storage:**
```kotlin
fun uploadFile(
    bucket: String,
    filePath: String,
    fileBytes: ByteArray,
    contentType: String = "image/jpeg"
): Result<String>  // Retorna URL pública

fun deleteFile(bucket: String, filePath: String): Result<Unit>
fun getPublicUrl(bucket: String, filePath: String): String
```

---

## 🔄 Navegação

### NavHost.kt

**Funções de Navegação:**

```kotlin
// Navegação por role após seleção
private fun navigateByRole(navController: NavHostController, role: String)
// - "candidato" → PerfilCompleto
// - "instrutor" → PerfilInstrutor (Wizard)
// - "admin" → (não implementado, fica na tela)

// Bottom Navigation Scaffolds
private fun CandidatoScaffold(...)  // Tabs: Home, Aulas, Match, Perfil
private fun InstrutorScaffold(...)  // Tabs: Home, Agenda, Aulas, Financeiro, Perfil
```

---

## 🐛 Correções Implementadas (Fase 1)

### 1. Navegação WelcomeScreen Loop
**Problema:** Usuário logado sem role voltava para Welcome
**Solução:** Em `MainActivity.kt`, verificar role "pendente" e redirecionar para SelectRole

### 2. Erros Silenciosos
**Problema:** Falhas no selectRole não mostravam feedback
**Solução:** Adicionar `errorMessage` state e `AlertDialog` em `AuthScreens.kt`

### 3. Upload de Documentos
**Problema:** Documentos não eram enviados para backend
**Solução:** 
- Adicionar `uploadDocumento()` no `InstrutorRepository`
- Converter URI para ByteArray no `DocumentosStep`
- Fazer upload antes de criar instrutor no `InstrutorWizardViewModel`

### 4. Navegação Admin
**Problema:** Role "admin" não tinha tratamento
**Solução:** Adicionar caso no `navigateByRole()` com comentário TODO

### 5. Máscaras CPF/Telefone
**Problema:** Campos sem formatação
**Solução:** Adicionar `formatCPF()` e `formatPhone()` em `OnboardingCandidatoScreen.kt`

---

## 📊 Estado do Projeto

### Telas com BannerCarrossel + FooterBanners:
✅ CandidatoHomeScreen
✅ CandidatoAulasScreen
✅ CandidatoMatchScreen
✅ CandidatoPagamentoScreen
✅ CandidatoPerfilScreen
✅ InstrutorHomeScreen
✅ InstrutorAgendaScreen
✅ InstrutorAulasScreen
✅ InstrutorFinanceiroScreen
✅ InstrutorPerfilScreen

### Componentes Criados:
✅ LocalizacaoAulaCard.kt
✅ DenunciaDialog.kt

### TODOs Pendentes:
- Implementar AdminHomeScreen
- Implementar navegação dos banners (onClick)
- Implementar tela de parceiros (gasolina/seguro)
- Salvar perfil comportamental no backend
- Integrar pagamento real (Mercado Pago)

---

## 🚀 Próximos Passos Recomendados

1. **Testar fluxo completo** de onboarding candidato e instrutor
2. **Implementar upload de avatar/foto** de perfil
3. **Criar tela Admin** para gerenciamento
4. **Integrar notificações push** (Firebase)
5. **Adicionar analytics** para métricas de uso

---

**Documentação criada em:** 2026-04-05
**Versão:** v0.02 Rebase
**Total de arquivos modificados:** 15+
**Total de arquivos criados:** 2
