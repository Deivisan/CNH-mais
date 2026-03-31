// ============================================
// CANDIDATO-EXTRA.JS - Telas Extras do Candidato
// ============================================

// Extender o módulo candidato com telas adicionais
Object.assign(candidato, {
  
  // ==================== CHAT ====================
  renderChat() {
    const instrutor = mockData.instrutor;
    const messages = [
      { sender: 'instrutor', text: 'Boa tarde, João! Tudo certo para nossa aula amanhã?', time: '14:30' },
      { sender: 'candidato', text: 'Boa tarde! Sim, tudo certo. Que horas mesmo?', time: '14:32' },
      { sender: 'instrutor', text: 'Às 15h, no mesmo local de sempre 😊', time: '14:33' },
      { sender: 'candidato', text: 'Perfeito! Até amanhã então!', time: '14:35' },
      { sender: 'instrutor', text: 'Até! Vamos trabalhar baliza amanhã.', time: '14:36' }
    ];
    
    return `
      <div class="chat-container">
        <div class="chat-header">
          <div class="avatar">
            <img src="${instrutor.foto}" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22%3E%3Crect fill=%22%234A90D9%22 width=%22100%22 height=%22100%22/%3E%3Ctext x=%2250%22 y=%2250%22 text-anchor=%22middle%22 dy=%22.3em%22 fill=%22white%22 font-size=%2240%22%3EMS%3C/text%3E%3C/svg%3E'" alt="${instrutor.nome}">
          </div>
          <div>
            <h3>${instrutor.nome}</h3>
            <p class="status-online">
              <span class="status-dot"></span> Online
            </p>
          </div>
        </div>
        
        <div class="chat-messages">
          ${messages.map(msg => `
            <div class="message ${msg.sender}">
              <div class="message-bubble">
                <p>${msg.text}</p>
                <span class="message-time">${msg.time}</span>
              </div>
            </div>
          `).join('')}
        </div>
        
        <div class="chat-input-container">
          <input type="text" class="chat-input" placeholder="Digite sua mensagem..." onkeypress="if(event.key==='Enter') candidato.sendMessage(this)">
          <button class="btn-icon" onclick="candidato.sendMessage(document.querySelector('.chat-input'))">
            <span class="material-symbols-rounded">send</span>
          </button>
        </div>
      </div>
    `;
  },
  
  sendMessage(input) {
    if (!input.value.trim()) return;
    
    const messagesContainer = document.querySelector('.chat-messages');
    const now = new Date();
    const time = `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}`;
    
    const messageHtml = `
      <div class="message candidato">
        <div class="message-bubble">
          <p>${input.value}</p>
          <span class="message-time">${time}</span>
        </div>
      </div>
    `;
    
    messagesContainer.insertAdjacentHTML('beforeend', messageHtml);
    input.value = '';
    
    // Scroll para o fim
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
    
    // Simular resposta do instrutor
    setTimeout(() => {
      const now2 = new Date();
      const time2 = `${now2.getHours()}:${now2.getMinutes().toString().padStart(2, '0')}`;
      
      messagesContainer.insertAdjacentHTML('beforeend', `
        <div class="message instrutor">
          <div class="message-bubble">
            <p>Recebi! 👍</p>
            <span class="message-time">${time2}</span>
          </div>
        </div>
      `);
      messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }, 1000);
  },
  
  // ==================== NOTIFICAÇÕES ====================
  renderNotificacoes() {
    const notifications = [
      { 
        id: 1, 
        type: 'aula', 
        icon: 'event', 
        title: 'Aula confirmada', 
        text: 'Aula de amanhã (15h) confirmada', 
        time: '2h atrás',
        unread: true
      },
      { 
        id: 2, 
        type: 'pagamento', 
        icon: 'payments', 
        title: 'Pagamento aprovado', 
        text: 'Pacote de 12 aulas - R$ 1.920,00', 
        time: 'Ontem',
        unread: true
      },
      { 
        id: 3, 
        type: 'instrutor', 
        icon: 'person', 
        title: 'Nova mensagem', 
        text: 'Maria Santos: "Até amanhã!"', 
        time: 'Ontem',
        unread: false
      },
      { 
        id: 4, 
        type: 'sistema', 
        icon: 'info', 
        title: 'Dica do dia', 
        text: 'Pratique baliza em casa com cones', 
        time: '2 dias atrás',
        unread: false
      },
      { 
        id: 5, 
        type: 'avaliacao', 
        icon: 'star', 
        title: 'Avalie sua última aula', 
        text: 'Como foi sua aula de 28/03?', 
        time: '3 dias atrás',
        unread: false
      }
    ];
    
    return `
      <div class="notifications-header">
        <h2>Notificações</h2>
        <button class="btn-text" onclick="candidato.markAllRead()">
          Marcar todas como lidas
        </button>
      </div>
      
      <div class="notifications-list">
        ${notifications.map(notif => `
          <div class="notification-item ${notif.unread ? 'unread' : ''}" data-id="${notif.id}">
            <div class="notification-icon ${notif.type}">
              <span class="material-symbols-rounded">${notif.icon}</span>
            </div>
            <div class="notification-content">
              <h3>${notif.title}</h3>
              <p>${notif.text}</p>
              <span class="notification-time">${notif.time}</span>
            </div>
            ${notif.unread ? '<div class="unread-dot"></div>' : ''}
          </div>
        `).join('')}
      </div>
    `;
  },
  
  markAllRead() {
    document.querySelectorAll('.notification-item.unread').forEach(item => {
      item.classList.remove('unread');
      const dot = item.querySelector('.unread-dot');
      if (dot) dot.remove();
    });
    
    onboarding.showToast('Notificações marcadas como lidas', 'success');
  },
  
  // ==================== AVALIAÇÕES ====================
  renderAvaliacoes() {
    const avaliacoes = [
      { 
        data: '28/03/2026', 
        nota: 5, 
        comentario: 'Excelente aula! Maria é muito paciente e explica super bem. Consegui fazer a baliza pela primeira vez! 🎉',
        resposta: 'Muito obrigada, João! Foi um prazer ver seu progresso. Continue assim!'
      },
      { 
        data: '26/03/2026', 
        nota: 5, 
        comentario: 'Ótima aula, focamos em rotatórias e melhorei bastante.',
        resposta: 'Parabéns pelo empenho! Você está evoluindo muito rápido.'
      },
      { 
        data: '24/03/2026', 
        nota: 4, 
        comentario: 'Boa aula, mas fiquei um pouco ansioso no trânsito.',
        resposta: 'Normal ficar ansioso no início. Vamos trabalhar isso juntos!'
      }
    ];
    
    return `
      <div class="avaliacoes-container">
        <div class="card" style="background: linear-gradient(135deg, var(--primary), var(--secondary)); color: white;">
          <div style="text-align: center;">
            <div style="font-size: 48px; font-weight: 700; margin-bottom: 8px;">4.7</div>
            <div style="font-size: 32px; margin-bottom: 8px;">⭐⭐⭐⭐⭐</div>
            <p style="opacity: 0.9;">Sua média de avaliações</p>
          </div>
        </div>
        
        <div class="card">
          <div class="card-title">Suas Avaliações</div>
          <p style="color: var(--text-secondary); margin-bottom: 16px;">
            Você avaliou ${avaliacoes.length} de ${this.data.pacote.aulasRealizadas} aulas
          </p>
          
          ${avaliacoes.map(av => `
            <div class="avaliacao-item">
              <div class="avaliacao-header">
                <span class="avaliacao-data">${av.data}</span>
                <span class="avaliacao-nota">${helpers.renderStars(av.nota)}</span>
              </div>
              <div class="avaliacao-comentario">
                <p><strong>Você:</strong> ${av.comentario}</p>
              </div>
              ${av.resposta ? `
                <div class="avaliacao-resposta">
                  <p><strong>Instrutor:</strong> ${av.resposta}</p>
                </div>
              ` : ''}
            </div>
          `).join('')}
        </div>
        
        <div class="card">
          <div class="card-title">Aulas Pendentes de Avaliação</div>
          <p style="color: var(--text-secondary); margin-bottom: 16px;">
            ${this.data.pacote.aulasRealizadas - avaliacoes.length} aulas aguardando avaliação
          </p>
          <button class="btn-primary" onclick="candidato.avaliarPendentes()">
            Avaliar Aulas Pendentes
          </button>
        </div>
      </div>
    `;
  },
  
  avaliarPendentes() {
    onboarding.showToast('Abrindo formulário de avaliação...', 'info');
  },
  
  // ==================== PAGAMENTOS ====================
  renderPagamentos() {
    const pagamentos = [
      { 
        id: 1, 
        data: '01/03/2026', 
        descricao: 'Pacote 12 aulas', 
        valor: 1920.00, 
        status: 'aprovado',
        metodo: 'PIX'
      },
      { 
        id: 2, 
        data: '15/02/2026', 
        descricao: 'Aula avulsa', 
        valor: 160.00, 
        status: 'aprovado',
        metodo: 'Cartão'
      }
    ];
    
    const total = pagamentos.reduce((sum, p) => sum + p.valor, 0);
    
    return `
      <div class="card" style="background: linear-gradient(135deg, #2E7D32, #66BB6A); color: white;">
        <div style="text-align: center;">
          <p style="opacity: 0.9; margin-bottom: 8px;">Total Investido</p>
          <div style="font-size: 36px; font-weight: 700;">${helpers.formatCurrency(total)}</div>
          <p style="opacity: 0.9; margin-top: 8px;">em ${pagamentos.length} transações</p>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Histórico de Pagamentos</div>
        
        <div class="pagamentos-list">
          ${pagamentos.map(pag => `
            <div class="pagamento-item">
              <div class="pagamento-icon">
                <span class="material-symbols-rounded">
                  ${pag.metodo === 'PIX' ? 'qr_code_2' : 'credit_card'}
                </span>
              </div>
              <div class="pagamento-info">
                <h4>${pag.descricao}</h4>
                <p>${pag.data} • ${pag.metodo}</p>
              </div>
              <div class="pagamento-valor">
                <span class="valor">${helpers.formatCurrency(pag.valor)}</span>
                <span class="badge badge-success">Aprovado</span>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Comprar Mais Aulas</div>
        <p style="color: var(--text-secondary); margin-bottom: 16px;">
          Pacotes com desconto progressivo
        </p>
        
        <div class="pacotes-grid">
          <div class="pacote-card">
            <div class="pacote-destaque">POPULAR</div>
            <h3>6 Aulas</h3>
            <div class="pacote-preco">
              <span class="preco-desconto">R$ 960,00</span>
              <span class="preco-original">R$ 990,00</span>
            </div>
            <p class="pacote-economia">Economize R$ 30</p>
            <button class="btn-secondary" onclick="candidato.comprarPacote(6)">Comprar</button>
          </div>
          
          <div class="pacote-card">
            <div class="pacote-destaque melhor">MELHOR CUSTO</div>
            <h3>12 Aulas</h3>
            <div class="pacote-preco">
              <span class="preco-desconto">R$ 1.800,00</span>
              <span class="preco-original">R$ 1.980,00</span>
            </div>
            <p class="pacote-economia">Economize R$ 180</p>
            <button class="btn-primary" onclick="candidato.comprarPacote(12)">Comprar</button>
          </div>
        </div>
      </div>
    `;
  },
  
  comprarPacote(aulas) {
    onboarding.showToast(`Abrindo checkout para pacote de ${aulas} aulas...`, 'info');
  },
  
  // ==================== MAPA DE LOCALIZAÇÃO ====================
  renderMapa() {
    // Limpar mapa anterior se existir
    if (maps.map) maps.destroy();
    
    // Container do mapa deve ser renderizado primeiro
    setTimeout(() => {
      maps.init('map-container', {
        showCandidato: true,
        showInstrutor: true
      });
      
      // Simular movimento do instrutor após 2s
      setTimeout(() => {
        maps.simulateInstrutorMovement();
      }, 2000);
    }, 100);
    
    const distance = maps.calculateDistance();
    
    return `
      <div class="mapa-container">
        <div class="card" style="margin-top: 0;">
          <div class="mapa-info">
            <div class="mapa-stat">
              <span class="material-symbols-rounded">near_me</span>
              <div>
                <div class="stat-value">${distance.km} km</div>
                <div class="stat-label">Distância</div>
              </div>
            </div>
            <div class="mapa-stat">
              <span class="material-symbols-rounded">schedule</span>
              <div>
                <div class="stat-value">~5 min</div>
                <div class="stat-label">Tempo estimado</div>
              </div>
            </div>
          </div>
        </div>
        
        <div id="map-container" class="map-wrapper"></div>
        
        <div class="card">
          <div class="card-title">Localização Atual</div>
          <div class="location-info">
            <div class="location-item">
              <span class="material-symbols-rounded" style="color: #4A90D9;">person_pin_circle</span>
              <div>
                <strong>Você</strong>
                <p>Av. Maria Quitéria, 1890 - Feira de Santana</p>
              </div>
            </div>
            <div class="location-item">
              <span class="material-symbols-rounded" style="color: #2E7D32;">drive_eta</span>
              <div>
                <strong>Maria Santos</strong>
                <p>Chegando em ~5 minutos</p>
              </div>
            </div>
          </div>
          
          <button class="btn-secondary btn-block" onclick="candidato.shareLocation()">
            <span class="material-symbols-rounded">share_location</span>
            Compartilhar Localização
          </button>
        </div>
      </div>
    `;
  },
  
  shareLocation() {
    onboarding.showToast('Localização compartilhada com sucesso!', 'success');
  },
  
  // ==================== INDICAÇÕES (Nova tela) ====================
  renderIndicacoes() {
    const indicacoes = [
      { nome: 'Pedro Silva', status: 'validada', data: '15/03/2026', bonus: true },
      { nome: 'Ana Costa', status: 'validada', data: '10/03/2026', bonus: true },
      { nome: 'Carlos Souza', status: 'pendente', data: '28/03/2026', bonus: false },
      { nome: 'Maria Lima', status: 'cadastrado', data: '29/03/2026', bonus: false }
    ];
    
    const validadas = indicacoes.filter(i => i.status === 'validada').length;
    const faltam = 10 - validadas;
    
    return `
      <div class="card" style="background: linear-gradient(135deg, #6A1B9A, #AB47BC); color: white;">
        <div style="text-align: center;">
          <div style="font-size: 48px; margin-bottom: 8px;">🎁</div>
          <h2 style="margin: 0 0 8px 0;">Programa de Indicações</h2>
          <p style="opacity: 0.9;">Ganhe 1 aula grátis a cada 10 indicações!</p>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Seu Progresso</div>
        <div class="indicacoes-progress">
          <div class="progress-circle">
            <svg viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="var(--divider)" stroke-width="8"/>
              <circle cx="50" cy="50" r="45" fill="none" stroke="var(--secondary)" stroke-width="8"
                stroke-dasharray="${validadas * 28.27} 282.7" transform="rotate(-90 50 50)"/>
            </svg>
            <div class="progress-text">
              <span class="progress-number">${validadas}</span>
              <span class="progress-label">de 10</span>
            </div>
          </div>
          <div class="indicacoes-info">
            <p>Você está a <strong>${faltam} indicações</strong> de ganhar sua primeira aula grátis!</p>
            <button class="btn-primary" onclick="candidato.compartilharCodigo()">
              <span class="material-symbols-rounded">share</span>
              Compartilhar Código
            </button>
          </div>
        </div>
        
        <div class="codigo-indicacao">
          <label>Seu código de indicação</label>
          <div class="codigo-box">
            <code>JOAO2026</code>
            <button class="btn-icon" onclick="candidato.copiarCodigo()">
              <span class="material-symbols-rounded">content_copy</span>
            </button>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Suas Indicações (${indicacoes.length})</div>
        
        <div class="indicacoes-list">
          ${indicacoes.map(ind => `
            <div class="indicacao-item">
              <div class="avatar-small">
                ${ind.nome.split(' ').map(n => n[0]).join('')}
              </div>
              <div class="indicacao-info">
                <h4>${ind.nome}</h4>
                <p>${ind.data}</p>
              </div>
              <span class="badge badge-${
                ind.status === 'validada' ? 'success' : 
                ind.status === 'pendente' ? 'warning' : 'default'
              }">
                ${ind.status === 'validada' ? '✓ Validada' : 
                  ind.status === 'pendente' ? '⏳ Pendente' : '📝 Cadastrado'}
              </span>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  },
  
  compartilharCodigo() {
    if (navigator.share) {
      navigator.share({
        title: 'CNH+ - Indicação',
        text: `Use meu código JOAO2026 e ganhe desconto no CNH+! 🚗`,
        url: 'https://cnhplus.com.br?ref=JOAO2026'
      }).catch(() => {});
    } else {
      this.copiarCodigo();
    }
  },
  
  copiarCodigo() {
    navigator.clipboard.writeText('JOAO2026');
    onboarding.showToast('Código copiado!', 'success');
  }
  
});
