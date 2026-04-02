# Plano de Evolução CNH+ - Versão 1.0

## 📊 DIAGNÓSTICO ATUAL

### Status do Código (Abril 2026)

| Aspecto | Status | Nota |
|---------|--------|------|
| Build | ✅ Funcional | 8/10 |
| Arquitetura | ⚠️ Acoplamento alto | 6/10 |
| UI/UX | ⚠️ Básica, Material puro | 5/10 |
| Código | ⚠️ Screens gigantes | 6/10 |
| Testes | ❌ Zero testes | 0/10 |
| Documentação | ✅ Excelente | 9/10 |

### Problemas Críticos Identificados

1. **Screens Monolíticas**
   - `PerfilInstrutorScreen.kt`: 533 linhas (23KB)
   - `AuthScreens.kt`: 562 linhas (19KB) - 4 screens em 1 arquivo
   - `CandidatoAulasScreen.kt`: 15KB
   - `CandidatoHomeScreen.kt`: 20KB

2. **Acoplamento Global**
   - `LocalAppState.current` usado diretamente em todas as screens
   - Zero injeção de dependência
   - Difícil de testar unitariamente

3. **Performance**
   - Múltiplas instâncias `Json { }` criadas (warnings do compilador)
   - LaunchedEffect sem lifecycle awareness adequado

4. **UI/UX Defasada**
   - Material 3 puro sem personalização
   - Sem animações de transição
   - Sem tema dinâmico/material you
   - Cards simples, sem elevação/shadows modernos

5. **Dependências Desatualizadas**
   - Kotlin 2.0.21 (disponível: 2.1.20)
   - Gradle 8.10 (disponível: 8.12)
   - Compose BOM 2025.02.00 (nova disponível)

---

## 🎯 PLANO DE EVOLUÇÃO

### FASE 1: Modernização Técnica (Week 1-2)

#### 1.1 Upgrade de Stack

**Kotlin & Toolchain**
```kotlin
// build.gradle.kts (root)
plugins {
    id("com.android.application") version "8.9.0" apply false  // Android Gradle Plugin
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false  // Latest stable
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20" apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.31" apply false
}
```

**Dependências Atualizadas**
```kotlin
// app/build.gradle.kts
dependencies {
    // Core - Latest stable versions
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    
    // Compose BOM - 2025.03.00 (ou mais recente)
    implementation(platform("androidx.compose:compose-bom:2025.03.00"))
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
    
    // Serialization - Fix warnings
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    
    // DI - NOVO: Hilt
    implementation("com.google.dagger:hilt-android:2.55")
    ksp("com.google.dagger:hilt-compiler:2.55")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    
    // Coroutines - Latest
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    
    // Testing - NOVO
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
    testImplementation("io.mockk:mockk:1.13.16")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

#### 1.2 Refatoração: Singleton Json

```kotlin
// data/JsonConfig.kt
object JsonConfig {
    val default = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }
    val pretty = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }
}

