// Basic helper functions for API calls with JWT stored in localStorage

function saveToken(token){ localStorage.setItem('jwt', token); }
function getToken(){ return localStorage.getItem('jwt'); }
function logout(){ localStorage.removeItem('jwt'); window.location.href='/'; }

function headers(auth){
  const h = { 'Content-Type': 'application/json' };
  if(auth){ const t = getToken(); if(t) h['Authorization'] = 'Bearer ' + t; }
  return h;
}

async function handle(res){
  if(!res.ok){
    let msg = 'Request failed';
    try { const data = await res.json(); msg = data.message || JSON.stringify(data); } catch(e) {}
    throw new Error(msg);
  }
  const ct = res.headers.get('Content-Type') || '';
  if(ct.includes('application/json')) return res.json();
  return res.text();
}

function showMsg(text, isError){
  const el = document.getElementById('msg');
  if(!el) return;
  el.className = isError ? 'error' : 'success';
  el.textContent = text;
}

function renderList(id, items){
  const ul = document.getElementById(id); if(!ul) return; ul.innerHTML = '';
  items.forEach(t => { const li = document.createElement('li'); li.textContent = t; ul.appendChild(li); });
}

async function apiGet(url, auth=false){ return handle(await fetch(url, { headers: headers(auth) })); }
async function apiPost(url, body, auth=false){ return handle(await fetch(url, { method:'POST', headers: headers(auth), body: JSON.stringify(body) })); }
async function apiPatch(url, auth=false, body){ return handle(await fetch(url, { method:'PATCH', headers: headers(auth), body: body? JSON.stringify(body): undefined })); }
async function apiDelete(url, auth=false){ return handle(await fetch(url, { method:'DELETE', headers: headers(auth) })); }

// Instructions to run locally:
// 1) Start MySQL and ensure database/user from application.properties exist
// 2) mvn spring-boot:run
// 3) Open http://localhost:8080/ in your browser

