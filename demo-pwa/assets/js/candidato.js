// ============================================
// CANDIDATO.JS - Módulo do Candidato
// ============================================

const candidato = {
  
  currentScreen: 'home',
  data: null,
  
  // Inicialização
  init() {
    this.data = mockData.candidato;
    this.navigate('home');
  },
  
  // Navegação entre telas
  navigate(screen) {
    this.currentScreen = screen;
    
    // Atualiza título
    const titles = {
      home: 'Início',
      aulas: 'Minhas Aulas',
      instrutor: 'Meu Instrutor',
      perfil: 'Meu Perfil'
    };
    app.setTitle(titles[screen] || 'CNH+');
    
    // Atualiza navegação ativa
    document.querySelectorAll('#app-candidato .nav-item').forEach(item => {
      item.classList.remove('active');
      if (item.dataset.screen === screen) {
        item.classList.add('active');
      }
    });
    
    // Renderiza conteúdo
    this.render(screen);
  },
  
  // Renderizar conteúdo
  render(screen) {
    const content = document.getElementById('candidato-content');
    
    switch(screen) {
      case 'home':
        content.innerHTML = this.renderHome();
        break;
      case 'aulas':
        content.innerHTML = this.renderAulas();
        break;
      case 'instrutor':
        content.innerHTML = this.renderInstrutor();
        break;
      case 'perfil':
        content.innerHTML = this.renderPerfil();
        break;
    }
    
    content.classList.add('fade-in');
  },
  
  // ==================== TELAS ====================
  
  renderHome() {
    const progressPercent = helpers.getProgressPercent(
      this.data.pacote.aulasRealizadas,
      this.data.pacote.aulasCompradas
    );
    
    const instrutor = mockData.instrutor;
    
    return `
      <!-- Card de Boas-vindas -->
      <div class="card" style="background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%); color: white; margin-top: 0;">
        <h2 style="margin-bottom: 8px;">Olá, ${this.data.nome.split(' ')[0]}! 👋</h2>
        <p style="opacity: 0.9;">Continue sua jornada rumo à CNH</p>
      </div>
      
      <!-- Progresso do Pacote -->
      <div class="card">
        <div class="card-title">Seu Progresso</div>
        <div class="progress-container">
          <div class="progress-bar">
            <div class="progress-fill" style="width: ${progressPercent}%"></div>
          </div>
          <div class="progress-label">
            <span>${this.data.pacote.aulasRealizadas} de ${this.data.pacote.aulasCompradas} aulas</span>
            <span>${progressPercent}%</span>
          </div>
        </div>
        <div style="margin-top: 16px; display: flex; gap: 12px;">
          <div style="flex: 1; text-align: center;">
            <div class="stat-value" style="font-size: 24px;">${this.data.pacote.aulasRestantes}</div>
            <div class="stat-label">Restantes</div>
          </div>
          <div style="flex: 1; text-align: center;">
            <div class="stat-value" style="font-size: 24px;">${this.data.pacote.aulasRecomendadas}</div>
            <div class="stat-label">Recomendadas</div>
          </div>
        </div>
      </div>
      
      <!-- Próximas Aulas -->
      <div class="card">
        <div class="card-title">Próximas Aulas</div>
        ${this.data.proximasAulas.map(aula => `
          <div class="list-item">
            <div class="list-item-icon">
              <span class="material-symbols-rounded" style="color: var(--secondary);">event</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${aula.tipo}</div>
              <div class="list-item-subtitle">${helpers.formatDate(aula.data)} às ${aula.hora}</div>
            </div>
            <div class="list-item-action">
              <span class="badge badge-success">Agendada</span>
            </div>
          </div>
        `).join('')}
      </div>
      
      <!-- Seu Instrutor -->
      <div class="card">
        <div class="card-title">Seu Instrutor</div>
        <div class="list-item" onclick="candidato.navigate('instrutor')" style="cursor: pointer;">
          <img src="${instrutor.foto}" alt="${instrutor.nome}" class="list-item-avatar" 
               onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2240%22 height=%2240%22%3E%3Crect fill=%22%234A90D9%22 width=%2240%22 height=%2240%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2218%22 fill=%22white%22%3EMS%3C/text%3E%3C/svg%3E'">
          <div class="list-item-content">
            <div class="list-item-title">${instrutor.nome} ${instrutor.verificado ? '✓' : ''}</div>
            <div class="list-item-subtitle">${helpers.getStars(instrutor.notaMedia)} (${instrutor.avaliacoes} avaliações)</div>
          </div>
          <div class="list-item-action">
            <span class="material-symbols-rounded" style="color: var(--text-secondary);">chevron_right</span>
          </div>
        </div>
      </div>
      
      <!-- Programa de Indicações -->
      <div class="card">
        <div class="card-title">🎁 Ganhe Aulas Grátis</div>
        <p class="card-subtitle">Indique amigos e ganhe 1 aula grátis a cada 10 indicações válidas</p>
        <div class="progress-container">
          <div class="progress-bar">
            <div class="progress-fill" style="width: ${(this.data.indicacoes.validadas / 10) * 100}%"></div>
          </div>
          <div class="progress-label">
            <span>${this.data.indicacoes.validadas} indicações</span>
            <span>Faltam ${this.data.indicacoes.proxima}</span>
          </div>
        </div>
        <div style="margin-top: 16px; padding: 12px; background: var(--background); border-radius: 8px; text-align: center;">
          <div style="font-size: 12px; color: var(--text-secondary); margin-bottom: 4px;">Seu código</div>
          <div style="font-size: 20px; font-weight: 700; color: var(--secondary); letter-spacing: 2px;">${this.data.indicacoes.codigo}</div>
        </div>
        <button class="btn btn-outlined" style="width: 100%; margin-top: 12px;">
          Compartilhar Código
        </button>
      </div>
    `;
  },
  
  renderAulas() {
    return `
      <!-- Resumo -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.pacote.aulasRealizadas}</div>
          <div class="stat-label">Realizadas</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.pacote.aulasRestantes}</div>
          <div class="stat-label">Restantes</div>
        </div>
      </div>
      
      <!-- Próximas Aulas -->
      <div class="card">
        <div class="card-title">Agendadas</div>
        ${this.data.proximasAulas.map(aula => `
          <div class="list-item">
            <div class="list-item-icon">
              <span class="material-symbols-rounded" style="color: var(--secondary);">event</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${aula.tipo}</div>
              <div class="list-item-subtitle">${helpers.formatDate(aula.data)} às ${aula.hora} (${aula.duracao} min)</div>
            </div>
            <div class="list-item-action">
              <button class="btn btn-text" style="padding: 8px 16px; font-size: 12px;">Detalhes</button>
            </div>
          </div>
        `).join('')}
      </div>
      
      <!-- Histórico (simulado) -->
      <div class="card">
        <div class="card-title">Concluídas</div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--success);">check_circle</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Prática - Controle do carro</div>
            <div class="list-item-subtitle">25/03/2026 às 09:00</div>
          </div>
          <div class="list-item-action">
            <span class="badge badge-success">Confirmada</span>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--success);">check_circle</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Prática - Trânsito urbano</div>
            <div class="list-item-subtitle">22/03/2026 às 14:00</div>
          </div>
          <div class="list-item-action">
            <span class="badge badge-success">Confirmada</span>
          </div>
        </div>
      </div>
    `;
  },
  
  renderInstrutor() {
    const instrutor = mockData.instrutor;
    
    return `
      <!-- Perfil do Instrutor -->
      <div class="card" style="margin-top: 0;">
        <div style="text-align: center; margin-bottom: 16px;">
          <img src="${instrutor.foto}" alt="${instrutor.nome}" 
               style="width: 96px; height: 96px; border-radius: 50%; object-fit: cover; margin-bottom: 12px; border: 4px solid var(--accent);"
               onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2296%22 height=%2296%22%3E%3Crect fill=%22%234A90D9%22 width=%2296%22 height=%2296%22 rx=%2248%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2236%22 fill=%22white%22%3EMS%3C/text%3E%3C/svg%3E'">
          <h2 style="margin-bottom: 4px;">${instrutor.nome} ${instrutor.verificado ? '✓' : ''}</h2>
          <div style="color: var(--text-secondary); margin-bottom: 8px;">${instrutor.cidade}</div>
          <div style="font-size: 18px; margin-bottom: 4px;">${helpers.getStars(instrutor.notaMedia)}</div>
          <div style="color: var(--text-secondary); font-size: 14px;">${instrutor.avaliacoes} avaliações</div>
        </div>
        
        <!-- Stats -->
        <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-top: 16px; padding-top: 16px; border-top: 1px solid var(--divider);">
          <div style="text-align: center;">
            <div style="font-size: 20px; font-weight: 700; color: var(--secondary);">${instrutor.alunosAtendidos}</div>
            <div style="font-size: 11px; color: var(--text-secondary);">ALUNOS</div>
          </div>
          <div style="text-align: center;">
            <div style="font-size: 20px; font-weight: 700; color: var(--secondary);">${instrutor.horasTrabalhadas}</div>
            <div style="font-size: 11px; color: var(--text-secondary);">HORAS</div>
          </div>
          <div style="text-align: center;">
            <div style="font-size: 20px; font-weight: 700; color: var(--success);">${instrutor.pontualidade}%</div>
            <div style="font-size: 11px; color: var(--text-secondary);">PONTUAL</div>
          </div>
        </div>
      </div>
      
      <!-- Biografia -->
      <div class="card">
        <div class="card-title">Sobre</div>
        <p style="color: var(--text-secondary); line-height: 1.6;">${instrutor.biografia}</p>
      </div>
      
      <!-- Especialidades -->
      <div class="card">
        <div class="card-title">Especialidades</div>
        <div style="display: flex; flex-wrap: wrap; gap: 8px;">
          ${instrutor.especialidades.map(e => `
            <span style="padding: 6px 12px; background: rgba(74, 144, 217, 0.1); color: var(--secondary); border-radius: 16px; font-size: 13px; font-weight: 500;">
              ${e}
            </span>
          `).join('')}
        </div>
      </div>
      
      <!-- Veículo -->
      <div class="card">
        <div class="card-title">Veículo</div>
        <div class="list-item" style="padding: 0;">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--secondary);">directions_car</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">${instrutor.veiculo.modelo}</div>
            <div class="list-item-subtitle">Ano ${instrutor.veiculo.ano} • ${instrutor.veiculo.temPedal ? 'Com pedal duplo' : 'Sem pedal'}</div>
          </div>
        </div>
      </div>
      
      <!-- Avaliações Recentes -->
      <div class="card">
        <div class="card-title">Avaliações Recentes</div>
        ${instrutor.avaliacoesRecentes.slice(0, 3).map(av => `
          <div style="margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px solid var(--divider);">
            <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
              <span style="font-weight: 500;">${av.candidato}</span>
              <span>${helpers.getStars(av.nota)}</span>
            </div>
            <p style="font-size: 14px; color: var(--text-secondary); margin-bottom: 4px;">${av.comentario}</p>
            <div style="font-size: 12px; color: var(--text-secondary);">${helpers.formatDate(av.data)}</div>
          </div>
        `).join('')}
      </div>
      
      <!-- Contato -->
      <div class="card">
        <button class="btn btn-primary" style="width: 100%; display: flex; align-items: center; justify-content: center; gap: 8px;">
          <span class="material-symbols-rounded">chat</span>
          <span>Enviar Mensagem</span>
        </button>
      </div>
    `;
  },
  
  renderPerfil() {
    return `
      <!-- Dados Pessoais -->
      <div class="card" style="margin-top: 0;">
        <div style="text-align: center; margin-bottom: 16px;">
          <img src="${this.data.foto}" alt="${this.data.nome}" 
               style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover; margin-bottom: 12px;"
               onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2280%22 height=%2280%22%3E%3Crect fill=%22%234A90D9%22 width=%2280%22 height=%2280%22 rx=%2240%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2230%22 fill=%22white%22%3EJS%3C/text%3E%3C/svg%3E'">
          <h2>${this.data.nome}</h2>
          <p style="color: var(--text-secondary);">${this.data.email}</p>
        </div>
      </div>
      
      <!-- Informações -->
      <div class="card">
        <div class="card-title">Informações</div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Celular</span>
          <span style="font-weight: 500;">${this.data.celular}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Cidade</span>
          <span style="font-weight: 500;">${this.data.cidade}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">RENACH</span>
          <span style="font-weight: 500;">${this.data.renach}</span>
        </div>
      </div>
      
      <!-- Pacote -->
      <div class="card">
        <div class="card-title">Meu Pacote</div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Aulas Compradas</span>
          <span style="font-weight: 500;">${this.data.pacote.aulasCompradas}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Valor Total</span>
          <span style="font-weight: 500;">${this.data.pacote.valorTotal}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Valor Restante</span>
          <span style="font-weight: 500; color: var(--success);">${this.data.pacote.valorRestante}</span>
        </div>
      </div>
      
      <!-- Ações -->
      <div class="card">
        <button class="btn btn-outlined" style="width: 100%; margin-bottom: 12px;">
          Editar Perfil
        </button>
        <button class="btn btn-text" style="width: 100%; color: var(--error);">
          Sair da Conta
        </button>
      </div>
    `;
  }
  
};