// Uso em Models.kt e Repositories
fun getPerfil(): PerfilCandidato {
    return if (perfilJson != null) {
        try {
            JsonConfig.default.decodeFromString<PerfilCandidato>(perfilJson)
        } catch (e: Exception) {
            PerfilCandidato()
        }
    } else PerfilCandidato()
}
```

---

### FASE 2: Arquitetura Limpa (Week 3-4)

#### 2.1 Estrutura de Pacotes Nova

```
app/src/main/java/com/cnhplus/
├── core/                          # NÚCLEO
│   ├── di/                        # Hilt Modules
│   │   ├── NetworkModule.kt
│   │   ├── RepositoryModule.kt
│   │   └── DatabaseModule.kt
│   ├── network/                   # Network layer
│   │   ├── SupabaseClient.kt      # (existente, refatorado)
│   │   └── SupabaseInterceptor.kt # (novo)
│   └── utils/                     # Extensions & Utils
│       ├── ResultExtensions.kt
│       └── FlowExtensions.kt
│
├── data/                          # DADOS
│   ├── local/                     # DataStore, Cache
│   │   ├── SessionDataStore.kt    # (novo)
│   │   └── CacheManager.kt        # (novo)
│   ├── remote/                    # API layer
│   │   ├── dto/                   # (models existentes)
│   │   └── mappers/               # (novo) DTO → Domain
│   └── repository/                # Repository pattern
│       ├── impl/                  # Implementações
│       └── interfaces/            # Contratos
│
├── domain/                        # DOMÍNIO (NOVO)
│   ├── model/                     # Domain models (novos)
│   │   ├── Candidato.kt           # (sem @Serializable)
│   │   ├── Instrutor.kt
│   │   └── Aula.kt
│   ├── usecase/                   # Use cases (novo)
│   │   ├── auth/
│   │   │   ├── LoginUseCase.kt
│   │   │   ├── RegisterUseCase.kt
│   │   │   └── LogoutUseCase.kt
│   │   ├── candidato/
│   │   │   ├── GetCandidatoUseCase.kt
│   │   │   ├── UpdatePerfilUseCase.kt
│   │   │   └── MatchInstrutorUseCase.kt
│   │   └── instrutor/
│   │       ├── GetInstrutorUseCase.kt
│   │       ├── UpdateAgendaUseCase.kt
│   │       └── GetEarningsUseCase.kt
│   └── repository/                # Interfaces de repositório
│
├── presentation/                  # UI LAYER
│   ├── common/                    # Componentes compartilhados
│   │   ├── components/            # (existentes, organizados)
│   │   ├── theme/                 # (existente, expandido)
│   │   └── base/                  # Base classes
│   │       ├── BaseViewModel.kt   # (novo)
│   │       └── BaseScreen.kt      # (novo)
│   │
│   ├── auth/                      # Fluxo Auth (refatorado)
│   │   ├── login/
│   │   │   ├── LoginScreen.kt     # (extraído de AuthScreens.kt)
│   │   │   ├── LoginViewModel.kt  # (novo)
│   │   │   └── LoginUiState.kt    # (novo)
│   │   ├── register/
│   │   ├── welcome/
│   │   └── selectrole/
│   │
│   ├── candidato/                 # Fluxo Candidato (refatorado)
│   │   ├── home/
│   │   │   ├── CandidatoHomeScreen.kt      # (refatorado)
│   │   │   ├── CandidatoHomeViewModel.kt   # (novo)
│   │   │   ├── CandidatoHomeUiState.kt     # (novo)
│   │   │   └── components/                 # (novo)
│   │   │       ├── ProgressCard.kt
│   │   │       ├── InstrutorCard.kt
│   │   │       └── MenuGrid.kt
│   │   ├── aulas/
│   │   ├── match/
│   │   ├── pagamento/
│   │   ├── onboarding/
│   │   │   ├── comportamental/    # Wizard steps
│   │   │   ├── perfilcompleto/
│   │   │   └── recomendacao/
│   │   └── perfil/
│   │
│   ├── instrutor/                 # Fluxo Instrutor (refatorado)
│   │   ├── home/
│   │   ├── agenda/
│   │   ├── aulas/
│   │   ├── financeiro/
│   │   ├── onboarding/            # Wizard refatorado
│   │   │   ├── step1_dados_pessoais/
│   │   │   ├── step2_documentos/
│   │   │   ├── step3_veiculo/
│   │   │   ├── step4_disponibilidade/
│   │   │   └── step5_especialidades/
│   │   └── perfil/
│   │
│   └── MainActivity.kt            # Entry point
```

#### 2.2 ViewModel Pattern

```kotlin
// presentation/base/BaseViewModel.kt
abstract class BaseViewModel<UiState, Event>(
    initialState: UiState
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    protected fun updateState(transform: UiState.() -> UiState) {
        _uiState.update(transform)
    }
    
    abstract fun onEvent(event: Event)
}

// presentation/candidato/home/CandidatoHomeUiState.kt
data class CandidatoHomeUiState(
    val isLoading: Boolean = false,
    val candidato: Candidato? = null,
    val instrutor: Instrutor? = null,
    val aulasHoje: Int = 0,
    val progresso: Float = 0f,
    val error: String? = null
)

sealed class CandidatoHomeEvent {
    data object Refresh : CandidatoHomeEvent()
    data object NavigateToPerfil : CandidatoHomeEvent()
    data class NavigateToAula(val aulaId: String) : CandidatoHomeEvent()
}
```

#### 2.3 Injeção de Dependência (Hilt)

```kotlin
// core/di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
    
    @Binds
    abstract fun bindCandidatoRepository(
        impl: CandidatoRepositoryImpl
    ): CandidatoRepository
}

