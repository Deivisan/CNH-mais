// ============================================
// GLOBAL-FEATURES.JS - Funcionalidades Globais
// ============================================

const globalFeatures = {
  
  // ==================== BUSCA GLOBAL ====================
  
  initGlobalSearch() {
    // FAB de busca
    const searchFab = document.createElement('button');
    searchFab.className = 'fab';
    searchFab.id = 'search-fab';
    searchFab.style.cssText = 'bottom: 150px; background: var(--accent);';
    searchFab.innerHTML = '<span class="material-symbols-rounded">search</span>';
    searchFab.onclick = () => this.openGlobalSearch();
    
    // Só mostrar se não estiver no onboarding
    if (!window.location.hash.includes('onboarding')) {
      document.body.appendChild(searchFab);
    }
  },
  
  openGlobalSearch() {
    const searchModal = document.createElement('div');
    searchModal.id = 'global-search-modal';
    searchModal.style.cssText = `
      position: fixed;
      inset: 0;
      background: var(--background);
      z-index: 1000;
      display: flex;
      flex-direction: column;
    `;
    
    searchModal.innerHTML = `
      <div style="background: var(--primary); color: white; padding: 16px; display: flex; align-items: center; gap: 12px;">
        <button class="icon-btn" onclick="globalFeatures.closeGlobalSearch()" style="color: white;">
          <span class="material-symbols-rounded">arrow_back</span>
        </button>
        <input id="global-search-input" type="text" placeholder="Buscar em tudo..." 
               style="flex: 1; padding: 12px; border: none; border-radius: 8px; font-family: inherit; font-size: 16px;"
               oninput="globalFeatures.performSearch(this.value)">
      </div>
      
      <div id="search-results" style="flex: 1; overflow-y: auto; padding: 16px;">
        <div class="empty-state" style="padding: 64px 16px;">
          <span class="material-symbols-rounded" style="font-size: 64px; color: var(--text-secondary); opacity: 0.3; margin-bottom: 16px;">search</span>
          <h3>Busque por qualquer coisa</h3>
          <p>Instrutores, alunos, aulas, configurações...</p>
        </div>
      </div>
    `;
    
    document.body.appendChild(searchModal);
    searchModal.classList.add('fade-in');
    document.getElementById('global-search-input').focus();
  },
  
  closeGlobalSearch() {
    const modal = document.getElementById('global-search-modal');
    if (modal) modal.remove();
  },
  
  performSearch(query) {
    if (!query || query.length < 2) {
      document.getElementById('search-results').innerHTML = `
        <div class="empty-state" style="padding: 64px 16px;">
          <span class="material-symbols-rounded" style="font-size: 64px; color: var(--text-secondary); opacity: 0.3; margin-bottom: 16px;">search</span>
          <h3>Busque por qualquer coisa</h3>
          <p>Instrutores, alunos, aulas, configurações...</p>
        </div>
      `;
      return;
    }
    
    // Simular resultados de busca
    const results = [
      { tipo: 'instrutor', nome: 'Maria Santos', info: 'Feira de Santana - BA • Nota 4.8', action: 'instrutor.navigate("perfil")' },
      { tipo: 'candidato', nome: 'João Silva', info: '8 aulas realizadas • 4 restantes', action: 'candidato.navigate("home")' },
      { tipo: 'config', nome: 'Configurações de Notificações', info: 'Gerenciar alertas e lembretes', action: 'alert("Abre configurações")' },
      { tipo: 'aula', nome: 'Aula #009 - João Silva', info: '02/04/2026 às 09:00 • Agendada', action: 'alert("Detalhes da aula")' }
    ].filter(r => r.nome.toLowerCase().includes(query.toLowerCase()));
    
    if (results.length === 0) {
      document.getElementById('search-results').innerHTML = `
        <div class="empty-state" style="padding: 64px 16px;">
          <span class="material-symbols-rounded" style="font-size: 64px; color: var(--text-secondary); opacity: 0.3; margin-bottom: 16px;">search_off</span>
          <h3>Nenhum resultado encontrado</h3>
          <p>Tente buscar por outro termo</p>
        </div>
      `;
      return;
    }
    
    const tipoIcons = {
      instrutor: 'person',
      candidato: 'school',
      config: 'settings',
      aula: 'event'
    };
    
    document.getElementById('search-results').innerHTML = `
      <div class="card" style="margin: 0;">
        <div class="card-title">Resultados (${results.length})</div>
        ${results.map(result => `
          <div class="list-item" onclick="${result.action}; globalFeatures.closeGlobalSearch();" style="cursor: pointer;">
            <div class="list-item-icon">
              <span class="material-symbols-rounded" style="color: var(--secondary);">${tipoIcons[result.tipo]}</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${result.nome}</div>
              <div class="list-item-subtitle">${result.info}</div>
            </div>
            <span class="material-symbols-rounded" style="color: var(--text-secondary);">chevron_right</span>
          </div>
        `).join('')}
      </div>
    `;
  },
  
  // ==================== NOTIFICAÇÕES PUSH ====================
  
  initPushNotifications() {
    // ⛔ NÃO disparar notificações antes do login
    // As notificações serão iniciadas por app.login() após login
  },

  // Chamado por app.login() após login bem-sucedido
  startNotificationsForProfile(profile) {
    // Limpar timers anteriores
    if (this._notifTimers) {
      this._notifTimers.forEach(t => clearTimeout(t));
    }
    this._notifTimers = [];

    if (profile === 'instrutor') {
      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'instrutor') {
          this.showPushNotification('Nova aula agendada', 'João Silva agendou aula para 02/04 às 09:00', 'event');
        }
      }, 8000));

      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'instrutor') {
          this.showPushNotification('Pagamento recebido', 'Repasse de R$ 420,00 foi processado', 'payments');
        }
      }, 18000));

    } else if (profile === 'candidato') {
      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'candidato') {
          this.showPushNotification('Aula amanhã', 'Sua aula com Maria Santos é amanhã às 14:00', 'event');
        }
      }, 8000));

      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'candidato') {
          this.showPushNotification('Lembrete', 'Você tem 3 aulas restantes no seu pacote', 'info');
        }
      }, 18000));

    } else if (profile === 'admin') {
      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'admin') {
          this.showPushNotification('Nova disputa', 'Candidato abriu disputa sobre aula #4521', 'gavel');
        }
      }, 8000));

      this._notifTimers.push(setTimeout(() => {
        if (app.currentProfile === 'admin') {
          this.showPushNotification('Instrutor pendente', 'Novo instrutor aguardando aprovação', 'person_add');
        }
      }, 18000));
    }
  },
  
  showPushNotification(titulo, mensagem, icon = 'notifications') {
    const notification = document.createElement('div');
    notification.className = 'push-notification';
    notification.style.cssText = `
      position: fixed;
      top: 80px;
      left: 16px;
      right: 16px;
      background: white;
      border-radius: 12px;
      padding: 16px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
      z-index: 9999;
      display: flex;
      gap: 12px;
      animation: slideInDown 0.3s ease;
      cursor: pointer;
    `;
    
    notification.innerHTML = `
      <div style="width: 40px; height: 40px; border-radius: 50%; background: var(--secondary)20; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
        <span class="material-symbols-rounded" style="color: var(--secondary); font-size: 20px;">${icon}</span>
      </div>
      <div style="flex: 1;">
        <div style="font-weight: 700; margin-bottom: 4px;">${titulo}</div>
        <div style="font-size: 14px; color: var(--text-secondary);">${mensagem}</div>
      </div>
      <button class="icon-btn" onclick="this.parentElement.remove()" style="flex-shrink: 0;">
        <span class="material-symbols-rounded">close</span>
      </button>
    `;
    
    notification.onclick = () => {
      notification.remove();
      // Aqui poderia navegar para a tela relacionada
    };
    
    document.body.appendChild(notification);
    
    // Auto-remover após 5 segundos
    setTimeout(() => {
      if (notification.parentElement) {
        notification.style.animation = 'slideInUp 0.3s ease reverse';
        setTimeout(() => notification.remove(), 300);
      }
    }, 5000);
  },
  
  // ==================== FILTROS AVANÇADOS ====================
  
  openFilters(contexto) {
    const filterModal = document.createElement('div');
    filterModal.id = 'filter-modal';
    filterModal.style.cssText = `
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.5);
      z-index: 1000;
      display: flex;
      align-items: flex-end;
      animation: overlayFadeIn 0.3s ease;
    `;
    
    filterModal.innerHTML = `
      <div style="background: var(--background); border-radius: 16px 16px 0 0; width: 100%; max-height: 80vh; overflow-y: auto; animation: bottomSheetSlideUp 0.3s ease;">
        <div style="padding: 16px; border-bottom: 1px solid var(--divider); display: flex; justify-content: space-between; align-items: center;">
          <h3 style="margin: 0;">Filtros</h3>
          <button class="icon-btn" onclick="globalFeatures.closeFilters()">
            <span class="material-symbols-rounded">close</span>
          </button>
        </div>
        
        <div style="padding: 16px;">
          <!-- Filtros por contexto -->
          ${this.renderFilterContent(contexto)}
          
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-top: 24px;">
            <button class="btn btn-outlined" onclick="globalFeatures.resetFilters()">
              Limpar
            </button>
            <button class="btn btn-primary" onclick="globalFeatures.applyFilters()">
              Aplicar
            </button>
          </div>
        </div>
      </div>
    `;
    
    filterModal.onclick = (e) => {
      if (e.target === filterModal) this.closeFilters();
    };
    
    document.body.appendChild(filterModal);
  },
  
  renderFilterContent(contexto) {
    // Filtros específicos por contexto
    const filters = {
      instrutores: `
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Cidade</label>
          <select style="width: 100%; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
            <option>Todas</option>
            <option>Feira de Santana</option>
            <option>Salvador</option>
            <option>Vitória da Conquista</option>
          </select>
        </div>
        
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Nota Mínima</label>
          <input type="range" min="0" max="5" step="0.5" value="4" 
                 style="width: 100%;"
                 oninput="this.nextElementSibling.textContent = this.value + ' ⭐'">
          <div style="text-align: center; margin-top: 8px; font-weight: 500;">4.0 ⭐</div>
        </div>
        
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Status</label>
          <div style="display: flex; gap: 8px; flex-wrap: wrap;">
            <label style="padding: 8px 16px; background: var(--secondary); color: white; border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" checked style="display: none;">
              Ativos
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Pendentes
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Suspensos
            </label>
          </div>
        </div>
      `,
      candidatos: `
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Status</label>
          <div style="display: flex; gap: 8px; flex-wrap: wrap;">
            <label style="padding: 8px 16px; background: var(--secondary); color: white; border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" checked style="display: none;">
              Ativos
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Aguardando 1ª Aula
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Inativos
            </label>
          </div>
        </div>
        
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Período de Cadastro</label>
          <select style="width: 100%; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
            <option>Todos os períodos</option>
            <option>Última semana</option>
            <option>Último mês</option>
            <option>Últimos 3 meses</option>
            <option>Último ano</option>
          </select>
        </div>
      `,
      aulas: `
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Status</label>
          <div style="display: flex; gap: 8px; flex-wrap: wrap;">
            <label style="padding: 8px 16px; background: var(--secondary); color: white; border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" checked style="display: none;">
              Agendadas
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Concluídas
            </label>
            <label style="padding: 8px 16px; background: var(--background); border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" style="display: none;">
              Canceladas
            </label>
          </div>
        </div>
        
        <div style="margin-bottom: 16px;">
          <label style="display: block; font-weight: 500; margin-bottom: 8px;">Período</label>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
            <input type="date" style="padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
            <input type="date" style="padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit;">
          </div>
        </div>
      `
    };
    
    return filters[contexto] || `
      <div class="empty-state" style="padding: 32px 16px;">
        <span class="material-symbols-rounded" style="font-size: 48px; color: var(--text-secondary); opacity: 0.3; margin-bottom: 12px;">filter_alt</span>
        <p>Nenhum filtro disponível para este contexto</p>
      </div>
    `;
  },
  
  closeFilters() {
    const modal = document.getElementById('filter-modal');
    if (modal) modal.remove();
  },
  
  resetFilters() {
    alert('Filtros limpos! (funcionalidade mockada)');
    this.closeFilters();
  },
  
  applyFilters() {
    alert('Filtros aplicados! (funcionalidade mockada)');
    this.closeFilters();
  },
  
  // ==================== PULL TO REFRESH ====================
  
  initPullToRefresh() {
    let startY = 0;
    let isPulling = false;
    
    const refreshIndicator = document.createElement('div');
    refreshIndicator.id = 'refresh-indicator';
    refreshIndicator.style.cssText = `
      position: fixed;
      top: 0;
      left: 50%;
      transform: translateX(-50%) translateY(-100px);
      padding: 16px;
      background: var(--surface);
      border-radius: 50%;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      z-index: 999;
      transition: transform 0.3s ease;
    `;
    refreshIndicator.innerHTML = '<span class="material-symbols-rounded spin" style="color: var(--secondary);">refresh</span>';
    document.body.appendChild(refreshIndicator);
    
    document.addEventListener('touchstart', (e) => {
      if (window.scrollY === 0) {
        startY = e.touches[0].pageY;
        isPulling = true;
      }
    });
    
    document.addEventListener('touchmove', (e) => {
      if (!isPulling) return;
      
      const currentY = e.touches[0].pageY;
      const diff = currentY - startY;
      
      if (diff > 0 && diff < 150) {
        refreshIndicator.style.transform = `translateX(-50%) translateY(${diff - 100}px)`;
      }
    });
    
    document.addEventListener('touchend', (e) => {
      if (!isPulling) return;
      
      const endY = e.changedTouches[0].pageY;
      const diff = endY - startY;
      
      if (diff > 100) {
        // Trigger refresh
        refreshIndicator.style.transform = 'translateX(-50%) translateY(16px)';
        
        setTimeout(() => {
          refreshIndicator.style.transform = 'translateX(-50%) translateY(-100px)';
          
          // Simular reload
          const currentProfile = app.currentProfile;
          if (currentProfile === 'candidato' && candidato.currentScreen) {
            candidato.navigate(candidato.currentScreen);
          } else if (currentProfile === 'instrutor' && instrutor.currentScreen) {
            instrutor.navigate(instrutor.currentScreen);
          } else if (currentProfile === 'admin' && admin.currentTab) {
            admin.navigate(admin.currentTab);
          }
        }, 1000);
      } else {
        refreshIndicator.style.transform = 'translateX(-50%) translateY(-100px)';
      }
      
      isPulling = false;
    });
  },
  
  // ==================== ATALHOS DE TECLADO ====================
  
  initKeyboardShortcuts() {
    document.addEventListener('keydown', (e) => {
      // Ctrl+K para busca
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        this.openGlobalSearch();
      }
      
      // ESC para fechar modais
      if (e.key === 'Escape') {
        this.closeGlobalSearch();
        this.closeFilters();
      }
    });
  },
  
  // ==================== INICIALIZAÇÃO ====================
  
  init() {
    this.initGlobalSearch();
    this.initPushNotifications();
    this.initPullToRefresh();
    this.initKeyboardShortcuts();
    
    console.log('✅ Global Features inicializadas');
  }
};

// Inicializar quando DOM carregar
document.addEventListener('DOMContentLoaded', () => {
  globalFeatures.init();
});