// Extender navegação com novas telas
const originalRender = candidato.render;
candidato.render = function(screen) {
  const content = document.getElementById('candidato-content');
  
  // Telas extras
  if (screen === 'chat') {
    content.innerHTML = this.renderChat();
  } else if (screen === 'notificacoes') {
    content.innerHTML = this.renderNotificacoes();
  } else if (screen === 'avaliacoes') {
    content.innerHTML = this.renderAvaliacoes();
  } else if (screen === 'pagamentos') {
    content.innerHTML = this.renderPagamentos();
  } else if (screen === 'mapa') {
    content.innerHTML = this.renderMapa();
  } else if (screen === 'indicacoes') {
    content.innerHTML = this.renderIndicacoes();
  } else {
    // Telas originais
    originalRender.call(this, screen);
    return;
  }
  
  content.classList.add('fade-in');
  
  // Fechar menu "Mais" se estiver aberto
  const moreMenu = document.getElementById('more-menu');
  if (moreMenu) moreMenu.classList.remove('active');
  
  // Atualizar bottom nav
  document.querySelectorAll('.bottom-nav-expanded .nav-item').forEach(item => {
    item.classList.remove('active');
    if (item.dataset.screen === screen) {
      item.classList.add('active');
    }
  });
};

// Toggle menu "Mais"
candidato.toggleMoreMenu = function() {
  const moreMenu = document.getElementById('more-menu');
  if (!moreMenu) {
    // Criar menu se não existir
    const html = `
      <div class="more-menu" id="more-menu">
        <div class="more-menu-content">
          <h3>Mais Opções</h3>
          <button class="more-menu-item" onclick="candidato.navigate('instrutor')">
            <span class="material-symbols-rounded">person</span>
            <span>Meu Instrutor</span>
          </button>
          <button class="more-menu-item" onclick="candidato.navigate('avaliacoes')">
            <span class="material-symbols-rounded">star</span>
            <span>Avaliações</span>
          </button>
          <button class="more-menu-item" onclick="candidato.navigate('pagamentos')">
            <span class="material-symbols-rounded">payments</span>
            <span>Pagamentos</span>
          </button>
          <button class="more-menu-item" onclick="candidato.navigate('indicacoes')">
            <span class="material-symbols-rounded">card_giftcard</span>
            <span>Indicações</span>
          </button>
          <button class="more-menu-item" onclick="candidato.navigate('notificacoes')">
            <span class="material-symbols-rounded">notifications</span>
            <span>Notificações</span>
          </button>
          <button class="more-menu-item" onclick="candidato.navigate('perfil')">
            <span class="material-symbols-rounded">account_circle</span>
            <span>Meu Perfil</span>
          </button>
          <button class="more-menu-close" onclick="candidato.toggleMoreMenu()">Fechar</button>
        </div>
      </div>
    `;
    document.body.insertAdjacentHTML('beforeend', html);
    
    // Click fora fecha
    setTimeout(() => {
      document.getElementById('more-menu').addEventListener('click', (e) => {
        if (e.target.id === 'more-menu') {
          candidato.toggleMoreMenu();
        }
      });
    }, 100);
  }
  
  document.getElementById('more-menu').classList.toggle('active');
};
