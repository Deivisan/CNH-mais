// ============================================
// DADOS MOCKADOS - CNH+ Demo PWA
// ============================================

const mockData = {
  
  // CANDIDATO: João Silva
  candidato: {
    id: 'cand_001',
    nome: 'João Silva',
    email: 'joao.silva@gmail.com',
    foto: 'assets/img/candidato-joao.jpg',
    cpf: '123.456.789-00',
    celular: '(71) 98765-4321',
    cidade: 'Feira de Santana - BA',
    renach: '00123456789',
    
    perfil: {
      abriuProcesso: true,
      passouTeorica: true,
      experiencia: 'poucas_vezes',
      ansiedade: 'media',
      reprovou: false,
      maiorDificuldade: ['baliza', 'rotatórias'],
      objetivo: 'aprender_calma',
      temCarro: 'nao',
      disponibilidade: {
        dias: ['segunda', 'quarta', 'sexta'],
        turnos: ['manhã', 'tarde']
      }
    },
    
    pacote: {
      aulasCompradas: 12,
      aulasRestantes: 4,
      aulasRealizadas: 8,
      aulasRecomendadas: 15,
      valorTotal: 'R$ 1.200,00',
      valorRestante: 'R$ 400,00'
    },
    
    instrutorId: 'inst_001',
    
    proximasAulas: [
      {
        id: 'aula_009',
        data: '2026-04-02',
        hora: '09:00',
        duracao: 60,
        tipo: 'Prática - Trânsito urbano',
        status: 'agendada'
      },
      {
        id: 'aula_010',
        data: '2026-04-04',
        hora: '14:00',
        duracao: 60,
        tipo: 'Prática - Baliza',
        status: 'agendada'
      }
    ],
    
    indicacoes: {
      total: 3,
      validadas: 2,
      proxima: 7, // faltam 7 para ganhar aula grátis
      aulasGanhas: 0,
      codigo: 'JOAO2026'
    }
  },
  
  // INSTRUTOR: Maria Santos
  instrutor: {
    id: 'inst_001',
    nome: 'Maria Santos',
    email: 'maria.santos@cnhmais.com',
    foto: 'assets/img/instrutor-maria.jpg',
    cpf: '987.654.321-00',
    telefone: '(71) 99876-5432',
    cidade: 'Feira de Santana - BA',
    
    biografia: 'Instrutora há 8 anos, especializada em alunos iniciantes e ansiosos. Ensino com paciência e didática diferenciada. Carro automático com pedal duplo.',
    
    estilo: ['calma', 'paciente', 'motivadora', 'didática'],
    especialidades: ['iniciante', 'ansioso', 'baliza', 'rotatórias'],
    regioes: ['Centro', 'Kalilândia', 'SIM', 'Tomba'],
    
    disponibilidade: {
      dias: ['segunda', 'terça', 'quarta', 'quinta', 'sexta', 'sábado'],
      turnos: ['manhã', 'tarde']
    },
    
    veiculo: {
      tipo: 'carro_proprio',
      modelo: 'Chevrolet Onix LTZ 1.0 Automático',
      ano: '2023',
      temPedal: true,
      placa: 'ABC-1D23'
    },
    
    // Métricas
    verificado: true,
    status: 'ativo',
    notaMedia: 4.8,
    avaliacoes: 127,
    horasTrabalhadas: 1240,
    alunosAtendidos: 340,
    alunosAprovados: 312,
    pontualidade: 96, // %
    cancelamentos: 'baixo', // 3%
    taxaConclusao: 98, // %
    
    // Financeiro
    saldoDisponivel: 2847.50,
    saldoPendente: 420.00,
    faturamentoTotal: 45890.00,
    frequenciaRepasse: 'semanal',
    
    dadosBancarios: {
      banco: 'Banco do Brasil',
      tipo: 'pix',
      chave: '(71) 99876-5432',
      titular: 'Maria Santos'
    },
    
    // Currículo
    autoescolas: [
      { nome: 'AutoEscola Volante', cidade: 'Feira de Santana', periodo: '2016-2020' },
      { nome: 'CFC Direção Segura', cidade: 'Feira de Santana', periodo: '2020-2024' }
    ],
    
    // Agenda semanal
    agendaSemanal: [
      { dia: 'Segunda', slots: 6, ocupados: 4, disponiveis: 2 },
      { dia: 'Terça', slots: 6, ocupados: 5, disponiveis: 1 },
      { dia: 'Quarta', slots: 6, ocupados: 3, disponiveis: 3 },
      { dia: 'Quinta', slots: 6, ocupados: 6, disponiveis: 0 },
      { dia: 'Sexta', slots: 6, ocupados: 4, disponiveis: 2 },
      { dia: 'Sábado', slots: 4, ocupados: 2, disponiveis: 2 }
    ],
    
    proximasAulas: [
      { id: 'aula_009', candidato: 'João Silva', data: '2026-04-02', hora: '09:00', tipo: 'trânsito' },
      { id: 'aula_010', candidato: 'João Silva', data: '2026-04-04', hora: '14:00', tipo: 'baliza' },
      { id: 'aula_045', candidato: 'Ana Costa', data: '2026-04-02', hora: '15:00', tipo: 'primeira_aula' },
      { id: 'aula_046', candidato: 'Pedro Lima', data: '2026-04-03', hora: '10:00', tipo: 'trânsito' }
    ],
    
    avaliacoesRecentes: [
      { candidato: 'Carlos Mendes', nota: 5, comentario: 'Excelente! Muito paciente e didática.', data: '2026-03-28' },
      { candidato: 'Juliana Rocha', nota: 5, comentario: 'Melhor instrutora! Passei de primeira graças a ela.', data: '2026-03-25' },
      { candidato: 'Roberto Alves', nota: 4, comentario: 'Ótima, só achei o carro um pouco sensível.', data: '2026-03-20' }
    ],
    
    videos: [
      { id: 'vid_001', titulo: 'Dicas de Baliza', duracao: 45, views: 234, url: '#' },
      { id: 'vid_002', titulo: 'Como vencer a ansiedade', duracao: 60, views: 187, url: '#' },
      { id: 'vid_003', titulo: 'Rotatórias sem medo', duracao: 38, views: 156, url: '#' }
    ]
  },
  
  // ADMIN: Dados globais da plataforma
  admin: {
    usuario: 'Administrador',
    email: 'admin@cnhmais.com',
    
    overview: {
      instrutoresAtivos: 512,
      candidatosAtivos: 8340,
      aulasHoje: 234,
      aulasSemana: 1879,
      aulasMes: 7456,
      faturamentoMes: 'R$ 745.600,00',
      ticketMedio: 'R$ 100,00',
      nps: 87,
      taxaAprovacao: 89
    },
    
    instrutoresPendentes: [
      { id: 'inst_512', nome: 'Carlos Pereira', cidade: 'Salvador - BA', status: 'aguardando_documentos', data: '2026-03-29' },
      { id: 'inst_513', nome: 'Fernanda Lima', cidade: 'Feira de Santana - BA', status: 'em_analise', data: '2026-03-30' },
      { id: 'inst_514', nome: 'José Souza', cidade: 'Vitória da Conquista - BA', status: 'em_analise', data: '2026-03-30' }
    ],
    
    disputasAbertas: [
      {
        id: 'disp_001',
        aula: 'aula_234',
        candidato: 'Ana Paula Silva',
        instrutor: 'Ricardo Oliveira',
        motivo: 'Atraso de 25 minutos',
        data: '2026-03-29',
        status: 'em_analise'
      },
      {
        id: 'disp_002',
        aula: 'aula_567',
        candidato: 'Marcos Vinicius',
        instrutor: 'Juliana Costa',
        motivo: 'Conduta inadequada',
        data: '2026-03-28',
        status: 'aguardando_resposta_instrutor'
      }
    ],
    
    financeiroMes: {
      totalRecebido: 745600.00,
      totalPago: 596480.00, // 80% para instrutores
      comissaoPlataforma: 149120.00, // 20%
      taxasCartao: 22368.00, // 3%
      taxasPix: 3728.00, // 0.5%
      lucroLiquido: 123024.00
    },
    
    topInstrutores: [
      { nome: 'Maria Santos', cidade: 'Feira de Santana', nota: 4.8, aulas: 89, faturamento: 8900 },
      { nome: 'João Carlos', cidade: 'Salvador', nota: 4.9, aulas: 102, faturamento: 10200 },
      { nome: 'Ana Beatriz', cidade: 'Vitória da Conquista', nota: 4.7, aulas: 76, faturamento: 7600 },
      { nome: 'Pedro Henrique', cidade: 'Feira de Santana', nota: 4.8, aulas: 81, faturamento: 8100 },
      { nome: 'Camila Rocha', cidade: 'Salvador', nota: 4.9, aulas: 94, faturamento: 9400 }
    ]
  }
  
};

// Helper para formatar valores
const helpers = {
  formatCurrency: (value) => {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  },
  
  formatDate: (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
  },
  
  formatDateTime: (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', { 
      day: '2-digit', 
      month: '2-digit', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  },
  
  getProgressPercent: (current, total) => {
    return Math.round((current / total) * 100);
  },
  
  getStars: (rating) => {
    const full = Math.floor(rating);
    const half = rating % 1 >= 0.5 ? 1 : 0;
    const empty = 5 - full - half;
    return '⭐'.repeat(full) + (half ? '½' : '') + '☆'.repeat(empty);
  }
};