// presentation/candidato/home/CandidatoHomeViewModel.kt
@HiltViewModel
class CandidatoHomeViewModel @Inject constructor(
    private val getCandidatoUseCase: GetCandidatoUseCase,
    private val getInstrutorUseCase: GetInstrutorUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel<CandidatoHomeUiState, CandidatoHomeEvent>(
    CandidatoHomeUiState()
) {
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            val userId = sessionManager.getCurrentUserId()
            
            getCandidatoUseCase(userId)
                .zip(getInstrutorUseCase(userId)) { candidato, instrutor ->
                    candidato to instrutor
                }
                .collect { (candidato, instrutor) ->
                    updateState {
                        copy(
                            isLoading = false,
                            candidato = candidato,
                            instrutor = instrutor,
                            progresso = calcularProgresso(candidato)
                        )
                    }
                }
        }
    }
    
    override fun onEvent(event: CandidatoHomeEvent) {
        when (event) {
            is CandidatoHomeEvent.Refresh -> loadData()
            // ... outros eventos
        }
    }
}
```

---

### FASE 3: UI/UX Premium (Week 5-6)

#### 3.1 Design System Expandido

```kotlin
// ui/theme/DesignSystem.kt

// Cores adicionais
val GradientPrimary = Brush.verticalGradient(
    colors = listOf(Primary, PrimaryLight, Secondary)
)

val GradientSurface = Brush.verticalGradient(
    colors = listOf(
        Color.White,
        SurfaceColor,
        SurfaceColor.copy(alpha = 0.8f)
    )
)

// Elevações/Shadows
object Elevation {
    val none = 0.dp
    val small = 4.dp
    val medium = 8.dp
    val large = 16.dp
    val xlarge = 24.dp
}

// Animações
object Animation {
    val fast = 150
    val normal = 300
    val slow = 500
    val emphasis = 800
}

// Tipografia customizada
val CnhPlusTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Bold
    ),
    // ... outros estilos
)
```

#### 3.2 Componentes UI Premium

```kotlin
// common/components/CnhCard.kt
@Composable
fun CnhCard(
    modifier: Modifier = Modifier,
    elevation: Dp = Elevation.medium,
    shape: Shape = RoundedCornerShape(24.dp),
    gradient: Brush? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                spotColor = Primary.copy(alpha = 0.1f)
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (gradient == null) Color.White else Color.Transparent
        )
    ) {
        if (gradient != null) {
            Box(modifier = Modifier.background(gradient)) {
                content()
            }
        } else {
            content()
        }
    }
}

// common/components/AnimatedButton.kt
@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    Button(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled && !isLoading
    ) {
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        }
        AnimatedVisibility(visible = !isLoading) {
            Row {
                icon?.let {
                    Icon(it, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                }
                Text(text)
            }
        }
    }
}

// common/components/ShimmerLoading.kt
@Composable
fun ShimmerCard() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shimmer(shimmer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.3f))
        )
    }
}
```

#### 3.3 Transições e Animações

```kotlin
// navigation/Transitions.kt
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        content = content
    )
}

