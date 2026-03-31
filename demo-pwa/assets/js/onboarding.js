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
            <span>ou continuar com</span>
          </div>
          
          <div class="auth-social">
            <button class="btn-social" onclick="onboarding.loginGoogle()">
              <img src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 48 48'%3E%3Cpath fill='%234285F4' d='M45.12 24.5c0-1.56-.14-3.06-.4-4.5H24v8.51h11.84c-.51 2.75-2.06 5.08-4.39 6.64v5.52h7.11c4.16-3.83 6.56-9.47 6.56-16.17z'/%3E%3Cpath fill='%2334A853' d='M24 46c5.94 0 10.92-1.97 14.56-5.33l-7.11-5.52c-1.97 1.32-4.49 2.1-7.45 2.1-5.73 0-10.58-3.87-12.31-9.07H4.34v5.7C7.96 41.07 15.4 46 24 46z'/%3E%3Cpath fill='%23FBBC05' d='M11.69 28.18C11.25 26.86 11 25.45 11 24s.25-2.86.69-4.18v-5.7H4.34C2.85 17.09 2 20.45 2 24c0 3.55.85 6.91 2.34 9.88l7.35-5.7z'/%3E%3Cpath fill='%23EA4335' d='M24 10.75c3.23 0 6.13 1.11 8.41 3.29l6.31-6.31C34.91 4.18 29.93 2 24 2 15.4 2 7.96 6.93 4.34 14.12l7.35 5.7c1.73-5.2 6.58-9.07 12.31-9.07z'/%3E%3C/svg%3E" alt="Google">
              Google
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
