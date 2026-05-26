const PAGE_SIZE = 20;

const state = {
    airlines: {page: 1},
    airplanes: {page: 1},
    flights: {page: 1},
    inventories: {page: 1},
    fares: {page: 1},
    rules: {page: 1},
    adjustments: {page: 1},
};

document.addEventListener('DOMContentLoaded', () => {

    document.querySelectorAll('.admin-nav-btn').forEach(btn => {
        btn.addEventListener('click', () => {

            const section = btn.dataset.section;

            document.querySelectorAll('.admin-nav-btn')
                .forEach(b => b.classList.remove('active'));

            document.querySelectorAll('.admin-section')
                .forEach(s => s.classList.remove('active'));

            btn.classList.add('active');

            document
                .getElementById('section-' + section)
                .classList.add('active');

            if (section === 'routes') {
                loadRoutes();
            } else {
                loadSection(section, 1);
            }
        });
    });

    loadSection('airlines', 1);

});

async function loadSection(section, page) {
    state[section].page = page;

    const params = new URLSearchParams({page: page - 1, size: PAGE_SIZE});

    switch (section) {
        case 'airlines':
            val('al-country') && params.set('countryCode', val('al-country').toUpperCase());
            val('al-active') && params.set('active', val('al-active'));
            break;
        case 'airplanes':
            val('ap-airline') && params.set('airlineCode', val('ap-airline').toUpperCase());
            val('ap-type') && params.set('airplaneTypeCode', val('ap-type'));
            break;
        case 'flights':
            val('fl-route') && params.set('routeId', val('fl-route'));
            val('fl-status') && params.set('status', val('fl-status'));
            break;
        case 'inventories':
            val('inv-flight') && params.set('flightId', val('inv-flight'));
            break;
        case 'fares':
            val('fa-airline') && params.set('airlineCode', val('fa-airline').toUpperCase());
            val('fa-cabin') && params.set('cabinClass', val('fa-cabin'));
            break;
        case 'rules':
            val('ru-fare') && params.set('fareId', val('ru-fare'));
            break;
        case 'adjustments':
            val('adj-flight') && params.set('flightId', val('adj-flight'));
            val('adj-fare') && params.set('fareId', val('adj-fare'));
            break;
    }

    const url = `/api/v1/admin/${sectionToPath(section)}?${params}`;
    const wrap = document.getElementById(`${section}-table-wrap`);
    wrap.innerHTML = '<div class="admin-loading">Loading…</div>';

    try {
        const res = await apiFetch(url);
        const data = await res.json();
        if (!res.ok) {
            wrap.innerHTML = adminError(data.detail);
            return;
        }
        wrap.innerHTML = renderTable(section, data.content);
        renderPagination(section, data);
    } catch {
        wrap.innerHTML = adminError('Connection error.');
    }
}

function sectionToPath(section) {
    const map = {
        airlines: 'flights/airlines',
        airplanes: 'flights/airplanes',
        flights: 'flights',
        inventories: 'flights/inventories',
        fares: 'flights/fares',
        rules: 'flights/pricing/rules',
        adjustments: 'flights/pricing/adjustments',
    };
    return map[section];
}

