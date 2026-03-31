// ============================================
// ONBOARDING.JS - Splash, Cadastro, Login
// ============================================

const onboarding = {
  
  currentStep: 0,
  userData: {},
  
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
  
  // Mostrar splash screen
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
    
    // Remover splash após 2s
    setTimeout(() => {
      document.querySelector('.splash-screen').classList.add('fade-out');
      setTimeout(() => {
        document.querySelector('.splash-screen').remove();
        this.showOnboarding();
      }, 300);
    }, 2000);
  },
  
  // Mostrar onboarding
  showOnboarding() {
    // Verificar se já viu onboarding
    if (localStorage.getItem('cnh-onboarding-seen')) {
      this.showAuth();
      return;
    }
    
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
            <button class="btn-text" onclick="onboarding.skip()">Pular</button>
            <button class="btn-primary" onclick="onboarding.nextSlide()">
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
  
  // Próximo slide
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
    
    // Atualizar botão no último slide
    if (this.currentStep === this.slides.length - 1) {
      document.getElementById('onboarding-btn-text').textContent = 'Começar';
    }
  },
  
  // Pular onboarding
  skip() {
    this.finishOnboarding();
  },
  
  // Finalizar onboarding
  finishOnboarding() {
    localStorage.setItem('cnh-onboarding-seen', 'true');
    document.querySelector('.onboarding-screen').classList.add('fade-out');
    setTimeout(() => {
      document.querySelector('.onboarding-screen').remove();
      this.showAuth();
    }, 300);
  },
  
  // Mostrar tela de autenticação
  showAuth() {
    const html = `
      <div class="auth-screen">
        <div class="auth-container">
          <div class="auth-header">
            <div class="app-logo">CNH+</div>
            <p class="tagline">Bem-vindo de volta!</p>
          </div>
          
          <div class="auth-tabs">
            <button class="auth-tab active" data-tab="login" onclick="onboarding.switchAuthTab('login')">
              Entrar
            </button>
            <button class="auth-tab" data-tab="signup" onclick="onboarding.switchAuthTab('signup')">
              Cadastrar
            </button>
          </div>
          
          <!-- Form de Login -->
          <form class="auth-form active" id="login-form" onsubmit="onboarding.handleLogin(event)">
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
            <button type="button" class="btn-text btn-block" onclick="onboarding.forgotPassword()">
              Esqueci minha senha
            </button>
          </form>
          
          <!-- Form de Cadastro -->
          <form class="auth-form" id="signup-form" onsubmit="onboarding.handleSignup(event)">
            <div class="form-group">
              <label>Nome completo</label>
              <input type="text" placeholder="João Silva" required>
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
            <div class="form-group">
              <label>Perfil</label>
              <select required>
                <option value="">Escolha uma opção</option>
                <option value="candidato">Candidato (quero aprender)</option>
                <option value="instrutor">Instrutor (quero ensinar)</option>
              </select>
            </div>
            <button type="submit" class="btn-primary btn-block">
              Criar conta
              <span class="material-symbols-rounded">arrow_forward</span>
            </button>
          </form>
          
          <div class="auth-divider">
            <span>ou entrar como</span>
          </div>

          <!-- Acesso Rápido Demo -->
          <div class="quick-login">
            <button class="quick-login-btn candidato" onclick="onboarding.quickLogin('candidato')">
              <span class="material-symbols-rounded">school</span>
              <span class="ql-label">Candidato</span>
              <span class="ql-name">João Silva</span>
            </button>
            <button class="quick-login-btn instrutor" onclick="onboarding.quickLogin('instrutor')">
              <span class="material-symbols-rounded">drive_eta</span>
              <span class="ql-label">Instrutor</span>
              <span class="ql-name">Maria Santos</span>
            </button>
            <button class="quick-login-btn admin" onclick="onboarding.quickLogin('admin')">
              <span class="material-symbols-rounded">admin_panel_settings</span>
              <span class="ql-label">Admin</span>
              <span class="ql-name">Backoffice</span>
            </button>
          </div>

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
  
  // Alternar entre login e cadastro
  switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(t => {
      t.classList.toggle('active', t.dataset.tab === tab);
    });
    
    document.querySelectorAll('.auth-form').forEach(f => {
      f.classList.toggle('active', f.id === `${tab}-form`);
    });
  },
  
  // Handle login
  handleLogin(e) {
    e.preventDefault();
    
    // Simular loading
    const btn = e.target.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.innerHTML = '<span class="loader-btn"></span> Entrando...';
    
    setTimeout(() => {
      // Remover tela de auth
      document.querySelector('.auth-screen').remove();
      
      // Mostrar seletor de perfil (tela original)
      document.getElementById('profile-selector').classList.add('active');
      
      // Notificação
      this.showToast('Login realizado com sucesso!', 'success');
    }, 1500);
  },
  
  // Handle signup
  handleSignup(e) {
    e.preventDefault();
    
    const btn = e.target.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.innerHTML = '<span class="loader-btn"></span> Criando conta...';
    
    setTimeout(() => {
      document.querySelector('.auth-screen').remove();
      document.getElementById('profile-selector').classList.add('active');
      this.showToast('Conta criada com sucesso!', 'success');
    }, 1500);
  },
  
  // Login com Google
  loginGoogle() {
    this.showToast('Login com Google em desenvolvimento', 'info');
  },
  
  // Esqueci senha
  forgotPassword() {
    this.showToast('Link de recuperação enviado para seu email', 'info');
  },
  
  // Login rápido (acesso direto ao perfil - demo)
  quickLogin(perfil) {
    // Feedback visual no botão clicado
    const btn = document.querySelector(`.quick-login-btn.${perfil}`);
    if (btn) {
      btn.classList.add('loading');
      // Ripple effect
      const ripple = document.createElement('span');
      ripple.className = 'ripple';
      const rect = btn.getBoundingClientRect();
      ripple.style.width = ripple.style.height = Math.max(rect.width, rect.height) + 'px';
      ripple.style.left = '0px';
      ripple.style.top = '0px';
      btn.appendChild(ripple);
      setTimeout(() => ripple.remove(), 600);
    }

    // Aguardar animação visual
    setTimeout(() => {
      // Remover telas de onboarding/auth
      const splash = document.querySelector('.splash-screen');
      const onboarding = document.querySelector('.onboarding-screen');
      const auth = document.querySelector('.auth-screen');
      if (splash) splash.remove();
      if (onboarding) onboarding.remove();
      if (auth) auth.remove();

      // Esconder seletor de perfil
      const profileSelector = document.getElementById('profile-selector');
      if (profileSelector) profileSelector.classList.remove('active');

      // Login direto via app.js
      app.login(perfil);
    }, 500);
  },

  // Toast notification
  showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
      <span class="material-symbols-rounded">
        ${type === 'success' ? 'check_circle' : 'info'}
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

// Auto-iniciar splash
window.addEventListener('load', () => {
  // Verificar se deve mostrar splash (somente primeira vez)
  if (!sessionStorage.getItem('cnh-splash-shown')) {
    sessionStorage.setItem('cnh-splash-shown', 'true');
    onboarding.showSplash();
  }
});
