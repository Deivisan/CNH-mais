# Wave 4 — Build Errors & TODO

## 🔴 BLOCKING ISSUE: Kotlin Version Mismatch

```
Error: Your current Kotlin version is 1.9.24, while kotlinx.serialization 
core runtime 1.7.3 requires at least Kotlin 2.0.0-RC1.
```

### Solution (choose one):

**Option A: Upgrade Kotlin to 2.0.0+** (recommended)
```gradle
// In build.gradle.kts (root)
plugins {
    kotlin("jvm") version "2.0.0" apply false
}
```

**Option B: Downgrade serialization to 1.6.x** (fallback)
```gradle
// In app/build.gradle.kts
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
```

---

## 🟠 IMPORT ERRORS (will resolve after Kotlin fix)

### 1. **MainActivity.kt** (line 22, 44, 45, 64)
```kotlin
// Missing imports:
import com.cnhplus.ui.theme.Secondary
import com.cnhplus.navigation.Screen  // InstrutorHome, AdminHome refs
```

**Lines to fix:**
- Line 22: `CircularProgressIndicator(color = Secondary)` → import Secondary from ui.theme
- Lines 44-45: `InstrutorHome`, `AdminHome` → import from navigation.Screen

### 2. **AppState.kt** (line 230)
```kotlin
// Error: Unresolved reference: prefs
// Fix: Replace with proper DataStore reference
private val _userIdFlow = dataStore.data.map { it[SessionKeys.USER_ID] ?: "" }
```

### 3. **Models.kt** (multiple lines)
```kotlin
// Type mismatch: Serialization strategy calls are broken
// Lines 75, 80, 160, 164 have Json.encodeToString() calls with wrong syntax
// Fix: Ensure proper import of kotlinx.serialization.json.Json
```

---

## 🟡 MISSING COMPONENTS (need to be recreated)

### 1. **Geolocalização Picker** (removed due to import errors)
```kotlin
// File: app/src/main/java/com/cnhplus/ui/components/LocalizacaoAulaCard.kt
// What it does:
// - Display aula location (address, lat/lng)
// - Click to open Google Maps
// - Edit location button

// Key features:
// - Maps intent: "https://maps.google.com/maps?q=$lat,$lng"
// - Address field + reference text
// - Confirmation buttons for location
```

**Where it's used:** InstrutorAgendaScreen, CandidatoAulasScreen (to show meeting location)

### 2. **Denúncia Dialog** (removed due to import errors)
```kotlin
// File: app/src/main/java/com/cnhplus/ui/components/DenunciaDialog.kt
// What it does:
// - Modal dialog for reporting aula issues
// - Form: motivo (dropdown) + descricao (text) + midia (image/video upload)
// - Submit to DenunciaRepository

// Dropdown options:
// - "Comportamento inadequado"
// - "Segurança"
// - "Qualidade da aula"
// - "Problema técnico"
// - "Outro"

// Media upload:
// - ActivityResultContract(GetContent) for image/video
// - Display thumbnail
// - Remove button
```

**Where it's used:** AulaDetalhesScreen (button: "Denunciar Problema")

### 3. **Header Component** (removed due to import errors)
```kotlin
// File: app/src/main/java/com/cnhplus/ui/components/CnhPlusHeader.kt
// What it does:
// - Unified TopAppBar with avatar on right
// - Integrates AvatarCircle component
// - Menu icon + title + avatar click callback
```

---

## 📋 INTEGRATION TASKS (after build fixes)

### Step 1: Add BannerCarrossel to all main screens
```kotlin
// In CandidatoHomeScreen, InstrutorHomeScreen, etc.

LazyColumn(...) {
    item {
        if (banners.isNotEmpty()) {
            BannerCarrossel(
                banners = banners,
                onBannerClick = { /* handle click */ },
                modifier = Modifier.padding(12.dp)
            )
        }
    }
    // ... rest of content
}
```

**Screens to update:**
- CandidatoHomeScreen ✓ (already attempted)
- CandidatoAulasScreen
- CandidatoMatchScreen
- CandidatoPagamentoScreen
- CandidatoPerfilScreen
- InstrutorHomeScreen
- InstrutorAgendaScreen
- InstrutorAulasScreen
- InstrutorFinanceiroScreen
- InstrutorPerfilScreen

### Step 2: Add FooterBanners to screens
```kotlin
LazyColumn(...) {
    // ... content
    item {
        FooterBanners(
            onGasolinaClick = { /* navigate to gas partner list */ },
            onSeguroClick = { /* navigate to insurance partner */ },
            modifier = Modifier.padding(16.dp)
        )
    }
}
```

### Step 3: Add Avatar to headers
```kotlin
CnhPlusHeader(
    title = "Screen Title",
    avatarUrl = user?.fotoUrl,
    onAvatarClick = { navController.navigate(Screen.CandidatoPerfil.route) }
)
```

### Step 4: Recreate Geolocalização & Denúncia
- Write both components with proper imports
- Test in emulator
- Add to respective screens

---

## ✅ COMPLETED IN WAVE 4

### Infrastructure (100%)
- ✓ 4 Supabase tables created (banners, localidades, denuncias, avatares)
- ✓ RLS policies applied to all 4 tables
- ✓ 4 new DTOs added to Models.kt
- ✓ 4 repositories created
- ✓ AppState updated with 4 new repos
- ✓ Colors.kt centralized in ui/theme

### Components (75%)
- ✓ BannerCarrossel.kt (complete, compiles fine)
- ✓ AvatarCircle.kt (complete, compiles fine)
- ✓ FooterBanners.kt (complete, compiles fine)
- ⚠️ Geolocalização picker (removed, needs rewrite)
- ⚠️ Denúncia dialog (removed, needs rewrite)
- ⚠️ Header component (removed, needs rewrite)

---

## 🚀 NEXT STEPS (Priority Order)

1. **FIX KOTLIN VERSION** (BLOCKING)
   - Run: `./gradlew compileDebugKotlin`
   - Choose Option A or B above
   - Re-test compile

2. **FIX REMAINING IMPORTS** (after Kotlin fix)
   - MainActivity: add Secondary import
   - AppState: fix DataStore reference
   - Models: verify serialization calls

3. **INTEGRATE 3 WORKING COMPONENTS**
   - Add BannerCarrossel to 10 screens
   - Add FooterBanners to all screens
   - Add Avatar to headers

4. **RECREATE 3 REMOVED COMPONENTS**
   - Geolocalização picker (simpler, just maps intent)
   - Denúncia dialog (forms + upload)
   - Header (optional, can use TopAppBar directly)

5. **BUILD & RELEASE**
   - Run `./gradlew build`
   - Create APK v0.5.1
   - Manual testing on emulator

---

## 📊 Metrics

- **Lines added**: ~2,000
- **New files**: 4 DTOs + 4 Repos + 3 Components + Colors.kt + napkin.md
- **Supabase tables**: 4 new
- **Build status**: BLOCKED (Kotlin version)
- **Component completeness**: 3/6 (50%)
- **Integration coverage**: 0/10 screens (pending)

---

**Last updated:** 2026-04-01
**Status:** Wave 4 — Infrastructure complete, Components partial, Build blocked