function renderTable(section, rows) {
    if (!rows?.length) return '<p class="admin-empty">No records found.</p>';

    const configs = {
        airlines: {
            cols: ['Code', 'Name', 'Country', 'Active'],
            row: r => [r.code, r.name, r.countryCode, badge(r.active)],
            actions: r => `<button class="btn-icon btn-icon--danger" title="Delete"
                onclick="deleteAirline('${r.code}')">🗑</button>`
        },
        airplanes: {
            cols: ['Id', 'Number', 'Type', 'Airline', 'Year', 'Active'],
            row: r => [r.id, r.number, r.airplaneTypeCode, r.airlineCode, r.manufacturedYear, badge(r.active)],
            actions: r => `<button class="btn-icon btn-icon--danger" title="Delete"
                onclick="deleteAirplane('${r.id}')">🗑</button>`
        },
        flights: {
            cols: ['ID', 'Route', 'Type', 'Departure', 'Status'],
            row: r => [id(r.id), id(r.routeId), r.airplaneTypeCode,
                fmtInstant(r.scheduledTime?.departure), statusBadge(r.status)],
            actions: r => `<button class="btn-icon" title="Edit"
                onclick="openFlightUpdate('${r.id}','${r.status}')">✏</button>`
        },
        inventories: {
            cols: ['ID', 'Flight ID', 'Cabin', 'Total', 'Available', 'Held', 'Price'],
            row: r => [id(r.id), id(r.flightId), r.cabinClass,
                r.totalSeats, r.availableSeats, r.heldSeats,
                `$${Number(r.price).toFixed(2)}`],
            actions: r => `<button class="btn-icon" title="Edit"
                onclick="openInventoryUpdate('${r.id}','${r.price}')">✏</button>`
        },
        fares: {
            cols: ['ID', 'Airline', 'Cabin', 'Name', 'Baggage', 'Refund', 'Active'],
            row: r => [id(r.id), r.airlineCode, r.cabinClass, r.name,
                yesNo(r.baggageIncluded), yesNo(r.refundable), badge(r.active)],
            actions: r => `<button class="btn-icon btn-icon--danger" title="Delete"
                onclick="deleteFare('${r.id}')">🗑</button>`
        },
        rules: {
            cols: ['ID', 'Fare ID', 'Passenger', 'Multiplier', 'Active'],
            row: r => [id(r.id), id(r.fareId), r.passengerType,
                r.multiplier, badge(r.active)],
            actions: r => r.active ? `
                <button class="btn-icon" title="Edit"
                    onclick="openRuleUpdate('${r.id}','${r.multiplier}')">✏</button>
                <button class="btn-icon btn-icon--danger" title="Delete"
                    onclick="deleteRule('${r.id}')">🗑</button>` : ''
        },
        adjustments: {
            cols: ['ID', 'Flight', 'Fare', 'Type', 'Value', 'Active'],
            row: r => [id(r.id), id(r.flightId), id(r.fareId),
                r.type, r.value, badge(r.active)],
            actions: r => r.active ? `
                <button class="btn-icon" title="Edit"
                    onclick="openAdjUpdate('${r.id}','${r.type}','${r.value}')">✏</button>
                <button class="btn-icon btn-icon--danger" title="Delete"
                    onclick="deleteAdjustment('${r.id}')">🗑</button>` : ''
        },
    };

    const cfg = configs[section];
    const thead = cfg.cols.map(c => `<th>${c}</th>`).join('') + '<th></th>';
    const tbody = rows.map(r => {
        const cells = cfg.row(r).map(c => `<td>${c ?? '—'}</td>`).join('');
        return `<tr>${cells}<td class="admin-table__actions">${cfg.actions(r)}</td></tr>`;
    }).join('');

    return `<table class="admin-table">
        <thead><tr>${thead}</tr></thead>
        <tbody>${tbody}</tbody>
    </table>`;
}

function switchRouteTab(tab) {
    document.querySelectorAll('.admin-subtab').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.admin-subtab-panel').forEach(p => p.classList.remove('active'));
    document.querySelector(`[data-tab="rt-${tab}"]`).classList.add('active');
    document.getElementById(`rt-${tab}`).classList.add('active');
}

async function loadRoutes() {
    const params = new URLSearchParams();
    val('rt-airline') && params.set('airlineCode', val('rt-airline').toUpperCase());
    val('rt-dep') && params.set('departureAirportCode', val('rt-dep').toUpperCase());
    val('rt-arr') && params.set('arrivalAirportCode', val('rt-arr').toUpperCase());
    val('rt-active') && params.set('active', val('rt-active'));

    const wrap = document.getElementById('routes-table-wrap');
    wrap.innerHTML = '<div class="admin-loading">Loading…</div>';

    try {
        const res = await apiFetch(`/api/v1/admin/flights/routes?${params}`);
        const rows = await res.json();
        if (!res.ok) {
            wrap.innerHTML = adminError(rows.detail);
            return;
        }
        wrap.innerHTML = renderRouteTable(rows);
    } catch {
        wrap.innerHTML = adminError('Connection error.');
    }
}

async function loadRoutesWithoutFlights() {
    const wrap = document.getElementById('routes-nofly-wrap');
    wrap.innerHTML = '<div class="admin-loading">Loading…</div>';

    try {
        const res = await apiFetch('/api/v1/admin/flights/routes/without-flights');
        const rows = await res.json();
        if (!res.ok) {
            wrap.innerHTML = adminError(rows.detail);
            return;
        }
        wrap.innerHTML = renderRouteTable(rows);
    } catch {
        wrap.innerHTML = adminError('Connection error.');
    }
}

