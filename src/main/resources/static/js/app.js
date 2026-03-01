const API = '/api/equipment';
let editingId = null;

// ── CLOCK ──
function updateClock() {
    const now = new Date();
    document.getElementById('clock').textContent = now.toTimeString().slice(0, 8);
}
setInterval(updateClock, 1000);
updateClock();

// ── CONSOLE LOGGING ──
function logToConsole(method, url, status, reqBody, resBody) {
    const el = document.getElementById('consoleBody');
    const empty = el.querySelector('.console-empty');
    if (empty) empty.remove();

    const time = new Date().toTimeString().slice(0, 8);
    const ok = status >= 200 && status < 300;
    const statusColor = ok ? 'log-res-ok' : 'log-res-err';

    let html = `<div class="log-entry">`;
    html += `<div class="log-time">${time}</div>`;
    html += `<div class="log-req">→ ${method} ${url}</div>`;
    if (reqBody) html += `<div class="log-body">${JSON.stringify(reqBody, null, 2)}</div>`;
    html += `<div class="${statusColor}">← ${status} ${ok ? 'OK' : 'ERROR'}</div>`;
    if (resBody) {
        const preview = JSON.stringify(resBody, null, 2);
        const trimmed = preview.length > 300 ? preview.slice(0, 300) + '\n...' : preview;
        html += `<div class="log-body">${trimmed}</div>`;
    }
    html += `</div>`;

    el.insertAdjacentHTML('afterbegin', html);
}

