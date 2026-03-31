const panelButtons = Array.from(document.querySelectorAll('.menu-btn'));
const panels = Array.from(document.querySelectorAll('.panel'));
const quickGoButtons = Array.from(document.querySelectorAll('[data-go]'));
const forms = Array.from(document.querySelectorAll('.emulated-form'));
const actionButtons = Array.from(document.querySelectorAll('[data-action]'));
const chooseInstrutorButtons = Array.from(document.querySelectorAll('.choose-instrutor'));
const chatButtons = Array.from(document.querySelectorAll('.send-chat'));
const mapPointButtons = Array.from(document.querySelectorAll('.map-point'));
const toast = document.getElementById('toast');

const AI_STORAGE_KEY = 'cnhplus_demo_pwa_ai_config';
const MAP_DEFAULT_BBOX = '-38.9963%2C-12.2864%2C-38.9363%2C-12.2464';

const roleToPanel = {
  candidato: 'panel-candidato-home',
  instrutor: 'panel-instrutor-home',
  admin: 'panel-admin-home'
};

const bairroToPoint = {
  centro: { lat: -12.2664, lon: -38.9663, label: 'centro' },
  'fraga maia': { lat: -12.2525, lon: -38.9553, label: 'fraga maia' },
  baliza: { lat: -12.2292, lon: -38.9751, label: 'baliza' },
  kalilandia: { lat: -12.2572, lon: -38.9498, label: 'kalilândia' },
  'kalilândia': { lat: -12.2572, lon: -38.9498, label: 'kalilândia' },
  tomba: { lat: -12.2924, lon: -38.9575, label: 'tomba' },
  'feira de santana': { lat: -12.2664, lon: -38.9663, label: 'feira de santana' }
};

function showToast(message) {
  if (!toast) return;
  toast.textContent = message;
  toast.classList.add('show');
  window.clearTimeout(showToast._timer);
  showToast._timer = window.setTimeout(() => toast.classList.remove('show'), 2400);
}

function activatePanel(panelId) {
  if (!panelId) return;

  panels.forEach((panel) => {
    panel.classList.toggle('active', panel.id === panelId);
  });

  panelButtons.forEach((btn) => {
    btn.classList.toggle('active', btn.dataset.panel === panelId);
  });

  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function buildMapEmbedUrl(lat, lon) {
  const bbox = `${lon - 0.03}%2C${lat - 0.02}%2C${lon + 0.03}%2C${lat + 0.02}`;
  return `https://www.openstreetmap.org/export/embed.html?bbox=${bbox}&layer=mapnik&marker=${lat}%2C${lon}`;
}

function updateMapByCoordinates(targetMapId, lat, lon, label = 'ponto') {
  const iframe = document.getElementById(targetMapId);
  if (!iframe) return false;
  iframe.src = buildMapEmbedUrl(lat, lon);
  showToast(`mapa atualizado: ${label} 🗺️`);
  return true;
}

function findPointByText(text) {
  const normalized = text
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '');

  for (const [bairro, point] of Object.entries(bairroToPoint)) {
    const bairroNormalized = bairro
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
    if (normalized.includes(bairroNormalized)) return point;
  }

  return null;
}

function appendChatBubble(thread, text, type = 'bot') {
  const bubble = document.createElement('div');
  bubble.className = `bubble ${type}`;
  bubble.textContent = text;
  thread.appendChild(bubble);
  thread.scrollTop = thread.scrollHeight;
}