async function loadConflictingRoutes() {
    const dateVal = val('rt-conflict-date');
    if (!dateVal) {
        document.getElementById('routes-conflict-wrap').innerHTML = adminError('Please select a date.');
        return;
    }
    const instant = new Date(dateVal).toISOString();
    const wrap = document.getElementById('routes-conflict-wrap');
    wrap.innerHTML = '<div class="admin-loading">Loading…</div>';

    try {
        const res = await apiFetch(`/api/v1/admin/flights/routes/conflicting?date=${encodeURIComponent(instant)}`);
        const rows = await res.json();
        if (!res.ok) {
            wrap.innerHTML = adminError(rows.detail);
            return;
        }
        wrap.innerHTML = renderRouteTable(rows);
    } catch {
        wrap.innerHTML = adminError('Connection error.');
    }
}

function renderRouteTable(rows) {
    if (!rows?.length) return '<p class="admin-empty">No routes found.</p>';

    const tbody = rows.map(r => `<tr>
        <td><span title="${r.id}">${r.id}</span></td>
        <td>${r.number}</td>
        <td>${r.airline.code} — ${r.airline.name}</td>
        <td>${r.departureAirport.code} (${r.departureAirport.cityName})</td>
        <td>${r.arrivalAirport.code} (${r.arrivalAirport.cityName})</td>
        <td>${badge(r.isActive)}</td>
    </tr>`).join('');

    return `<table class="admin-table">
        <thead><tr>
            <th>ID</th><th>Number</th><th>Airline</th><th>From</th><th>To</th><th>Active</th>
        </tr></thead>
        <tbody>${tbody}</tbody>
    </table>`;
}

function renderPagination(section, data) {
    const el = document.getElementById(`${section}-pagination`);
    if (!el) return;
    if (data.totalPages <= 1) {
        el.innerHTML = '';
        return;
    }

    const cur = data.page + 1;
    let html = `<span class="admin-pag__info">Page ${cur} of ${data.totalPages} · ${data.totalElements} total</span>`;

    if (data.hasPrev) html += `<button class="btn btn--ghost btn--sm" onclick="loadSection('${section}',${cur - 1})">← Prev</button>`;
    if (data.hasNext) html += `<button class="btn btn--ghost btn--sm" onclick="loadSection('${section}',${cur + 1})">Next →</button>`;

    el.innerHTML = html;
}

async function createAirline() {
    const body = {code: val('al-c-code').toUpperCase(), name: val('al-c-name'), countryCode: val('al-c-country')};
    await crudAction('POST', '/api/v1/admin/flights/airlines', body, 'err-airline-create',
        () => {
            closeModal('modal-airline-create');
            loadSection('airlines', 1);
            showToast('Airline created');
        });
}

async function deleteAirline(code) {
    if (!confirm(`Delete airline ${code}?`)) return;
    await crudAction('DELETE', `/api/v1/admin/flights/airlines/${code}`, null, null,
        () => {
            loadSection('airlines', 1);
            showToast('Airline deleted');
        });
}

async function createAirplane() {
    const body = {
        number: val('ap-c-number'), airplaneTypeCode: val('ap-c-type'),
        airlineCode: val('ap-c-airline').toUpperCase(), manufacturedYear: parseInt(val('ap-c-year'))
    };
    await crudAction('POST', '/api/v1/admin/flights/airplanes', body, 'err-airplane-create',
        () => {
            closeModal('modal-airplane-create');
            loadSection('airplanes', 1);
            showToast('Airplane created');
        });
}

async function deleteAirplane(id) {
    if (!confirm('Delete this airplane?')) return;
    await crudAction('DELETE', `/api/v1/admin/flights/airplanes/${id}`, null, null,
        () => {
            loadSection('airplanes', 1);
            showToast('Airplane deleted');
        });
}

async function createFlight() {
    const dep = val('fl-c-dep'), arr = val('fl-c-arr');
    const body = {
        routeId: val('fl-c-route'), airplaneTypeCode: val('fl-c-aptype'),
        airplaneId: val('fl-c-airplane'),
        scheduledTime: {
            departure: dep ? new Date(dep).toISOString() : null,
            arrival: arr ? new Date(arr).toISOString() : null,
        }
    };
    await crudAction('POST', '/api/v1/admin/flights', body, 'err-flight-create',
        () => {
            closeModal('modal-flight-create');
            loadSection('flights', 1);
            showToast('Flight created');
        });
}

