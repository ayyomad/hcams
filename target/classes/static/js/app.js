// Basic helper functions for API calls with JWT stored in localStorage and cookies

function saveToken(token){ 
  localStorage.setItem('jwt', token); 
  document.cookie = `authToken=${token}; path=/; max-age=${30*24*60*60}`; // 30 days
}

function getToken(){ 
  const fromStorage = localStorage.getItem('jwt');
  if(fromStorage) return fromStorage;
  
  // Fallback to cookie
  const cookies = document.cookie.split(';');
  for(let cookie of cookies){
    const [name, value] = cookie.trim().split('=');
    if(name === 'authToken') return value;
  }
  return null;
}

function logout(){ 
  localStorage.removeItem('jwt'); 
  document.cookie = 'authToken=; path=/; max-age=0';
  window.location.href='/'; 
}

function headers(auth){
  const h = { 'Content-Type': 'application/json' };
  if(auth){ const t = getToken(); if(t) h['Authorization'] = 'Bearer ' + t; }
  return h;
}

// Check if user is authenticated
function isAuthenticated(){
  return getToken() !== null;
}

// Get current user info from token
function getCurrentUser(){
  const token = getToken();
  if(!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return {
      email: payload.sub,
      role: payload.role,
      userId: payload.userId
    };
  } catch(e) {
    return null;
  }
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

