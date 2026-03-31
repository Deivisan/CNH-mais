// ============================================
// THEMES.JS - Sistema de Temas
// ============================================

const themes = {
  
  current: 'default',
  
  // Lista de temas disponíveis
  available: [
    { id: 'default', name: 'CNH+ Azul', emoji: '🔵' },
    { id: 'dark', name: 'Modo Escuro', emoji: '🌙' },
    { id: 'nature', name: 'Natureza', emoji: '🌿' },
    { id: 'premium', name: 'Premium', emoji: '💜' },
    { id: 'sunset', name: 'Pôr do Sol', emoji: '🌅' },
    { id: 'rose', name: 'Rosa', emoji: '🌸' },
    { id: 'ocean', name: 'Oceano', emoji: '🌊' }
  ],
  
  // Inicializar sistema de temas
  init() {
    console.log('🎨 Sistema de Temas Inicializado');
    
    // Carregar tema salvo
    const saved = localStorage.getItem('cnh-theme');
    if (saved && this.available.find(t => t.id === saved)) {
      this.apply(saved, false);
    }
    
    // Criar FAB e painel
    this.createFAB();
    this.createPanel();
  },
  
  // Criar botão flutuante
  createFAB() {
    const fab = document.createElement('button');
    fab.className = 'theme-fab';
    fab.innerHTML = '<span class="material-symbols-rounded">palette</span>';
    fab.onclick = () => this.togglePanel();
    document.body.appendChild(fab);
  },
  
  // Criar painel de seleção
  createPanel() {
    const panel = document.createElement('div');
    panel.className = 'theme-panel';
    panel.id = 'theme-panel';
    
    panel.innerHTML = `
      <h3>Escolher Tema</h3>
      <div class="theme-grid">
        ${this.available.map(theme => `
          <button 
            class="theme-option ${theme.id === this.current ? 'active' : ''}"
            data-theme="${theme.id}"
            onclick="themes.apply('${theme.id}')"
            title="${theme.name}"
          ></button>
        `).join('')}
      </div>
    `;
    
    document.body.appendChild(panel);
  },
  
  // Alternar visibilidade do painel
  togglePanel() {
    const panel = document.getElementById('theme-panel');
    panel.classList.toggle('active');
  },
  
  // Aplicar tema
  apply(themeId, animate = true) {
    if (!this.available.find(t => t.id === themeId)) return;
    
    // Animação de transição
    if (animate) {
      document.body.classList.add('theme-transitioning');
      setTimeout(() => {
        document.body.classList.remove('theme-transitioning');
      }, 300);
    }
    
    // Aplicar tema
    if (themeId === 'default') {
      document.documentElement.removeAttribute('data-theme');
    } else {
      document.documentElement.setAttribute('data-theme', themeId);
    }
    
    // Atualizar meta theme-color
    const theme = this.available.find(t => t.id === themeId);
    const colors = {
      'default': '#1E3A5F',
      'dark': '#121212',
      'nature': '#2E7D32',
      'premium': '#6A1B9A',
      'sunset': '#E65100',
      'rose': '#C2185B',
      'ocean': '#006064'
    };
    
    document.querySelector('meta[name="theme-color"]').setAttribute('content', colors[themeId]);
    
    // Atualizar estado
    this.current = themeId;
    localStorage.setItem('cnh-theme', themeId);
    
    // Atualizar UI
    document.querySelectorAll('.theme-option').forEach(opt => {
      opt.classList.toggle('active', opt.dataset.theme === themeId);
    });
    
    // Fechar painel
    setTimeout(() => {
      const panel = document.getElementById('theme-panel');
      if (panel) panel.classList.remove('active');
    }, 200);
    
    console.log(`🎨 Tema aplicado: ${themeId}`);
  }
  
};

// Inicializar quando DOM estiver pronto
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => themes.init());
} else {
  themes.init();
}
