// ============================================
// INSTRUTOR.JS - Módulo do Instrutor
// ============================================

const instrutor = {
  
  currentScreen: 'dashboard',
  data: null,
  
  // Inicialização
  init() {
    this.data = mockData.instrutor;
    this.navigate('dashboard');
  },
  
  // Navegação entre abas
  navigate(screen) {
    this.currentScreen = screen;
    
    // Atualiza título
    const titles = {
      dashboard: 'Dashboard',
      perfil: 'Meu Perfil',
      agenda: 'Agenda',
      midia: 'Mídia',
      resultados: 'Resultados',
      financeiro: 'Financeiro',
      bancarios: 'Dados Bancários',
      aulas: 'Aulas & Disputas',
      veiculo: 'Veículo',
      comunidade: 'Comunidade',
      suporte: 'Suporte'
    };
    app.setTitle(titles[screen] || 'CNH+');
    
    // Atualiza drawer ativo
    document.querySelectorAll('#instrutor-drawer .drawer-item').forEach(item => {
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
    const content = document.getElementById('instrutor-content');
    
    const screens = {
      dashboard: this.renderDashboard(),
      perfil: this.renderPerfil(),
      agenda: this.renderAgenda(),
      midia: this.renderMidia(),
      resultados: this.renderResultados(),
      financeiro: this.renderFinanceiro(),
      bancarios: this.renderBancarios(),
      aulas: this.renderAulas(),
      veiculo: this.renderVeiculo(),
      comunidade: this.renderComunidade(),
      suporte: this.renderSuporte()
    };
    
    content.innerHTML = screens[screen] || '';
    content.classList.add('fade-in');
  },
  
  // ==================== TELAS ====================
  
  renderDashboard() {
    const proximoRepasse = helpers.formatCurrency(this.data.saldoDisponivel);
    const saldoPendente = helpers.formatCurrency(this.data.saldoPendente);
    
    return `
      <!-- Stats Principais -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.notaMedia}</div>
          <div class="stat-label">⭐ Nota Média</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.alunosAtendidos}</div>
          <div class="stat-label">Alunos Atendidos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.horasTrabalhadas}h</div>
          <div class="stat-label">Horas Trabalhadas</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.pontualidade}%</div>
          <div class="stat-label">Pontualidade</div>
        </div>
      </div>
      
      <!-- Financeiro Resumido -->
      <div class="card">
        <div class="card-title">💰 Financeiro</div>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px;">
          <div>
            <div style="font-size: 12px; color: var(--text-secondary); margin-bottom: 4px;">Disponível</div>
            <div style="font-size: 24px; font-weight: 700; color: var(--success);">${proximoRepasse}</div>
          </div>
          <div>
            <div style="font-size: 12px; color: var(--text-secondary); margin-bottom: 4px;">Pendente</div>
            <div style="font-size: 24px; font-weight: 700; color: var(--warning);">${saldoPendente}</div>
          </div>
        </div>
        <p style="font-size: 12px; color: var(--text-secondary); margin-bottom: 12px;">
          Repasse ${this.data.frequenciaRepasse} • Próximo: Segunda-feira
        </p>
        <button class="btn btn-outlined" style="width: 100%;" onclick="instrutor.navigate('financeiro')">
          Ver Detalhes
        </button>
      </div>
      
      <!-- Próximas Aulas -->
      <div class="card">
        <div class="card-title">📅 Próximas Aulas</div>
        ${this.data.proximasAulas.slice(0, 4).map(aula => `
          <div class="list-item">
            <div class="list-item-icon">
              <span class="material-symbols-rounded" style="color: var(--secondary);">event</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${aula.candidato}</div>
              <div class="list-item-subtitle">${helpers.formatDate(aula.data)} às ${aula.hora} • ${aula.tipo === 'primeira_aula' ? 'Primeira aula' : 'Aula prática'}</div>
            </div>
          </div>
        `).join('')}
        <button class="btn btn-text" style="width: 100%; margin-top: 8px;" onclick="instrutor.navigate('agenda')">
          Ver Agenda Completa
        </button>
      </div>
      
      <!-- Atalhos Rápidos -->
      <div class="card">
        <div class="card-title">⚡ Atalhos</div>
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px;">
          <button class="btn btn-outlined" onclick="instrutor.navigate('perfil')">
            <span class="material-symbols-rounded" style="font-size: 20px;">person</span>
            <span>Editar Perfil</span>
          </button>
          <button class="btn btn-outlined" onclick="instrutor.navigate('agenda')">
            <span class="material-symbols-rounded" style="font-size: 20px;">calendar_month</span>
            <span>Agenda</span>
          </button>
          <button class="btn btn-outlined" onclick="instrutor.navigate('financeiro')">
            <span class="material-symbols-rounded" style="font-size: 20px;">payments</span>
            <span>Financeiro</span>
          </button>
          <button class="btn btn-outlined" onclick="instrutor.navigate('suporte')">
            <span class="material-symbols-rounded" style="font-size: 20px;">support_agent</span>
            <span>Suporte</span>
          </button>
        </div>
      </div>
    `;
  },
  
  renderPerfil() {
    return `
      <div class="card" style="margin-top: 0;">
        <div style="text-align: center; margin-bottom: 16px;">
          <img src="${this.data.foto}" alt="${this.data.nome}" 
               style="width: 96px; height: 96px; border-radius: 50%; object-fit: cover; margin-bottom: 12px; border: 4px solid var(--accent);"
               onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2296%22 height=%2296%22%3E%3Crect fill=%22%234A90D9%22 width=%2296%22 height=%2296%22 rx=%2248%22/%3E%3Ctext x=%2250%25%22 y=%2250%25%22 dominant-baseline=%22middle%22 text-anchor=%22middle%22 font-family=%22Arial%22 font-size=%2236%22 fill=%22white%22%3EMS%3C/text%3E%3C/svg%3E'">
          <button class="btn btn-text" style="font-size: 12px;">Alterar Foto</button>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Informações Básicas</div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Nome</span>
          <span style="font-weight: 500;">${this.data.nome}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Email</span>
          <span style="font-weight: 500;">${this.data.email}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Telefone</span>
          <span style="font-weight: 500;">${this.data.telefone}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Cidade</span>
          <span style="font-weight: 500;">${this.data.cidade}</span>
        </div>
        <div class="list-item">
          <span style="color: var(--text-secondary);">Status</span>
          <span class="badge badge-success">Ativo</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Biografia</div>
        <textarea style="width: 100%; min-height: 100px; padding: 12px; border: 1px solid var(--divider); border-radius: 8px; font-family: inherit; resize: vertical;" maxlength="300">${this.data.biografia}</textarea>
        <div style="font-size: 12px; color: var(--text-secondary); margin-top: 4px;">
          ${this.data.biografia.length}/300 caracteres
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Estilo de Ensino</div>
        <div style="display: flex; flex-wrap: wrap; gap: 8px;">
          ${['calma', 'paciente', 'motivadora', 'didática', 'objetivo', 'rigoroso'].map(estilo => `
            <label style="padding: 8px 16px; background: ${this.data.estilo.includes(estilo) ? 'var(--secondary)' : 'var(--background)'}; color: ${this.data.estilo.includes(estilo) ? 'white' : 'var(--text-primary)'}; border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" ${this.data.estilo.includes(estilo) ? 'checked' : ''} style="display: none;">
              ${estilo}
            </label>
          `).join('')}
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">Especialidades</div>
        <div style="display: flex; flex-wrap: wrap; gap: 8px;">
          ${['iniciante', 'ansioso', 'baliza', 'rotatórias', 'reprovado', 'estrada'].map(esp => `
            <label style="padding: 8px 16px; background: ${this.data.especialidades.includes(esp) ? 'var(--secondary)' : 'var(--background)'}; color: ${this.data.especialidades.includes(esp) ? 'white' : 'var(--text-primary)'}; border-radius: 20px; font-size: 13px; cursor: pointer;">
              <input type="checkbox" ${this.data.especialidades.includes(esp) ? 'checked' : ''} style="display: none;">
              ${esp}
            </label>
          `).join('')}
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-primary" style="width: 100%;" onclick="onboarding.showToast('Alterações salvas com sucesso!', 'success')">Salvar Alterações</button>
      </div>
    `;
  },
  
  renderAgenda() {
    const hoje = new Date();
    const totalSlots = this.data.agendaSemanal.reduce((acc, dia) => acc + dia.slots, 0);
    const totalOcupados = this.data.agendaSemanal.reduce((acc, dia) => acc + dia.ocupados, 0);
    const taxaOcupacao = Math.round((totalOcupados / totalSlots) * 100);
    
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">📊 Ocupação Semanal</div>
        <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px;">
          <div style="text-align: center;">
            <div style="font-size: 24px; font-weight: 700; color: var(--secondary);">${totalOcupados}</div>
            <div style="font-size: 11px; color: var(--text-secondary);">AGENDADAS</div>
          </div>
          <div style="text-align: center;">
            <div style="font-size: 24px; font-weight: 700; color: var(--success);">${totalSlots - totalOcupados}</div>
            <div style="font-size: 11px; color: var(--text-secondary);">DISPONÍVEIS</div>
          </div>
          <div style="text-align: center;">
            <div style="font-size: 24px; font-weight: 700; color: var(--warning);">${taxaOcupacao}%</div>
            <div style="font-size: 11px; color: var(--text-secondary);">OCUPAÇÃO</div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📅 Esta Semana</div>
        ${this.data.agendaSemanal.map(dia => `
          <div class="list-item">
            <div style="flex: 1;">
              <div class="list-item-title">${dia.dia}</div>
              <div class="list-item-subtitle">${dia.ocupados}/${dia.slots} horários ocupados</div>
            </div>
            <div style="width: 120px;">
              <div class="progress-bar" style="height: 6px;">
                <div class="progress-fill" style="width: ${(dia.ocupados / dia.slots) * 100}%; background: ${dia.ocupados === dia.slots ? 'var(--error)' : 'var(--secondary)'}"></div>
              </div>
            </div>
          </div>
        `).join('')}
      </div>
      
      <div class="card">
        <div class="card-title">⏰ Próximas Aulas</div>
        ${this.data.proximasAulas.map(aula => `
          <div class="list-item">
            <div class="list-item-icon">
              <span class="material-symbols-rounded" style="color: var(--secondary);">person</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${aula.candidato}</div>
              <div class="list-item-subtitle">${helpers.formatDate(aula.data)} às ${aula.hora}</div>
            </div>
            <div class="list-item-action">
              ${aula.tipo === 'primeira_aula' ? '<span class="badge badge-info">1ª Aula</span>' : ''}
            </div>
          </div>
        `).join('')}
      </div>
      
      <div class="card">
        <button class="btn btn-primary" style="width: 100%; margin-bottom: 12px;" onclick="onboarding.showToast('Selecione um horário no calendário para adicionar', 'info')">
          <span class="material-symbols-rounded" style="font-size: 18px;">add</span>
          <span>Adicionar Horário</span>
        </button>
        <button class="btn btn-outlined" style="width: 100%;" onclick="onboarding.showToast('Gerenciamento de bloqueios em desenvolvimento', 'info')">
          Gerenciar Bloqueios
        </button>
      </div>
    `;
  },
  
  renderMidia() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">📹 Seus Vídeos (${this.data.videos.length}/5)</div>
        <p class="card-subtitle">Mostre seu trabalho para atrair mais alunos. Máximo 5 vídeos de até 60 segundos.</p>
        
        ${this.data.videos.map(video => `
          <div class="list-item">
            <div class="list-item-icon" style="border-radius: 8px; width: 64px; height: 64px; background: var(--background);">
              <span class="material-symbols-rounded" style="font-size: 32px; color: var(--secondary);">play_circle</span>
            </div>
            <div class="list-item-content">
              <div class="list-item-title">${video.titulo}</div>
              <div class="list-item-subtitle">${video.duracao}s • ${video.views} visualizações</div>
            </div>
            <div class="list-item-action">
              <button class="icon-btn">
                <span class="material-symbols-rounded">more_vert</span>
              </button>
            </div>
          </div>
        `).join('')}
      </div>
      
      ${this.data.videos.length < 5 ? `
        <div class="card">
          <button class="btn btn-primary" style="width: 100%;">
            <span class="material-symbols-rounded">add</span>
            <span>Adicionar Vídeo</span>
          </button>
        </div>
      ` : ''}
      
      <div class="card">
        <div class="card-title">💡 Dicas</div>
        <ul style="padding-left: 20px; color: var(--text-secondary); font-size: 14px; line-height: 1.8;">
          <li>Mostre aulas práticas (baliza, controle, etc)</li>
          <li>Não exponha rosto/nome de alunos sem autorização</li>
          <li>Mantenha conteúdo profissional e educativo</li>
          <li>Vídeos de qualidade atraem mais alunos</li>
        </ul>
      </div>
    `;
  },
  
  renderResultados() {
    return `
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.taxaConclusao}%</div>
          <div class="stat-label">Taxa Conclusão</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">3%</div>
          <div class="stat-label">Cancelamentos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.notaMedia}</div>
          <div class="stat-label">Nota Média</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.alunosAprovados}</div>
          <div class="stat-label">Aprovados</div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📊 Desempenho Mensal</div>
        <div class="list-item">
          <span>Aulas concluídas</span>
          <span style="font-weight: 700;">89 aulas</span>
        </div>
        <div class="list-item">
          <span>Taxa de pontualidade</span>
          <span style="font-weight: 700; color: var(--success);">${this.data.pontualidade}%</span>
        </div>
        <div class="list-item">
          <span>Nota média recebida</span>
          <span style="font-weight: 700;">${this.data.notaMedia} ⭐</span>
        </div>
        <div class="list-item">
          <span>Novos alunos</span>
          <span style="font-weight: 700;">12 alunos</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">🏆 Conquistas</div>
        <div style="padding: 16px; background: var(--background); border-radius: 8px; text-align: center; margin-bottom: 12px;">
          <div style="font-size: 48px; margin-bottom: 8px;">🥇</div>
          <div style="font-weight: 500; margin-bottom: 4px;">Top 5% Instrutores</div>
          <div style="font-size: 12px; color: var(--text-secondary);">Feira de Santana - BA</div>
        </div>
        <div style="padding: 16px; background: var(--background); border-radius: 8px; text-align: center;">
          <div style="font-size: 48px; margin-bottom: 8px;">⭐</div>
          <div style="font-weight: 500; margin-bottom: 4px;">Nota >4.5 há 6 meses</div>
          <div style="font-size: 12px; color: var(--text-secondary);">-2% de taxa ativa</div>
        </div>
      </div>
    `;
  },
  
  renderFinanceiro() {
    return `
      <div class="card" style="margin-top: 0; background: linear-gradient(135deg, var(--success) 0%, #2E7D32 100%); color: white;">
        <div style="font-size: 14px; opacity: 0.9; margin-bottom: 4px;">Saldo Disponível</div>
        <div style="font-size: 36px; font-weight: 700; margin-bottom: 16px;">${helpers.formatCurrency(this.data.saldoDisponivel)}</div>
        <button class="btn" style="background: white; color: var(--success); width: 100%;" onclick="onboarding.showToast('Solicitação de repasse enviada! Pagamento em até 24h.', 'success')">
          Solicitar Repasse
        </button>
      </div>
      
      <div class="card">
        <div class="card-title">💰 Resumo</div>
        <div class="list-item">
          <span>Saldo Pendente</span>
          <span style="font-weight: 700; color: var(--warning);">${helpers.formatCurrency(this.data.saldoPendente)}</span>
        </div>
        <div class="list-item">
          <span>Faturamento Total</span>
          <span style="font-weight: 700;">${helpers.formatCurrency(this.data.faturamentoTotal)}</span>
        </div>
        <div class="list-item">
          <span>Frequência de Repasse</span>
          <span style="font-weight: 500; text-transform: capitalize;">${this.data.frequenciaRepasse}</span>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📅 Próximos Repasses</div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--success);">schedule</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Segunda-feira, 07/04</div>
            <div class="list-item-subtitle">Repasse semanal</div>
          </div>
          <div class="list-item-action">
            <span style="font-weight: 700;">${helpers.formatCurrency(this.data.saldoDisponivel)}</span>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📊 Histórico (Março 2026)</div>
        <div class="list-item">
          <span>24/03/2026</span>
          <span style="font-weight: 700; color: var(--success);">+ R$ 1.240,00</span>
        </div>
        <div class="list-item">
          <span>17/03/2026</span>
          <span style="font-weight: 700; color: var(--success);">+ R$ 1.580,00</span>
        </div>
        <div class="list-item">
          <span>10/03/2026</span>
          <span style="font-weight: 700; color: var(--success);">+ R$ 1.320,00</span>
        </div>
      </div>
    `;
  },
  
  renderBancarios() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">🏦 Dados Cadastrados</div>
        <div class="list-item">
          <span>Banco</span>
          <span style="font-weight: 500;">${this.data.dadosBancarios.banco}</span>
        </div>
        <div class="list-item">
          <span>Tipo</span>
          <span style="font-weight: 500; text-transform: uppercase;">${this.data.dadosBancarios.tipo}</span>
        </div>
        <div class="list-item">
          <span>Chave/Conta</span>
          <span style="font-weight: 500;">${this.data.dadosBancarios.chave}</span>
        </div>
        <div class="list-item">
          <span>Titular</span>
          <span style="font-weight: 500;">${this.data.dadosBancarios.titular}</span>
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-outlined" style="width: 100%;">
          Alterar Dados Bancários
        </button>
      </div>
      
      <div class="card">
        <div style="padding: 12px; background: rgba(245, 124, 0, 0.1); border-left: 4px solid var(--warning); border-radius: 8px;">
          <div style="display: flex; gap: 12px;">
            <span class="material-symbols-rounded" style="color: var(--warning);">info</span>
            <div style="flex: 1;">
              <div style="font-weight: 500; margin-bottom: 4px;">Atenção</div>
              <div style="font-size: 13px; color: var(--text-secondary); line-height: 1.5;">
                Alterações de dados bancários passam por análise de segurança e podem levar de 24 a 48 horas para serem aprovadas.
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
  },
  
  renderAulas() {
    return `
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">${this.data.horasTrabalhadas}</div>
          <div class="stat-label">Total Horas</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.data.alunosAtendidos}</div>
          <div class="stat-label">Total Alunos</div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">📅 Aulas Agendadas</div>
        ${this.data.proximasAulas.map(aula => `
          <div class="list-item">
            <div class="list-item-content">
              <div class="list-item-title">${aula.candidato}</div>
              <div class="list-item-subtitle">${helpers.formatDate(aula.data)} às ${aula.hora}</div>
            </div>
            <span class="badge badge-info">Agendada</span>
          </div>
        `).join('')}
      </div>
      
      <div class="card">
        <div class="card-title">⚖️ Disputas</div>
        <div class="empty-state" style="padding: 32px 16px;">
          <span class="material-symbols-rounded" style="font-size: 48px; color: var(--success); margin-bottom: 12px;">check_circle</span>
          <h3>Nenhuma disputa aberta</h3>
          <p>Você mantém um excelente histórico!</p>
        </div>
      </div>
    `;
  },
  
  renderVeiculo() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">🚗 Veículo Cadastrado</div>
        <div class="list-item">
          <span>Modelo</span>
          <span style="font-weight: 500;">${this.data.veiculo.modelo}</span>
        </div>
        <div class="list-item">
          <span>Ano</span>
          <span style="font-weight: 500;">${this.data.veiculo.ano}</span>
        </div>
        <div class="list-item">
          <span>Placa</span>
          <span style="font-weight: 500;">${this.data.veiculo.placa}</span>
        </div>
        <div class="list-item">
          <span>Pedal Duplo</span>
          <span class="badge badge-success">${this.data.veiculo.temPedal ? 'Sim' : 'Não'}</span>
        </div>
        <div class="list-item">
          <span>Tipo</span>
          <span style="font-weight: 500; text-transform: capitalize;">${this.data.veiculo.tipo.replace(/_/g, ' ')}</span>
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-outlined" style="width: 100%; margin-bottom: 12px;">
          Editar Informações
        </button>
        <button class="btn btn-outlined" style="width: 100%;">
          Atualizar Documento do Veículo
        </button>
      </div>
      
      <div class="card">
        <div class="card-title">📋 Termo de Responsabilidade</div>
        <div style="padding: 12px; background: rgba(56, 142, 60, 0.1); border-left: 4px solid var(--success); border-radius: 8px;">
          <div style="display: flex; gap: 12px;">
            <span class="material-symbols-rounded" style="color: var(--success);">check_circle</span>
            <div style="flex: 1;">
              <div style="font-weight: 500; margin-bottom: 4px;">Termo Aceito</div>
              <div style="font-size: 13px; color: var(--text-secondary);">
                Aceito em 15/01/2024 às 14:32
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
  },
  
  renderComunidade() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">👥 Comunidade de Instrutores</div>
        <p class="card-subtitle">Troque experiências e aprenda com outros profissionais</p>
      </div>
      
      <div class="card">
        <div class="card-title">💬 Tópicos Recentes</div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Dicas para ensinar baliza</div>
            <div class="list-item-subtitle">Por João Carlos • 23 respostas</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Lidar com alunos muito ansiosos</div>
            <div class="list-item-subtitle">Por Ana Beatriz • 45 respostas</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Manutenção preventiva do carro</div>
            <div class="list-item-subtitle">Por Pedro Henrique • 12 respostas</div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <button class="btn btn-primary" style="width: 100%;">
          <span class="material-symbols-rounded">add</span>
          <span>Novo Tópico</span>
        </button>
      </div>
    `;
  },
  
  renderSuporte() {
    return `
      <div class="card" style="margin-top: 0;">
        <div class="card-title">💬 Fale Conosco</div>
        <p class="card-subtitle">Estamos aqui para ajudar!</p>
        <button class="btn btn-primary" style="width: 100%;">
          <span class="material-symbols-rounded">chat</span>
          <span>Iniciar Chat</span>
        </button>
      </div>
      
      <div class="card">
        <div class="card-title">📞 Outros Canais</div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--success);">phone</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">WhatsApp</div>
            <div class="list-item-subtitle">(71) 8300-0722</div>
          </div>
        </div>
        <div class="list-item">
          <div class="list-item-icon">
            <span class="material-symbols-rounded" style="color: var(--secondary);">mail</span>
          </div>
          <div class="list-item-content">
            <div class="list-item-title">Email</div>
            <div class="list-item-subtitle">suporte@cnhmais.com.br</div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="card-title">❓ Perguntas Frequentes</div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Como funciona o repasse?</div>
          </div>
          <span class="material-symbols-rounded">chevron_right</span>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">Como alterar minha agenda?</div>
          </div>
          <span class="material-symbols-rounded">chevron_right</span>
        </div>
        <div class="list-item">
          <div class="list-item-content">
            <div class="list-item-title">O que fazer em caso de disputa?</div>
          </div>
          <span class="material-symbols-rounded">chevron_right</span>
        </div>
      </div>
    `;
  }
  
};