// Uso no NavHost
animatedComposable(Screen.CandidatoHome.route) {
    CandidatoHomeScreen()
}
```

#### 3.4 Screens Refatoradas - Exemplo

```kotlin
// candidato/home/CandidatoHomeScreen.kt (refatorado ~200 linhas)
@Composable
fun CandidatoHomeScreen(
    viewModel: CandidatoHomeViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = { CnhTopBar(title = "Olá, ${uiState.candidato?.nome ?: ""}!") },
        bottomBar = { CandidatoBottomNav(onNavigate) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(GradientSurface)
        ) {
            when {
                uiState.isLoading -> ShimmerLoadingContent()
                uiState.error != null -> ErrorState(
                    message = uiState.error,
                    onRetry = { viewModel.onEvent(CandidatoHomeEvent.Refresh) }
                )
                else -> CandidatoHomeContent(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

@Composable
private fun CandidatoHomeContent(
    uiState: CandidatoHomeUiState,
    onEvent: (CandidatoHomeEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ProgressCard(
                progresso = uiState.progresso,
                aulasFeitas = uiState.candidato?.aulasFeitas ?: 0,
                aulasRestantes = uiState.candidato?.aulasRestantes ?: 0
            )
        }
        
        item {
            uiState.instrutor?.let { instrutor ->
                InstrutorCard(
                    instrutor = instrutor,
                    onClick = { onEvent(CandidatoHomeEvent.NavigateToPerfil) }
                )
            } ?: EmptyInstrutorCard(
                onFindInstrutor = { onEvent(CandidatoHomeEvent.NavigateToMatch) }
            )
        }
        
        item {
            MenuGrid(
                items = menuItems,
                onItemClick = { route -> onEvent(CandidatoHomeEvent.NavigateTo(route)) }
            )
        }
    }
}
```

---

### FASE 4: Feature Completeness (Week 7-8)

#### 4.1 Wizard de Onboarding Instrutor (Refatorado)

```kotlin
// instrutor/onboarding/InstrutorOnboardingNavigation.kt
@Composable
fun InstrutorOnboardingNavHost(
    onComplete: () -> Unit
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = OnboardingStep.DadosPessoas.route
    ) {
        composable(OnboardingStep.DadosPessoas.route) {
            DadosPessoaisStep(
                onNext = { navController.navigate(OnboardingStep.Documentos.route) }
            )
        }
        composable(OnboardingStep.Documentos.route) {
            DocumentosStep(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(OnboardingStep.Veiculo.route) }
            )
        }
        composable(OnboardingStep.Veiculo.route) {
            VeiculoStep(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(OnboardingStep.Disponibilidade.route) }
            )
        }
        composable(OnboardingStep.Disponibilidade.route) {
            DisponibilidadeStep(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(OnboardingStep.Especialidades.route) }
            )
        }
        composable(OnboardingStep.Especialidades.route) {
            EspecialidadesStep(
                onBack = { navController.popBackStack() },
                onComplete = onComplete
            )
        }
    }
}
```

#### 4.2 Componentes Removidos na Wave 4 (Recreate)

```kotlin
// components/GeolocalizacaoPicker.kt
@Composable
fun GeolocalizacaoPicker(
    localidade: Localidade?,
    onLocalidadeChange: (Localidade) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Local da Aula",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(12.dp))
            
            OutlinedTextField(
                value = localidade?.endereco ?: "",
                onValueChange = { 
                    onLocalidadeChange(
                        (localidade ?: Localidade()).copy(endereco = it)
                    )
                },
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { 
                            // Abrir mapa
                            localidade?.let { loc ->
                                if (loc.latitude != null && loc.longitude != null) {
                                    val uri = "https://maps.google.com/maps?q=${loc.latitude},${loc.longitude}"
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Map, contentDescription = "Ver no mapa")
                    }
                }
            )
        }
    }
}
```

---

### FASE 5: Testing & Quality (Week 9-10)

#### 5.1 Testes Unitários

```kotlin
// test/domain/usecase/LoginUseCaseTest.kt
@ExperimentalCoroutinesApi
class LoginUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    private lateinit var useCase: LoginUseCase
    private val mockAuthRepository = mockk<AuthRepository>()
    private val mockSessionManager = mockk<SessionManager>()
    
    @Before
    fun setup() {
        useCase = LoginUseCase(mockAuthRepository, mockSessionManager)
    }
    
    @Test
    fun `invoke with valid credentials returns success`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "123456"
        val expectedUser = User(id = "1", email = email)
        
        coEvery { mockAuthRepository.login(email, password) } returns Result.success(expectedUser)
        coEvery { mockSessionManager.saveUser(expectedUser) } just Runs
        
        // When
        val result = useCase(email, password)
        
        // Then
        assertTrue(result.isSuccess)
        coVerify { mockSessionManager.saveUser(expectedUser) }
    }
}
```

#### 5.2 Testes de UI (Compose)

```kotlin
// androidTest/presentation/candidato/CandidatoHomeScreenTest.kt
@HiltAndroidTest
class CandidatoHomeScreenTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Test
    fun candidatoHome_displaysProgressCard() {
        composeTestRule.setContent {
            CandidatoHomeScreen()
        }
        
        composeTestRule.onNodeWithText("Seu Progresso").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Progresso").assertIsDisplayed()
    }
}
```

---

## 📅 ROADMAP DE EXECUÇÃO

### Semana 1-2: Foundation
- [ ] Upgrade Kotlin 2.1.20 + Gradle 8.12
- [ ] Atualizar todas as dependências
- [ ] Implementar Hilt DI
- [ ] Criar estrutura de pacotes nova
- [ ] Mover DTOs para domain models

### Semana 3-4: Architecture
- [ ] Refatorar AppState → ViewModels
- [ ] Criar Use Cases principais
- [ ] Implementar Repository Pattern completo
- [ ] Criar BaseViewModel
- [ ] Refatorar repositories com interfaces

### Semana 5-6: UI/UX
- [ ] Implementar Design System completo
- [ ] Criar componentes premium
- [ ] Adicionar transições animadas
- [ ] Implementar Shimmer loading
- [ ] Refatorar screens grandes

### Semana 7-8: Features
- [ ] Wizard Instrutor refatorado
- [ ] Recriar componentes removidos
- [ ] Implementar agenda interativa
- [ ] Adicionar mapa de localização
- [ ] Sistema de notificações local

### Semana 9-10: Quality
- [ ] Setup testing framework
- [ ] Escrever testes unitários críticos
- [ ] Escrever testes de UI principais
- [ ] Performance profiling
- [ ] Memory leak detection

---

## 🎨 MELHORIAS VISUAIS ESPECÍFICAS

### 1. Home Candidato
- [ ] **Hero Animation**: Card de instrutor com parallax
- [ ] **Micro-interações**: Botões com ripple customizado
- [ ] **Progresso Animado**: Circular progress com animação fluida
- [ ] **Skeleton Loading**: Shimmer em vez de spinner

### 2. Onboarding
- [ ] **Page Transition**: Shared element transitions entre steps
- [ ] **Form Validation**: Validação em tempo real com animação
- [ ] **Image Upload**: Preview com crop circular animado

### 3. Agenda Instrutor
- [ ] **Calendar**: Custom calendar view com seleção fluida
- [ ] **Time Slots**: Chips animados para horários
- [ ] **Drag & Drop**: Reorganizar horários (futuro)

### 4. Match Screen
- [ ] **Card Stack**: Visual de pilha de instrutores
- [ ] **Swipe Gestures**: Swipe para aceitar/recusar
- [ ] **Profile Animation**: Expandir perfil com hero animation

---

## 🔧 CONFIGURAÇÕES ESPECÍFICAS

### Gradle Configuration
```kotlin
// gradle.properties
org.gradle.jvmargs=-Xmx8g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.parallel=true
org.gradle.daemon=true
kotlin.code.style=official
kotlin.incremental=true
kotlin.incremental.android=true
android.useAndroidX=true
android.enableJetifier=true
android.experimental.enableSourceSetPathsMap=true
```

### ProGuard/R8 (para release)
```kotlin
// app/proguard-rules.pro
# Keep DTOs for serialization
-keep class com.cnhplus.data.** { *; }
-keep class com.cnhplus.domain.model.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Keep for Hilt
-keep class dagger.hilt.** { *; }
```

---

## ✅ CHECKLIST DE CONCLUSÃO

### Fase 1 Completa quando:
- [ ] Build com Kotlin 2.1.20 sem erros
- [ ] Hilt injetando dependências
- [ ] Todos warnings de Json resolvidos

### Fase 2 Completa quando:
- [ ] Zero referências a `LocalAppState`
- [ ] Todos ViewModels usando BaseViewModel
- [ ] Use Cases cobrindo 80% dos fluxos

### Fase 3 Completa quando:
- [ ] Todas screens < 250 linhas
- [ ] Animações em todas as navegações
- [ ] Design System documentado

### Fase 4 Completa quando:
- [ ] Wizard refatorado funcionando
- [ ] Componentes removidos recriados
- [ ] Agenda visualmente completa

### Fase 5 Completa quando:
- [ ] Cobertura de testes > 60%
- [ ] CI rodando testes automaticamente
- [ ] APK release < 25MB otimizado

---

**Data do Plano:** Abril 2026
**Versão do Plano:** 1.0
**Próxima Revisão:** Após Fase 1