function openFlightUpdate(id, status) {
    document.getElementById('fl-u-id').value = id;
    document.getElementById('fl-u-status').value = status ?? '';
    document.getElementById('fl-u-airplane').value = '';
    document.getElementById('fl-u-adep').value = '';
    document.getElementById('fl-u-aarr').value = '';
    openModal('modal-flight-update');
}

async function updateFlight() {
    const id = val('fl-u-id');
    const adep = val('fl-u-adep'), aarr = val('fl-u-aarr');
    const body = {};
    if (val('fl-u-status')) body.status = val('fl-u-status');
    if (val('fl-u-airplane')) body.airplaneId = val('fl-u-airplane');
    if (adep || aarr) body.actualTime = {
        departure: adep ? new Date(adep).toISOString() : null,
        arrival: aarr ? new Date(aarr).toISOString() : null,
    };
    await crudAction('PATCH', `/api/v1/admin/flights/${id}`, body, 'err-flight-update',
        () => {
            closeModal('modal-flight-update');
            loadSection('flights', state.flights.page);
            showToast('Flight updated');
        });
}

async function createInventory() {
    const body = {flightId: val('inv-c-flight'), cabinClass: val('inv-c-cabin'), price: parseFloat(val('inv-c-price'))};
    await crudAction('POST', '/api/v1/admin/flights/inventories', body, 'err-inventory-create',
        () => {
            closeModal('modal-inventory-create');
            loadSection('inventories', 1);
            showToast('Inventory created');
        });
}

function openInventoryUpdate(id, price) {
    document.getElementById('inv-u-id').value = id;
    document.getElementById('inv-u-price').value = price;
    openModal('modal-inventory-update');
}

async function updateInventory() {
    const id = val('inv-u-id');
    const body = {price: parseFloat(val('inv-u-price'))};
    await crudAction('PATCH', `/api/v1/admin/flights/inventories/${id}`, body, 'err-inventory-update',
        () => {
            closeModal('modal-inventory-update');
            loadSection('inventories', state.inventories.page);
            showToast('Inventory updated');
        });
}

async function createFare() {
    const body = {
        airlineCode: val('fa-c-airline').toUpperCase(), cabinClass: val('fa-c-cabin'),
        name: val('fa-c-name'),
        baggageIncluded: document.getElementById('fa-c-baggage').checked,
        isRefundable: document.getElementById('fa-c-refund').checked,
    };
    await crudAction('POST', '/api/v1/admin/flights/fares', body, 'err-fare-create',
        () => {
            closeModal('modal-fare-create');
            loadSection('fares', 1);
            showToast('Fare created');
        });
}

async function deleteFare(id) {
    if (!confirm('Deactivate this fare?')) return;
    await crudAction('DELETE', `/api/v1/admin/flights/fares/${id}`, null, null,
        () => {
            loadSection('fares', state.fares.page);
            showToast('Fare deactivated');
        });
}

async function createRule() {
    const body = {fareId: val('ru-c-fare'), passengerType: val('ru-c-pax'), multiplier: parseFloat(val('ru-c-mult'))};
    await crudAction('POST', '/api/v1/admin/flights/pricing/rules', body, 'err-rule-create',
        () => {
            closeModal('modal-rule-create');
            loadSection('rules', 1);
            showToast('Rule created');
        });
}

function openRuleUpdate(id, multiplier) {
    document.getElementById('ru-u-id').value = id;
    document.getElementById('ru-u-mult').value = multiplier;
    openModal('modal-rule-update');
}

async function updateRule() {
    const id = val('ru-u-id');
    await crudAction('PATCH', `/api/v1/admin/flights/pricing/rules/${id}`,
        {multiplier: parseFloat(val('ru-u-mult'))}, 'err-rule-update',
        () => {
            closeModal('modal-rule-update');
            loadSection('rules', state.rules.page);
            showToast('Rule updated');
        });
}

async function deleteRule(id) {
    if (!confirm('Deactivate this rule?')) return;
    await crudAction('DELETE', `/api/v1/admin/flights/pricing/rules/${id}`, null, null,
        () => {
            loadSection('rules', state.rules.page);
            showToast('Rule deactivated');
        });
}