function getAIConfig() {
  try {
    const raw = localStorage.getItem(AI_STORAGE_KEY);
    if (!raw) return null;
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

function setAIConfig(config) {
  localStorage.setItem(AI_STORAGE_KEY, JSON.stringify(config));
}

function clearAIConfig() {
  localStorage.removeItem(AI_STORAGE_KEY);
}

function loadAIConfigToForm() {
  const cfg = getAIConfig();
  if (!cfg) return;

  const endpointEl = document.getElementById('ai-endpoint');
  const modelEl = document.getElementById('ai-model');
  const tempEl = document.getElementById('ai-temperature');

  if (endpointEl && cfg.endpoint) endpointEl.value = cfg.endpoint;
  if (modelEl && cfg.model) modelEl.value = cfg.model;
  if (tempEl && typeof cfg.temperature === 'number') tempEl.value = String(cfg.temperature);
}

async function requestAIResponse(userText) {
  const cfg = getAIConfig();
  if (!cfg?.endpoint || !cfg?.model || !cfg?.apiKey) {
    return null;
  }

  const body = {
    model: cfg.model,
    temperature: Number(cfg.temperature ?? 0.2),
    messages: [
      {
        role: 'system',
        content:
          'Você é assistente de rota da CNH+ demo PWA. Responda curto em pt-BR. Se o usuário pedir bairro/local, responda JSON válido no formato {"action":"move_map","destination":"nome","message":"texto"}. Se não for rota, use {"action":"chat","message":"texto"}. Se o usuário disser "cheguei", finalize com humor leve: "ah ops, eu sou só uma IA, programada por Deivison."'
      },
      { role: 'user', content: userText }
    ]
  };

  const response = await fetch(cfg.endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${cfg.apiKey}`
    },
    body: JSON.stringify(body)
  });

  if (!response.ok) {
    throw new Error(`erro HTTP ${response.status}`);
  }

  const data = await response.json();
  const content = data?.choices?.[0]?.message?.content;
  if (!content) return null;
  return String(content).trim();
}

function parseAIAction(raw) {
  if (!raw) return null;

  const sanitized = raw.replace(/^```json\s*/i, '').replace(/^```/i, '').replace(/```$/i, '').trim();
  try {
    const parsed = JSON.parse(sanitized);
    if (parsed && typeof parsed === 'object') return parsed;
  } catch {
    // fallback para texto normal
  }

  return { action: 'chat', message: raw };
}

function localRouteLogic(userText) {
  const text = userText.toLowerCase();

  if (text.includes('cheguei')) {
    return {
      action: 'chat',
      message: 'cheguei no ponto combinado ✅ ah ops, eu sou só uma ia, programada por Deivison. 😄'
    };
  }

  const point = findPointByText(userText);
  if (point) {
    return {
      action: 'move_map',
      destination: point.label,
      message: `rota ajustada para ${point.label}. já estou te guiando por esse trajeto.`
    };
  }

  return {
    action: 'chat',
    message: 'entendi! posso te ajudar com rota para centro, fraga maia, tomba, kalilândia ou baliza.'
  };
}

async function handleCandidateChatWithAI(chatContainer, userText) {
  const thread = chatContainer.querySelector('.chat-thread');
  if (!thread) return;

  let aiAction = null;

  try {
    const raw = await requestAIResponse(userText);
    if (raw) {
      aiAction = parseAIAction(raw);
    }
  } catch {
    aiAction = null;
  }

  if (!aiAction) {
    aiAction = localRouteLogic(userText);
  }

  const actionType = (aiAction.action || '').toLowerCase();
  const message = aiAction.message || 'ok';

  if (actionType === 'move_map') {
    const destination = aiAction.destination || userText;
    const point = findPointByText(destination) || findPointByText(userText);
    if (point) {
      updateMapByCoordinates('map-candidato', point.lat, point.lon, point.label);
      activatePanel('panel-candidato-mapas');
      appendChatBubble(thread, `${message} 🧭`, 'bot');
      return;
    }
  }

  appendChatBubble(thread, message, 'bot');
}

function bindPanelsAndButtons() {
  panelButtons.forEach((button) => {
    button.addEventListener('click', () => activatePanel(button.dataset.panel));
  });

  quickGoButtons.forEach((button) => {
    button.addEventListener('click', () => activatePanel(button.dataset.go));
  });

  forms.forEach((form) => {
    form.addEventListener('submit', (event) => {
      event.preventDefault();

      const successMessage = form.dataset.success || 'ação simulada com sucesso';
      const nextPanel = form.dataset.next;

      if (form.closest('#panel-login')) {
        const roleSelect = form.querySelector('[data-login-role]');
        const role = roleSelect?.value || 'candidato';
        const rolePanel = roleToPanel[role] || 'panel-candidato-home';
        showToast(`login ${role} emulado com sucesso ✅`);
        activatePanel(rolePanel);
        return;
      }

      showToast(`${successMessage} ✅`);

      if (nextPanel) {
        activatePanel(nextPanel);
      }
    });
  });

  actionButtons.forEach((button) => {
    button.addEventListener('click', () => {
      const action = button.dataset.action || 'ação';
      showToast(`${action} (emulado)`);
    });
  });

  chooseInstrutorButtons.forEach((button) => {
    button.addEventListener('click', () => {
      const instrutor = button.dataset.name || 'instrutor';
      const assignedEl = document.getElementById('assigned-instrutor');
      if (assignedEl) assignedEl.textContent = instrutor;
      showToast(`match confirmado com ${instrutor} 🚗`);
      activatePanel('panel-candidato-home');
    });
  });
}

function bindMapButtons() {
  mapPointButtons.forEach((button) => {
    button.addEventListener('click', () => {
      const target = button.dataset.mapTarget;
      const lat = Number(button.dataset.lat);
      const lon = Number(button.dataset.lon);
      const label = button.dataset.label || 'ponto';
      if (!target || Number.isNaN(lat) || Number.isNaN(lon)) return;

      updateMapByCoordinates(target, lat, lon, label);

      const siblings = button.parentElement?.querySelectorAll('.map-point') || [];
      siblings.forEach((item) => item.classList.remove('active'));
      button.classList.add('active');
    });
  });
}

function bindChat() {
  chatButtons.forEach((button) => {
    button.addEventListener('click', async () => {
      const chatContainer = button.closest('.chat');
      const input = chatContainer?.querySelector('input');
      const thread = chatContainer?.querySelector('.chat-thread');
      if (!input || !thread || !chatContainer) return;

      const text = input.value.trim();
      if (!text) return;

      appendChatBubble(thread, text, 'me');
      input.value = '';

      if (chatContainer.dataset.chat === 'candidato') {
        await handleCandidateChatWithAI(chatContainer, text);
      } else {
        window.setTimeout(() => {
          appendChatBubble(thread, 'resposta automática da demo: mensagem recebida e registrada ✅', 'bot');
        }, 450);
      }
    });
  });
}

function bindAIConfigPanel() {
  const saveBtn = document.getElementById('save-ai-config');
  const clearBtn = document.getElementById('clear-ai-config');
  const testBtn = document.getElementById('test-ai-config');

  const endpointEl = document.getElementById('ai-endpoint');
  const modelEl = document.getElementById('ai-model');
  const keyEl = document.getElementById('ai-api-key');
  const tempEl = document.getElementById('ai-temperature');

  if (saveBtn) {
    saveBtn.addEventListener('click', () => {
      const config = {
        endpoint: endpointEl?.value?.trim(),
        model: modelEl?.value?.trim(),
        apiKey: keyEl?.value?.trim(),
        temperature: Number(tempEl?.value || 0.2)
      };

      if (!config.endpoint || !config.model || !config.apiKey) {
        showToast('preencha endpoint, modelo e api key para salvar.');
        return;
      }

      setAIConfig(config);
      showToast('configuração de ia salva localmente ✅');
    });
  }

  if (clearBtn) {
    clearBtn.addEventListener('click', () => {
      clearAIConfig();
      if (keyEl) keyEl.value = '';
      showToast('configuração local removida.');
    });
  }

  if (testBtn) {
    testBtn.addEventListener('click', () => {
      activatePanel('panel-candidato-chat');
      showToast('digite no chat: "me leve para fraga maia" 🧠');
    });
  }
}

function bindKeyboardShortcuts() {
  window.addEventListener('keydown', (event) => {
    if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
      event.preventDefault();
      activatePanel('panel-login');
      showToast('atalho: abriu login rápido 🔐');
    }
  });
}

function initServiceWorker() {
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('./sw.js').catch(() => {
        // ignora erro de sw em ambiente de preview
      });
    });
  }
}

function init() {
  bindPanelsAndButtons();
  bindMapButtons();
  bindChat();
  bindAIConfigPanel();
  bindKeyboardShortcuts();
  loadAIConfigToForm();
  initServiceWorker();

  const candidatoMap = document.getElementById('map-candidato');
  if (candidatoMap && !candidatoMap.src) {
    candidatoMap.src = `https://www.openstreetmap.org/export/embed.html?bbox=${MAP_DEFAULT_BBOX}&layer=mapnik&marker=-12.2664%2C-38.9663`;
  }
}

init();
