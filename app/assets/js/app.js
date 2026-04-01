// ============================================
// APP.JS - Controle Principal
// ============================================

const app = {
  
  currentProfile: null,
  
  // Inicialização
  init() {
    console.log('🚀 CNH+ Demo PWA Inicializado');
    this.setupDrawer();
    this.checkServiceWorker();
  },
  
  // Login (selecionar perfil)
  login(profile) {
    this.currentProfile = profile;
    
    // Esconde seletor de perfil
    document.getElementById('profile-selector').classList.remove('active');
    
    // Mostra app do perfil
    const appScreen = document.getElementById(`app-${profile}`);
    appScreen.classList.add('active');
    
    // Inicializa módulo específico
    switch(profile) {
      case 'candidato':
        candidato.init();
        break;
      case 'instrutor':
        instrutor.init();
        break;
      case 'admin':
        admin.init();
        break;
    }

    // Iniciar notificações contextualizadas
    if (typeof globalFeatures !== 'undefined') {
      globalFeatures.startNotificationsForProfile(profile);
    }

    // Registra no console
    console.log(`✅ Logado como: ${profile}`);
  },
  
  // Logout
  logout() {
    // Esconde app atual
    if (this.currentProfile) {
      document.getElementById(`app-${this.currentProfile}`).classList.remove('active');
    }
    
    // Mostra seletor de perfil
    document.getElementById('profile-selector').classList.add('active');
    
    // Fecha drawer se estiver aberto
    this.closeDrawer();
    
    // Limpa perfil atual
    this.currentProfile = null;
    
    console.log('👋 Logout realizado');
  },
  
  // Notificações (placeholder)
  showNotifications() {
    alert('🔔 Notificações\n\nEm breve você terá acesso às suas notificações aqui!');
  },
  
  // Setup do Drawer (instrutor)
  setupDrawer() {
    const drawerToggle = document.getElementById('instrutor-drawer-toggle');
    const drawer = document.getElementById('instrutor-drawer');
    const overlay = document.getElementById('drawer-overlay');
    
    if (!drawerToggle) return;
    
    // Toggle drawer
    drawerToggle.addEventListener('click', (e) => {
      e.stopPropagation();
      this.toggleDrawer();
    });
    
    // Fechar ao clicar no overlay
    overlay.addEventListener('click', () => {
      this.closeDrawer();
    });
    
    // Fechar ao clicar em item (mobile)
    const drawerItems = drawer.querySelectorAll('.drawer-item');
    drawerItems.forEach(item => {
      item.addEventListener('click', () => {
        if (window.innerWidth < 768) {
          this.closeDrawer();
        }
      });
    });
  },
  
  toggleDrawer() {
    const drawer = document.getElementById('instrutor-drawer');
    const overlay = document.getElementById('drawer-overlay');
    
    drawer.classList.toggle('open');
    overlay.classList.toggle('visible');
  },
  
  closeDrawer() {
    const drawer = document.getElementById('instrutor-drawer');
    const overlay = document.getElementById('drawer-overlay');
    
    drawer.classList.remove('open');
    overlay.classList.remove('visible');
  },
  
  // Service Worker
  checkServiceWorker() {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.ready.then(registration => {
        console.log('✅ Service Worker ativo');
      });
    }
  },
  
  // Helpers
  setTitle(text) {
    const titleElement = document.querySelector('.app-bar h1');
    if (titleElement) {
      titleElement.textContent = text;
    }
  }
  
};

// Inicializar quando DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
  app.init();
});

// Lidar com back button do navegador
window.addEventListener('popstate', (e) => {
  if (app.currentProfile) {
    app.logout();
  }
});