async function createAdjustment() {
    const body = {
        flightId: val('adj-c-flight'), fareId: val('adj-c-fare'),
        type: val('adj-c-type'), value: parseFloat(val('adj-c-value'))
    };
    await crudAction('POST', '/api/v1/admin/flights/pricing/adjustments', body, 'err-adj-create',
        () => {
            closeModal('modal-adj-create');
            loadSection('adjustments', 1);
            showToast('Adjustment created');
        });
}

function openAdjUpdate(id, type, value) {
    document.getElementById('adj-u-id').value = id;
    document.getElementById('adj-u-type').value = type;
    document.getElementById('adj-u-value').value = value;
    openModal('modal-adj-update');
}

async function updateAdjustment() {
    const id = val('adj-u-id');
    const body = {type: val('adj-u-type'), value: parseFloat(val('adj-u-value'))};
    await crudAction('PATCH', `/api/v1/admin/flights/pricing/adjustments/${id}`, body, 'err-adj-update',
        () => {
            closeModal('modal-adj-update');
            loadSection('adjustments', state.adjustments.page);
            showToast('Adjustment updated');
        });
}

async function deleteAdjustment(id) {
    if (!confirm('Deactivate this adjustment?')) return;
    await crudAction('DELETE', `/api/v1/admin/flights/pricing/adjustments/${id}`, null, null,
        () => {
            loadSection('adjustments', state.adjustments.page);
            showToast('Adjustment deactivated');
        });
}

async function crudAction(method, url, body, errElId, onSuccess) {
    const opts = {method, headers: {'Content-Type': 'application/json'}};
    if (body) opts.body = JSON.stringify(body);

    if (errElId) document.getElementById(errElId).style.display = 'none';

    try {
        const res = await apiFetch(url, opts);
        if (res.ok || res.status === 204) {
            onSuccess();
            return;
        }
        const err = await res.json().catch(() => ({}));
        const msg = err.detail ?? (err.errors ? Object.values(err.errors).flat().join(', ') : 'Operation failed.');
        if (errElId) showFieldError(errElId, msg);
        else alert(msg);
    } catch {
        if (errElId) showFieldError(errElId, 'Connection error.');
        else alert('Connection error.');
    }
}

function openModal(id) {
    document.getElementById(id).classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeModal(id) {
    document.getElementById(id).classList.remove('open');
    document.body.style.overflow = '';
}

document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', e => {
        if (e.target === overlay) closeModal(overlay.id);
    });
});

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal-overlay.open').forEach(m => closeModal(m.id));
    }
});

function val(id) {
    const el = document.getElementById(id);
    return el ? el.value.trim() : '';
}

function badge(active) {
    return active
        ? '<span class="tag tag--green" style="font-size:.7rem">active</span>'
        : '<span class="tag tag--muted" style="font-size:.7rem">inactive</span>';
}

function yesNo(v) {
    return v ? '✓' : '✕';
}

function statusBadge(s) {
    const colors = {
        scheduled: 'tag--blue', 'on time': 'tag--green', delayed: 'tag--muted',
        boarding: 'tag--blue', departed: 'tag--green', arrived: 'tag--green',
        cancelled: 'tag--muted', diverted: 'tag--muted'
    };
    return `<span class="tag ${colors[s] ?? 'tag--muted'}" style="font-size:.7rem">${s}</span>`;
}

function id(id) {
    return id ? `<span title="${id}">${id}</span>` : '—';
}

function fmtInstant(iso) {
    if (!iso) return '—';
    try {
        return new Date(iso).toLocaleString('en-GB', {dateStyle: 'short', timeStyle: 'short'});
    } catch {
        return iso;
    }
}

function adminError(msg) {
    return `<div class="auth-error" style="margin:1rem">${msg ?? 'Error'}</div>`;
}

function showFieldError(elId, msg) {
    const el = document.getElementById(elId);
    if (!el) return;
    el.textContent = msg;
    el.style.display = 'block';
}

function showToast(msg) {
    const t = document.getElementById('admin-toast');
    t.textContent = '✓ ' + msg;
    t.style.display = 'block';
    setTimeout(() => {
        t.style.display = 'none';
    }, 2500);
}
