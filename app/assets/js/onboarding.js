// ============================================
// ONBOARDING.JS - Fluxo Completo de Entrada
// ============================================
// Fluxo: Splash → Onboarding → Seleção Perfil → Login → App

const onboarding = {

  currentStep: 0,
  selectedProfile: null,

  // Slides do onboarding
  slides: [
    {
      icon: 'school',
      title: 'Encontre seu Instrutor',
      description: 'Match inteligente com os melhores instrutores da sua região'
    },
    {
      icon: 'event',
      title: 'Agende suas Aulas',
      description: 'Flexibilidade total de horários e localização'
    },
    {
      icon: 'workspace_premium',
      title: 'Aprenda com Segurança',
      description: 'Instrutores verificados e avaliados pela comunidade'
    },
    {
      icon: 'celebration',
      title: 'Conquiste sua CNH',
      description: '92% de aprovação na primeira tentativa'
    }
  ],

  // Perfis disponíveis
  profiles: [
    {
      id: 'candidato',
      icon: 'school',
      label: 'Sou Candidato',
      description: 'Quero aprender a dirigir',
      color: '#4CAF50',
      demoName: 'João Silva'
    },
    {
      id: 'instrutor',
      icon: 'drive_eta',
      label: 'Sou Instrutor',
      description: 'Quero dar aulas de direção',
      color: '#FF9800',
      demoName: 'Maria Santos'
    },
    {
      id: 'admin',
      icon: 'admin_panel_settings',
      label: 'Sou Administrador',
      description: 'Gerenciar a plataforma',
      color: '#E91E63',
      demoName: 'Backoffice'
    }
  ],

  // ==================== SPLASH ====================

  showSplash() {
    const html = `
      <div class="splash-screen">
        <div class="splash-content">
          <div class="splash-logo">
            <div class="logo-circle">
              <span class="material-symbols-rounded">drive_eta</span>
            </div>
            <h1 class="splash-title">CNH+</h1>
            <p class="splash-tagline">Sua CNH mais fácil</p>
          </div>
          <div class="splash-loader">
            <div class="loader-bar"></div>
          </div>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = html;
    document.body.appendChild(container.firstElementChild);

    // Remover splash após 2s → ir para onboarding
    setTimeout(() => {
      const splash = document.querySelector('.splash-screen');
      if (!splash) return;
      splash.classList.add('fade-out');
      setTimeout(() => {
        splash.remove();
        // Verificar se já passou pelo onboarding
        if (localStorage.getItem('cnh-onboarding-seen')) {
          this.showProfileSelector();
        } else {
          this.showOnboarding();
        }
      }, 300);
    }, 2000);
  },

  // ==================== ONBOARDING (4 slides) ====================

  showOnboarding() {
    this.currentStep = 0;

    const html = `
      <div class="onboarding-screen">
        <div class="onboarding-container">
          <div class="slides-container">
            ${this.slides.map((slide, i) => `
              <div class="slide ${i === 0 ? 'active' : ''}" data-slide="${i}">
                <span class="material-symbols-rounded slide-icon">${slide.icon}</span>
                <h2>${slide.title}</h2>
                <p>${slide.description}</p>
              </div>
            `).join('')}
          </div>

          <div class="slide-indicators">
            ${this.slides.map((_, i) => `
              <span class="indicator ${i === 0 ? 'active' : ''}" data-slide="${i}"></span>
            `).join('')}
          </div>

          <div class="onboarding-actions">
            <button class="btn-text" onclick="onboarding.skipOnboarding()">Pular</button>
            <button class="btn-primary" id="onboarding-next-btn" onclick="onboarding.nextSlide()">
              <span id="onboarding-btn-text">Próximo</span>
              <span class="material-symbols-rounded">arrow_forward</span>
            </button>
          </div>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = html;
    document.body.appendChild(container.firstElementChild);
  },

  nextSlide() {
    this.currentStep++;

    if (this.currentStep >= this.slides.length) {
      this.finishOnboarding();
      return;
    }

    // Atualizar slides
    document.querySelectorAll('.slide').forEach((slide, i) => {
      slide.classList.toggle('active', i === this.currentStep);
    });

    // Atualizar indicadores
    document.querySelectorAll('.indicator').forEach((ind, i) => {
      ind.classList.toggle('active', i === this.currentStep);
    });

    // Último slide: mudar texto do botão
    if (this.currentStep === this.slides.length - 1) {
      const btnText = document.getElementById('onboarding-btn-text');
      if (btnText) btnText.textContent = 'Começar';
    }
  },

  skipOnboarding() {
    this.finishOnboarding();
  },

  finishOnboarding() {
    localStorage.setItem('cnh-onboarding-seen', 'true');
    const screen = document.querySelector('.onboarding-screen');
    if (screen) {
      screen.classList.add('fade-out');
      setTimeout(() => {
        screen.remove();
        this.showProfileSelector();
      }, 300);
    } else {
      this.showProfileSelector();
    }
  },

  // ==================== SELEÇÃO DE PERFIL ====================

  showProfileSelector() {
    const html = `
      <div class="profile-select-screen" id="profile-select-screen">
        <div class="profile-select-container">
          <div class="profile-select-header">
            <div class="ps-logo">CNH+</div>
            <h1>Qual seu perfil?</h1>
            <p>Escolha como deseja acessar</p>
          </div>

          <div class="profile-options">
            ${this.profiles.map((p, i) => `
              <button class="profile-option" data-profile="${p.id}"
                onclick="onboarding.selectProfile('${p.id}')"
                style="--profile-color: ${p.color}; animation: profileSlideIn 0.4s ${0.15 * i}s both;">
                <div class="po-icon" style="background: ${p.color}15; color: ${p.color};">
                  <span class="material-symbols-rounded">${p.icon}</span>
                </div>
                <div class="po-text">
                  <span class="po-label">${p.label}</span>
                  <span class="po-desc">${p.description}</span>
                </div>
                <span class="material-symbols-rounded po-arrow">chevron_right</span>
              </button>
            `).join('')}
          </div>

          <!-- Acesso rápido demo -->
          <div class="quick-access-section">
            <div class="qa-divider">
              <span>Acesso rápido (demo)</span>
            </div>
            <div class="quick-access-buttons">
              ${this.profiles.map(p => `
                <button class="qa-btn" style="--profile-color: ${p.color};"
                  onclick="onboarding.quickLogin('${p.id}')">
                  <span class="material-symbols-rounded">${p.icon}</span>
                  <span class="qa-name">${p.demoName}</span>
                </button>
              `).join('')}
            </div>
          </div>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = html;
    document.body.appendChild(container.firstElementChild);
  },

  selectProfile(profile) {
    this.selectedProfile = profile;

    // Feedback visual
    const btn = document.querySelector(`.profile-option[data-profile="${profile}"]`);
    if (btn) {
      btn.style.transform = 'scale(0.97)';
      btn.style.opacity = '0.7';
    }

    setTimeout(() => {
      // Remover tela de seleção
      const screen = document.getElementById('profile-select-screen');
      if (screen) {
        screen.classList.add('fade-out');
        setTimeout(() => {
          screen.remove();
          this.showAuth(profile);
        }, 300);
      } else {
        this.showAuth(profile);
      }
    }, 300);
  },

  // ==================== TELA DE LOGIN / CADASTRO ====================

  showAuth(profile) {
    const profileData = this.profiles.find(p => p.id === profile);
    const isAdmin = profile === 'admin';

    const html = `
      <div class="auth-screen" id="auth-screen">
        <div class="auth-container">
          <button class="auth-back-btn" onclick="onboarding.backToProfileSelect()">
            <span class="material-symbols-rounded">arrow_back</span>
          </button>

          <div class="auth-header">
            <div class="auth-profile-icon" style="background: ${profileData.color}15; color: ${profileData.color};">
              <span class="material-symbols-rounded">${profileData.icon}</span>
            </div>
            <h1>${isAdmin ? 'Área Administrativa' : profileData.label.replace('Sou ', '')}</h1>
            <p class="auth-subtitle">${isAdmin ? 'Acesse o painel de controle' : profileData.description}</p>
          </div>

          ${!isAdmin ? `
          <div class="auth-tabs">
            <button class="auth-tab active" data-tab="login" onclick="onboarding.switchAuthTab('login')">
              Entrar
            </button>
            <button class="auth-tab" data-tab="signup" onclick="onboarding.switchAuthTab('signup')">
              Cadastrar
            </button>
          </div>
          ` : ''}

          <!-- Form de Login -->
          <form class="auth-form active" id="login-form" onsubmit="onboarding.handleLogin(event, '${profile}')">
            <div class="form-group">
              <label>Email</label>
              <input type="email" placeholder="seu@email.com" required>
            </div>
            <div class="form-group">
              <label>Senha</label>
              <input type="password" placeholder="••••••••" required>
            </div>
            <button type="submit" class="btn-primary btn-block">
              Entrar
              <span class="material-symbols-rounded">arrow_forward</span>
            </button>
            ${!isAdmin ? `
            <button type="button" class="btn-text btn-block" onclick="onboarding.forgotPassword()">
              Esqueci minha senha
            </button>
            ` : ''}
          </form>

          ${!isAdmin ? `
          <!-- Form de Cadastro -->
          <form class="auth-form" id="signup-form" onsubmit="onboarding.handleSignup(event, '${profile}')">
            <div class="form-group">
              <label>Nome completo</label>
              <input type="text" placeholder="Seu nome" required>
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" placeholder="seu@email.com" required>
            </div>
            <div class="form-group">
              <label>Celular</label>
              <input type="tel" placeholder="(00) 00000-0000" required>
            </div>
            <div class="form-group">
              <label>Senha</label>
              <input type="password" placeholder="Mínimo 6 caracteres" required>
            </div>
            <button type="submit" class="btn-primary btn-block">
              Criar conta
              <span class="material-symbols-rounded">arrow_forward</span>
            </button>
          </form>

          <div class="auth-divider">
            <span>ou continue com</span>
          </div>

          <button class="btn-google" onclick="onboarding.showGoogleConfirm('${profile}')">
            <img src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 48 48'%3E%3Cpath fill='%234285F4' d='M45.12 24.5c0-1.56-.14-3.06-.4-4.5H24v8.51h11.84c-.51 2.75-2.06 5.08-4.39 6.64v5.52h7.11c4.16-3.83 6.56-9.47 6.56-16.17z'/%3E%3Cpath fill='%2334A853' d='M24 46c5.94 0 10.92-1.97 14.56-5.33l-7.11-5.52c-1.97 1.32-4.49 2.1-7.45 2.1-5.73 0-10.58-3.87-12.31-9.07H4.34v5.7C7.96 41.07 15.4 46 24 46z'/%3E%3Cpath fill='%23FBBC05' d='M11.69 28.18C11.25 26.86 11 25.45 11 24s.25-2.86.69-4.18v-5.7H4.34C2.85 17.09 2 20.45 2 24c0 3.55.85 6.91 2.34 9.88l7.35-5.7z'/%3E%3Cpath fill='%23EA4335' d='M24 10.75c3.23 0 6.13 1.11 8.41 3.29l6.31-6.31C34.91 4.18 29.93 2 24 2 15.4 2 7.96 6.93 4.34 14.12l7.35 5.7c1.73-5.2 6.58-9.07 12.31-9.07z'/%3E%3C/svg%3E" alt="G">
            Continuar com Google
          </button>
          ` : ''}

          <p class="auth-footer">
            Ao continuar, você aceita nossos
            <a href="#termos">Termos de Uso</a> e
            <a href="#privacidade">Política de Privacidade</a>
          </p>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = html;
    document.body.appendChild(container.firstElementChild);
  },

  backToProfileSelect() {
    const authScreen = document.getElementById('auth-screen');
    if (authScreen) {
      authScreen.classList.add('fade-out');
      setTimeout(() => {
        authScreen.remove();
        this.showProfileSelector();
      }, 300);
    }
  },

  switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(t => {
      t.classList.toggle('active', t.dataset.tab === tab);
    });
    document.querySelectorAll('.auth-form').forEach(f => {
      f.classList.toggle('active', f.id === `${tab}-form`);
    });
  },

  // ==================== LOGIN NORMAL ====================

  handleLogin(e, profile) {
    e.preventDefault();

    const btn = e.target.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.innerHTML = '<span class="loader-btn"></span> Entrando...';

    setTimeout(() => {
      this.enterApp(profile);
    }, 1200);
  },

  handleSignup(e, profile) {
    e.preventDefault();

    const btn = e.target.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.innerHTML = '<span class="loader-btn"></span> Criando conta...';

    setTimeout(() => {
      this.enterApp(profile);
    }, 1200);
  },

  // ==================== GOOGLE LOGIN SIMULADO ====================

  showGoogleConfirm(profile) {
    const profileData = this.profiles.find(p => p.id === profile);

    const html = `
      <div class="google-confirm-overlay" id="google-confirm-overlay">
        <div class="google-confirm-modal">
          <div class="gc-header">
            <svg width="24" height="24" viewBox="0 0 48 48">
              <path fill="#4285F4" d="M45.12 24.5c0-1.56-.14-3.06-.4-4.5H24v8.51h11.84c-.51 2.75-2.06 5.08-4.39 6.64v5.52h7.11c4.16-3.83 6.56-9.47 6.56-16.17z"/>
              <path fill="#34A853" d="M24 46c5.94 0 10.92-1.97 14.56-5.33l-7.11-5.52c-1.97 1.32-4.49 2.1-7.45 2.1-5.73 0-10.58-3.87-12.31-9.07H4.34v5.7C7.96 41.07 15.4 46 24 46z"/>
              <path fill="#FBBC05" d="M11.69 28.18C11.25 26.86 11 25.45 11 24s.25-2.86.69-4.18v-5.7H4.34C2.85 17.09 2 20.45 2 24c0 3.55.85 6.91 2.34 9.88l7.35-5.7z"/>
              <path fill="#EA4335" d="M24 10.75c3.23 0 6.13 1.11 8.41 3.29l6.31-6.31C34.91 4.18 29.93 2 24 2 15.4 2 7.96 6.93 4.34 14.12l7.35 5.7c1.73-5.2 6.58-9.07 12.31-9.07z"/>
            </svg>
            <span>Escolha uma conta</span>
          </div>

          <div class="gc-account" onclick="onboarding.confirmGoogleLogin('${profile}')">
            <div class="gc-avatar" style="background: ${profileData.color};">
              ${profileData.demoName.charAt(0)}
            </div>
            <div class="gc-info">
              <span class="gc-name">${profileData.demoName}</span>
              <span class="gc-email">${profileData.id}@cnh-mais.demo</span>
            </div>
            <span class="material-symbols-rounded gc-arrow">arrow_forward</span>
          </div>

          <div class="gc-footer">
            <button class="gc-cancel" onclick="onboarding.cancelGoogleLogin()">
              Cancelar
            </button>
            <span class="gc-terms">
              Para continuar, o Google compartilhará seu nome, email e foto de perfil com este app.
            </span>
          </div>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = html;
    document.body.appendChild(container.firstElementChild);

    // Animar entrada
    requestAnimationFrame(() => {
      const overlay = document.getElementById('google-confirm-overlay');
      if (overlay) overlay.classList.add('active');
    });
  },

  confirmGoogleLogin(profile) {
    const overlay = document.getElementById('google-confirm-overlay');
    if (overlay) {
      overlay.classList.add('confirming');
      const account = overlay.querySelector('.gc-account');
      if (account) {
        account.style.background = '#e8f5e9';
        const arrow = account.querySelector('.gc-arrow');
        if (arrow) {
          arrow.textContent = 'check_circle';
          arrow.style.color = '#4CAF50';
        }
      }

      setTimeout(() => {
        overlay.classList.remove('active');
        setTimeout(() => {
          overlay.remove();
          this.enterApp(profile);
        }, 300);
      }, 800);
    }
  },

  cancelGoogleLogin() {
    const overlay = document.getElementById('google-confirm-overlay');
    if (overlay) {
      overlay.classList.remove('active');
      setTimeout(() => overlay.remove(), 300);
    }
  },

  // ==================== LOGIN RÁPIDO (DEMO) ====================

  quickLogin(profile) {
    // Feedback visual no botão clicado
    const btn = document.querySelector(`.qa-btn[onclick*="${profile}"]`);
    if (btn) {
      btn.classList.add('loading');
      btn.style.transform = 'scale(0.95)';
    }

    setTimeout(() => {
      this.enterApp(profile);
    }, 400);
  },

  // ==================== ENTRAR NO APP ====================

  enterApp(profile) {
    // Remover TODAS as telas de onboarding/auth
    document.querySelectorAll('.splash-screen, .onboarding-screen, .auth-screen, .profile-select-screen, .google-confirm-overlay').forEach(el => el.remove());

    // Garantir que profile-selector está escondido
    const ps = document.getElementById('profile-selector');
    if (ps) ps.classList.remove('active');

    // Login via app.js
    app.login(profile);
  },

  // ==================== HELPERS ====================

  forgotPassword() {
    this.showToast('Link de recuperação enviado para seu email', 'info');
  },

  showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
      <span class="material-symbols-rounded">
        ${type === 'success' ? 'check_circle' : type === 'error' ? 'error' : 'info'}
      </span>
      <span>${message}</span>
    `;

    document.body.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
      toast.classList.remove('show');
      setTimeout(() => toast.remove(), 300);
    }, 3000);
  }

};

// Auto-iniciar splash ao carregar a página
window.addEventListener('load', () => {
  if (!sessionStorage.getItem('cnh-splash-shown')) {
    sessionStorage.setItem('cnh-splash-shown', 'true');
    onboarding.showSplash();
  } else {
    // Já passou pelo splash nesta sessão → ir direto para seleção de perfil
    if (localStorage.getItem('cnh-onboarding-seen')) {
      onboarding.showProfileSelector();
    } else {
      onboarding.showOnboarding();
    }
  }
});