// ── API HELPER ──
async function apiFetch(method, url, body) {
    const opts = {
        method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (body) opts.body = JSON.stringify(body);

    let resData = null;
    let status = 0;
    try {
        const res = await fetch(url, opts);
        status = res.status;
        if (status !== 204) {
            try { resData = await res.json(); } catch (e) {}
        }
        logToConsole(method, url.replace('http://localhost:8080', ''), status, body || null, resData);
        return { ok: res.ok, status, data: resData };
    } catch (err) {
        logToConsole(method, url.replace('http://localhost:8080', ''), 0, body || null, { error: err.message });
        return { ok: false, status: 0, data: null };
    }
}

// ── LOAD / RENDER ──
async function loadAll(params = '') {
    document.getElementById('maintWarning').style.display = 'none';
    const res = await apiFetch('GET', API + params, null);
    if (res.ok) renderTable(res.data);
}

function renderTable(data) {
    const tbody = document.getElementById('tableBody');
    document.getElementById('rowCount').textContent = `${data.length} RECORDS`;

    const counts = { RUNNING: 0, IDLE: 0, DOWNTIME: 0, MAINTENANCE: 0, OFFLINE: 0 };
    data.forEach(e => { if (counts[e.status] !== undefined) counts[e.status]++; });
    document.getElementById('s-running').textContent     = counts.RUNNING;
    document.getElementById('s-idle').textContent        = counts.IDLE;
    document.getElementById('s-downtime').textContent    = counts.DOWNTIME;
    document.getElementById('s-maintenance').textContent = counts.MAINTENANCE;
    document.getElementById('s-offline').textContent     = counts.OFFLINE;

    if (!data.length) {
        tbody.innerHTML = `<tr><td colspan="8" class="empty-state">NO RECORDS FOUND</td></tr>`;
        return;
    }

    tbody.innerHTML = data.map(e => `
    <tr id="row-${e.id}">
      <td class="id-cell">#${e.id}</td>
      <td><strong>${e.name || '—'}</strong></td>
      <td style="color:var(--dim)">${e.type || '—'}</td>
      <td style="font-family:var(--mono);font-size:12px">${e.location || '—'}</td>
      <td>
        <div class="status-badge status-${e.status}">
          <div class="status-dot dot-${e.status}"></div>
          ${e.status}
        </div>
      </td>
      <td style="font-family:var(--mono);font-size:12px">${e.lastMaintenanceDate || '—'}</td>
      <td style="font-family:var(--mono);font-size:12px">${e.operationalHours ?? '—'}</td>
      <td>
        <div class="actions-cell">
          <button class="btn btn-edit" onclick="startEdit(${e.id})">EDIT</button>
          <button class="btn btn-danger" onclick="deleteEquipment(${e.id})">DEL</button>
          <select class="status-select-inline status-badge status-${e.status}"
            onchange="quickStatus(${e.id}, this.value, this)"
            title="Quick status update">
            <option value="RUNNING"     ${e.status === 'RUNNING'     ? 'selected' : ''}>RUNNING</option>
            <option value="IDLE"        ${e.status === 'IDLE'        ? 'selected' : ''}>IDLE</option>
            <option value="DOWNTIME"    ${e.status === 'DOWNTIME'    ? 'selected' : ''}>DOWNTIME</option>
            <option value="MAINTENANCE" ${e.status === 'MAINTENANCE' ? 'selected' : ''}>MAINTENANCE</option>
            <option value="OFFLINE"     ${e.status === 'OFFLINE'     ? 'selected' : ''}>OFFLINE</option>
          </select>
        </div>
      </td>
    </tr>
  `).join('');
}

// ── FILTERS ──
function applyFilters() {
    const status   = document.getElementById('fStatus').value;
    const location = document.getElementById('fLocation').value.trim();
    const name     = document.getElementById('fName').value.trim();
    const params = [];
    if (status)   params.push(`status=${encodeURIComponent(status)}`);
    if (location) params.push(`location=${encodeURIComponent(location)}`);
    if (name)     params.push(`name=${encodeURIComponent(name)}`);
    loadAll(params.length ? '?' + params.join('&') : '');
}

function clearFilters() {
    document.getElementById('fStatus').value   = '';
    document.getElementById('fLocation').value = '';
    document.getElementById('fName').value     = '';
    loadAll();
}

// ── MAINTENANCE DUE ──
async function loadMaintenance() {
    const res = await apiFetch('GET', API + '/maintenance-due', null);
    if (res.ok) {
        renderTable(res.data);
        const w = document.getElementById('maintWarning');
        const c = document.getElementById('maintCount');
        if (res.data.length > 0) {
            c.textContent = res.data.length;
            w.style.display = 'block';
        } else {
            w.style.display = 'none';
        }
    }
}

// ── FORM SUBMIT ──
async function submitForm() {
    const payload = {
        name:                document.getElementById('fName2').value.trim(),
        type:                document.getElementById('fType').value.trim(),
        location:            document.getElementById('fLocation2').value.trim(),
        status:              document.getElementById('fStatus2').value,
        lastMaintenanceDate: document.getElementById('fDate').value || null,
        operationalHours:    parseInt(document.getElementById('fHours').value) || 0
    };

    if (!payload.name) { alert('Name is required.'); return; }
    if (!payload.lastMaintenanceDate) { alert('Last maintenance date is required.'); return; }

    let res;
    if (editingId) {
        res = await apiFetch('PUT', `${API}/${editingId}`, payload);
    } else {
        res = await apiFetch('POST', API, payload);
    }

    if (res.ok) {
        clearForm();
        loadAll();
    }
}

// ── EDIT ──
async function startEdit(id) {
    const res = await apiFetch('GET', `${API}/${id}`, null);
    if (!res.ok) return;
    const e = res.data;
    editingId = id;

    document.getElementById('fName2').value     = e.name || '';
    document.getElementById('fType').value      = e.type || '';
    document.getElementById('fLocation2').value = e.location || '';
    document.getElementById('fStatus2').value   = e.status || 'RUNNING';
    document.getElementById('fDate').value      = e.lastMaintenanceDate || '';
    document.getElementById('fHours').value     = e.operationalHours || 0;

    document.getElementById('submitBtn').textContent          = 'UPDATE EQUIPMENT';
    document.getElementById('cancelBtn').style.display        = 'inline-block';
    document.getElementById('editingBadge').style.display     = 'inline';
    document.getElementById('editingId').textContent          = id;

    document.getElementById('fName2').scrollIntoView({ behavior: 'smooth', block: 'center' });
    document.getElementById('fName2').focus();
}

function cancelEdit() {
    clearForm();
}

function clearForm() {
    editingId = null;
    ['fName2', 'fType', 'fLocation2', 'fDate', 'fHours'].forEach(id => document.getElementById(id).value = '');
    document.getElementById('fStatus2').value                 = 'RUNNING';
    document.getElementById('submitBtn').textContent          = 'ADD EQUIPMENT';
    document.getElementById('cancelBtn').style.display        = 'none';
    document.getElementById('editingBadge').style.display     = 'none';
}

// ── DELETE ──
async function deleteEquipment(id) {
    if (!confirm(`Delete equipment #${id}?`)) return;
    const res = await apiFetch('DELETE', `${API}/${id}`, null);
    if (res.ok) loadAll();
}

// ── QUICK STATUS PATCH ──
async function quickStatus(id, newStatus, selectEl) {
    const res = await apiFetch('PATCH', `${API}/${id}/status`, { status: newStatus });
    if (res.ok) {
        selectEl.className = `status-select-inline status-badge status-${newStatus}`;
        const row = document.getElementById(`row-${id}`);
        if (row) {
            const badgeDiv = row.querySelector('.status-badge:not(select)');
            if (badgeDiv) {
                badgeDiv.className = `status-badge status-${newStatus}`;
                badgeDiv.innerHTML = `<div class="status-dot dot-${newStatus}"></div>${newStatus}`;
            }
        }
    } else {
        loadAll();
    }
}

loadAll();