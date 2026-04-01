// ============================================
// MAPS.JS - Sistema de Mapas e Localização
// ============================================

const maps = {
  
  map: null,
  candidatoMarker: null,
  instrutorMarker: null,
  watchId: null,
  
  // Coordenadas mockadas (Feira de Santana - BA)
  mockLocations: {
    candidato: { lat: -12.2664, lng: -38.9663, name: 'João Silva' },
    instrutor: { lat: -12.2564, lng: -38.9563, name: 'Maria Santos' }
  },
  
  // Inicializar mapa
  init(containerId, options = {}) {
    const { 
      center = [-12.2664, -38.9663],
      zoom = 14,
      showCandidato = true,
      showInstrutor = true
    } = options;
    
    // Criar mapa
    this.map = L.map(containerId, {
      zoomControl: false // Remover controles padrão
    }).setView(center, zoom);
    
    // Adicionar tile layer (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap',
      maxZoom: 19
    }).addTo(this.map);
    
    // Adicionar controle de zoom customizado
    L.control.zoom({
      position: 'bottomright'
    }).addTo(this.map);
    
    // Adicionar marcadores
    if (showCandidato) {
      this.addCandidatoMarker();
    }
    
    if (showInstrutor) {
      this.addInstrutorMarker();
    }
    
    // Ajustar bounds para mostrar ambos
    if (showCandidato && showInstrutor) {
      const bounds = L.latLngBounds([
        [this.mockLocations.candidato.lat, this.mockLocations.candidato.lng],
        [this.mockLocations.instrutor.lat, this.mockLocations.instrutor.lng]
      ]);
      this.map.fitBounds(bounds, { padding: [50, 50] });
    }
    
    console.log('🗺️ Mapa inicializado');
  },
  
  // Adicionar marcador do candidato
  addCandidatoMarker() {
    const { lat, lng, name } = this.mockLocations.candidato;
    
    // Ícone customizado
    const icon = L.divIcon({
      className: 'custom-marker candidato-marker',
      html: `
        <div class="marker-pin">
          <span class="material-symbols-rounded">person_pin_circle</span>
        </div>
      `,
      iconSize: [40, 40],
      iconAnchor: [20, 40],
      popupAnchor: [0, -40]
    });
    
    this.candidatoMarker = L.marker([lat, lng], { icon })
      .addTo(this.map)
      .bindPopup(`
        <div class="marker-popup">
          <strong>${name}</strong>
          <p>Candidato</p>
          <small>Localização atual</small>
        </div>
      `);
  },
  
  // Adicionar marcador do instrutor
  addInstrutorMarker() {
    const { lat, lng, name } = this.mockLocations.instrutor;
    
    const icon = L.divIcon({
      className: 'custom-marker instrutor-marker',
      html: `
        <div class="marker-pin">
          <span class="material-symbols-rounded">drive_eta</span>
        </div>
      `,
      iconSize: [40, 40],
      iconAnchor: [20, 40],
      popupAnchor: [0, -40]
    });
    
    this.instrutorMarker = L.marker([lat, lng], { icon })
      .addTo(this.map)
      .bindPopup(`
        <div class="marker-popup">
          <strong>${name}</strong>
          <p>Instrutor</p>
          <small>Chegando em ~5 min</small>
        </div>
      `);
  },
  
  // Calcular distância entre candidato e instrutor
  calculateDistance() {
    const c = this.mockLocations.candidato;
    const i = this.mockLocations.instrutor;
    
    const from = L.latLng(c.lat, c.lng);
    const to = L.latLng(i.lat, i.lng);
    
    const distance = from.distanceTo(to); // metros
    const km = (distance / 1000).toFixed(1);
    
    return { distance, km };
  },
  
  // Desenhar rota entre candidato e instrutor
  drawRoute() {
    const c = this.mockLocations.candidato;
    const i = this.mockLocations.instrutor;
    
    // Linha simples (em produção usar API de rotas)
    const route = L.polyline([
      [c.lat, c.lng],
      [i.lat, i.lng]
    ], {
      color: '#4A90D9',
      weight: 4,
      opacity: 0.7,
      dashArray: '10, 10'
    }).addTo(this.map);
    
    return route;
  },
  
  // Simular movimento do instrutor
  simulateInstrutorMovement() {
    let step = 0;
    const steps = 20;
    const c = this.mockLocations.candidato;
    const i = this.mockLocations.instrutor;
    
    const latStep = (c.lat - i.lat) / steps;
    const lngStep = (c.lng - i.lng) / steps;
    
    const interval = setInterval(() => {
      step++;
      
      if (step >= steps) {
        clearInterval(interval);
        return;
      }
      
      const newLat = i.lat + (latStep * step);
      const newLng = i.lng + (lngStep * step);
      
      this.instrutorMarker.setLatLng([newLat, newLng]);
      
      // Atualizar popup
      const remaining = steps - step;
      const minutes = Math.ceil(remaining / 4);
      
      this.instrutorMarker.getPopup().setContent(`
        <div class="marker-popup">
          <strong>${i.name}</strong>
          <p>Instrutor</p>
          <small>Chegando em ~${minutes} min</small>
        </div>
      `);
      
    }, 1000); // Atualizar a cada 1s
  },
  
  // Obter localização real do usuário (GPS)
  getUserLocation() {
    return new Promise((resolve, reject) => {
      if (!navigator.geolocation) {
        reject(new Error('Geolocalização não suportada'));
        return;
      }
      
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          resolve({ lat: latitude, lng: longitude });
        },
        (error) => {
          reject(error);
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0
        }
      );
    });
  },
  
  // Monitorar localização em tempo real
  watchUserLocation(callback) {
    if (!navigator.geolocation) return;
    
    this.watchId = navigator.geolocation.watchPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        callback({ lat: latitude, lng: longitude });
      },
      (error) => {
        console.error('Erro ao obter localização:', error);
      },
      {
        enableHighAccuracy: true,
        maximumAge: 0,
        timeout: 5000
      }
    );
  },
  
  // Parar monitoramento
  stopWatching() {
    if (this.watchId) {
      navigator.geolocation.clearWatch(this.watchId);
      this.watchId = null;
    }
  },
  
  // Limpar mapa
  destroy() {
    this.stopWatching();
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
  }
  
};

// Adicionar estilo dos marcadores customizados ao DOM
const style = document.createElement('style');
style.textContent = `
  .custom-marker {
    background: transparent !important;
    border: none !important;
  }
  
  .marker-pin {
    width: 40px;
    height: 40px;
    background: white;
    border-radius: 50% 50% 50% 0;
    transform: rotate(-45deg);
    box-shadow: 0 2px 8px rgba(0,0,0,0.3);
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .marker-pin .material-symbols-rounded {
    transform: rotate(45deg);
    font-size: 24px;
  }
  
  .candidato-marker .marker-pin {
    background: #4A90D9;
  }
  
  .candidato-marker .material-symbols-rounded {
    color: white;
  }
  
  .instrutor-marker .marker-pin {
    background: #2E7D32;
  }
  
  .instrutor-marker .material-symbols-rounded {
    color: white;
  }
  
  .marker-popup {
    text-align: center;
  }
  
  .marker-popup strong {
    display: block;
    font-size: 14px;
    margin-bottom: 4px;
  }
  
  .marker-popup p {
    margin: 0;
    font-size: 12px;
    color: #666;
  }
  
  .marker-popup small {
    display: block;
    font-size: 11px;
    color: #999;
    margin-top: 4px;
  }
`;
document.head.appendChild(style);
