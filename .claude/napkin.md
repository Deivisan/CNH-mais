# Napkin Runbook — CNH+ Android App v0.5.0

## Curation Rules
- Re-prioritize on every read (highest-value first)
- Keep only recurring, reusable guidance + critical gotchas
- Max 10 items per category
- Each item includes date + actionable "Do instead"

---

## 🎯 Execution & Validation (HIGHEST PRIORITY)

1. **[2026-04-01] NO build until SUPABASE_ANON_KEY replaced**
   Do instead: Keep placeholder `s0_placeholder_XXXXXXXX...` until real key obtained from Supabase console. First build WILL fail on key validation at runtime if placeholder remains.

2. **[2026-04-01] LocalAppState.current mandatory in ALL composables**
   Do instead: Every screen must access `val app = LocalAppState.current` from CompositionLocal. Never use ViewModels, never access AppState via constructor. Pattern: `val app = LocalAppState.current; LaunchedEffect(Unit) { app.candidatoRepo.fetch(...) }`

3. **[2026-04-01] LaunchedEffect for ALL data fetch, no init blocks**
   Do instead: Move all network calls from `init {}` to `LaunchedEffect(Unit) { ... }`. This ensures recomposition safety and proper state management. Exception: UI state (loading, error) can be mutable state.

4. **[2026-04-01] Zero EmulatedData in Candidato/Instrutor screens**
   Do instead: Remove ALL `copy(with: EmulatedData.candidato, ...)` from production screens. Admin screens (5 files) exempt—kept for demo purposes. Use real Supabase data via repos only.

5. **[2026-04-01] Match algorithm follows SPEC.md exactly**
   Do instead: Score = (especialidade_match * 30) + (nota_media * 20) + (pontualidade * 20) + (1 - cancelamentos_normalizado * 10) + (horas_experiencia_normalizado * 10) + (distancia_normalizado * 10). Max ~100pts. Implemented in CandidatoMatchScreen, validated post-Wave 3.

6. **[2026-04-01] CompositionLocalProvider wraps NavHost in MainActivity**
   Do instead: AppState initialized before NavHost. CompositionLocalProvider(LocalAppState provides app) { NavHost(...) }. This ensures all child composables access same AppState instance. Verified in MainActivity line 47-50.

7. **[2026-04-01] All 10 repositories initialized in AppState.init()**
   Do instead: ProfileRepository, CandidatoRepository, InstrutorRepository, AulaRepository, PagamentoRepository, AvaliacaoRepository, DisputaRepository, MensagemRepository, DadosBancariosRepository, RepasseRepository must be created in `init {}` block. Lazy initialization breaks LocalAppState pattern.

8. **[2026-04-01] NavHost bottom tab scaffolds per role (Candidato ≠ Instrutor)**
   Do instead: 2 separate NavHost blocks—one for Candidato (5 tabs), one for Instrutor (5 tabs). Each has its own bottom navigation. Switching roles requires route change to `Screen.SelectRole` first.

9. **[2026-04-01] Colors must be exported from Theme.kt**
   Do instead: All colors (Primary, Secondary, Accent, TextSecondary, etc.) exported as composable functions in `theme/Theme.kt`. Never hardcode hex values in screens. Import from Theme: `color = Primary`, `color = Secondary`, etc.

10. **[2026-04-01] GSD workflow: Waves sequential, each MUST validate before next starts**
    Do instead: Complete one wave fully (code + validation), document results in NAPKIN, then proceed. Wave 4 = Features, Wave 5 = Build, Wave 6 = APK, Wave 7 = Commit. No jumping. Validation includes: 0 lint errors, target screens pass 5+ checks, no EmulatedData refs.

---

## 🏗️ Architecture & Code Patterns

1. **[2026-04-01] SupabaseClient.kt raw OkHttp (no Retrofit)**
   Do instead: All HTTP calls use `SupabaseClient.postRequest()`, `getRequest()`, `patchRequest()` methods. No Retrofit. JSON parsing via `Json.decodeFromString<T>()`. Response status check BEFORE decoding.

2. **[2026-04-01] Models.kt extensions: getPerfil(), getPacote(), getVeiculo(), withPerfil()**
   Do instead: These are helper functions on DTOs. Use `candidato.getPerfil()` to extract nested object, `candidato.withPerfil(newPerfil)` to update. Avoids null-safety boilerplate. All defined in Models.kt lines 200+.

3. **[2026-04-01] Repositories return sealed Result<T> or nullable flow**
   Do instead: All repo methods return `suspend fun fetch(...): Result<T>` or `Flow<T?>`. Error handling is caller's responsibility. Never throw exceptions in repos.

4. **[2026-04-01] DataStore persistence for user ID + role**
   Do instead: AppState uses `DataStore<UserPreferences>` (proto). User ID persisted after login. This survives app restart. Check `userIdFlow` in AppState for current user.

---

## 🎨 UI/UX & Visual Requirements

1. **[2026-04-01] Color scheme: Azul #1E3A5F → Celeste #87CEEB degradê**
   Do instead: Primary (#1E3A5F) for headers/bold. Secondary (#4A90D9) for buttons/accents. Accent (#87CEEB) for lighter elements. Never use hardcoded hex—use Theme.kt exports (Primary, Secondary, Accent, TextSecondary, etc.).

2. **[2026-04-01] Client feedback Wave 4: 6 features in priority order**
   Do instead: (1) BannerCarrossel (slider, todas pages), (2) Avatar redondo (foto user), (3) Footer banners (gasolina + seguro), (4) Geolocalização picker, (5) Info resumida aluno, (6) Gravação aulas. Started post-validation 01/04. See FEEDBACK-CLIENTE-2026-04-01.md for full spec.

---

## 📱 Screen Template & Common Patterns

1. **[2026-04-01] Standard screen structure: Scaffold + LaunchedEffect + mutable states**
   Do instead: 
   ```kotlin
   @Composable
   fun MyScreen(onNavigate: (String) -> Unit) {
       val app = LocalAppState.current
       var state by remember { mutableStateOf<State>(State.Loading) }
       
       LaunchedEffect(Unit) {
           when (val result = app.someRepo.fetch()) {
               is Result.Success -> state = State.Data(result.data)
               is Result.Error -> state = State.Error(result.message)
           }
       }
       
       Scaffold(...) { ... }
   }
   ```

---

## 🚨 Known Gotchas & Blockers

1. **[2026-04-01] SUPABASE_ANON_KEY in MainActivity is PLACEHOLDER**
   Do instead: Before ANY build, replace `s0_placeholder_XXXXXXXX...` with real value from Supabase project settings → API → Anon Key. Obtain from https://supabase.com/dashboard → project ibyngfqddoefatqtojfj → Settings → API keys. This is BLOCKING build.

2. **[2026-04-01] Admin screens still use EmulatedData (intentional, deprioritized)**
   Do instead: 5 admin files (AdminHomeScreen, AdminInstrutoresScreen, AdminCandidatosScreen, AdminAulasScreen, AdminDisputasScreen) kept as-is with mocks. Replace when Wave 8 starts. Not blocking MVP.

3. **[2026-04-01] Video recording requires Camera + storage permissions**
   Do instead: Add to AndroidManifest.xml: `<uses-permission android:name="android.permission.CAMERA" />` + `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`. Runtime permission check in app (targetSdk 35 requires). Use `rememberLauncherForActivityResult` for permission flow.

---

**Last updated:** 2026-04-01 — Post Wave 3 validation, pre Wave 4 start
**Author:** DevSan
**Version:** 0.5.0-napkin
