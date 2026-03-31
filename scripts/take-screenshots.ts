/**
 * 📸 CNH+ Screenshot Generator
 * 
 * Gera screenshots da Landing Page e Demo PWA
 * Execução: bun run screenshots
 */

import puppeteer from 'puppeteer';
import path from 'path';
import { fileURLToPath } from 'url';
import fs from 'fs';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.dirname(__dirname);
const SCREENSHOTS_DIR = path.join(ROOT, 'screenshots');

// Garante pasta
fs.mkdirSync(SCREENSHOTS_DIR, { recursive: true });

async function takeScreenshot(url: string, filename: string, width = 390, height = 844, viewport = { width: 1280, height: 800 }) {
  console.log(`📸 ${filename}...`);
  
  const browser = await puppeteer.launch({ 
    headless: true, 
    args: ['--no-sandbox', '--disable-setuid-sandbox'] 
  });
  
  try {
    const page = await browser.newPage();
    await page.setViewport(viewport);
    
    await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });
    await new Promise(resolve => setTimeout(resolve, 1500)); // Aguarda renderização
    
    const filePath = path.join(SCREENSHOTS_DIR, filename);
    await page.screenshot({ 
      path: filePath, 
      fullPage: true,
      type: 'png'
    });
    console.log(`✅ Salvo: screenshots/${filename}`);
    
    return filePath;
  } catch (err: any) {
    console.error(`❌ Erro em ${filename}: ${err.message.substring(0, 100)}`);
    throw err;
  } finally {
    await browser.close();
  }
}

async function takeMobileScreenshot(url: string, filename: string, waitForSelector?: string) {
  console.log(`📱 ${filename}...`);
  
  const browser = await puppeteer.launch({ 
    headless: true, 
    args: ['--no-sandbox', '--disable-setuid-sandbox'] 
  });
  
  try {
    const page = await browser.newPage();
    await page.setViewport({ width: 390, height: 844 });
    
    await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    if (waitForSelector) {
      try {
        await page.waitForSelector(waitForSelector, { timeout: 5000 });
        await new Promise(resolve => setTimeout(resolve, 1000));
      } catch {
        // Continua sem esperar
      }
    }
    
    const filePath = path.join(SCREENSHOTS_DIR, filename);
    await page.screenshot({ 
      path: filePath, 
      fullPage: false,
      type: 'png'
    });
    console.log(`✅ Salvo: screenshots/${filename}`);
    
    return filePath;
  } catch (err: any) {
    console.error(`❌ Erro em ${filename}: ${err.message.substring(0, 100)}`);
    throw err;
  } finally {
    await browser.close();
  }
}

async function main() {
  const landingUrl = `file://${path.join(ROOT, 'index.html')}`;
  const pwaUrl = `file://${path.join(ROOT, 'demo-pwa', 'index.html')}`;
  
  console.log('🚀 Iniciando geração de screenshots...\n');
  console.log(`📁 Pasta: ${SCREENSHOTS_DIR}\n`);
  
  try {
    // Landing Page (desktop)
    await takeScreenshot(landingUrl, 'landing-hero.png', 1280, 720, { width: 1280, height: 800 });
    
    // Demo PWA - Seletor de Perfil (mobile)
    await takeMobileScreenshot(pwaUrl, 'pwa-profile-selector.png');
    
    console.log('\n✅ TODOS screenshots gerados com sucesso!');
    console.log('\n📁 Arquivos criados:');
    fs.readdirSync(SCREENSHOTS_DIR).forEach(file => {
      const size = fs.statSync(path.join(SCREENSHOTS_DIR, file)).size;
      console.log(`   📸 ${file} (${(size / 1024).toFixed(1)}KB)`);
    });
    
  } catch (err) {
    console.error('\n❌ Erro ao gerar screenshots:', err);
    process.exit(1);
  }
}

main();
