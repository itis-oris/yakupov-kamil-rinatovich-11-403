let nextCursor = null;
const IS_AUTHENTICATED = document.getElementById('search-meta')?.dataset.auth === 'true';

const FIELD_MAP = {
    'cityCodeFrom': 'cityCodeFrom',
    'cityCodeTo': 'cityCodeTo',
    'scheduledDate.departure': 'depDate',
    'scheduledDate.arrival': 'arrDate',
    'passengers': 'adults',
    'cabinClass': 'cabinClass',
    'filter.priceRange.begin': 'priceMin',
    'filter.priceRange.end': 'priceMax',
    'filter.scheduledDepartureTimeType': 'depTime',
    'filter.orderType': 'orderType',
};

document.getElementById('btn-search').addEventListener('click', () => doSearch(true));
document.getElementById('btn-apply-filters').addEventListener('click', () => doSearch(true));
document.getElementById('btn-load-more').addEventListener('click', () => doSearch(false));

async function doSearch(reset) {
    if (reset) nextCursor = null;

    clearAllErrors();

    const body = buildRequest();
    const btn = document.getElementById('btn-search');
    btn.disabled = true;
    document.getElementById('btn-search-text').textContent = 'Searching…';

    try {
        const res = await fetch('/api/v1/flights/search', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body),
        });

        const data = await res.json();

        if (!res.ok) {
            handleApiError(data);
            return;
        }

        if (reset) document.getElementById('results-list').innerHTML = '';
        renderFlights(data.flights ?? []);
        populateFilters(data.filter);

        nextCursor = data.nextCursor ?? null;
        document.getElementById('load-more-wrap').style.display = nextCursor ? 'block' : 'none';
        document.getElementById('results-section').style.display = 'block';
        document.getElementById('filter-sidebar').style.display = 'block';

    } catch (e) {
        showBanner('Could not connect to the server. Please try again.');
    } finally {
        btn.disabled = false;
        document.getElementById('btn-search-text').textContent = 'Search';
    }
}

function handleApiError(err) {
    if (err.errors && Object.keys(err.errors).length) {
        let hasInlineErrors = false;

        for (const [field, messages] of Object.entries(err.errors)) {
            const inputId = FIELD_MAP[field];
            if (inputId) {
                markFieldError(inputId, messages[0]);
                hasInlineErrors = true;
            }
        }

        const first = document.querySelector('.field--error');
        if (first) first.scrollIntoView({behavior: 'smooth', block: 'center'});

        const unmapped = Object.entries(err.errors)
            .filter(([field]) => !FIELD_MAP[field])
            .flatMap(([field, msgs]) => msgs.map(m => `${field}: ${m}`));
        if (unmapped.length) showBanner(unmapped.join(' · '));

    } else if (err.detail) {
        showBanner(err.detail);
    } else {
        showBanner(`Something went wrong (${err.status ?? 'unknown'})`);
    }
}

function markFieldError(inputId, message) {
    const input = document.getElementById(inputId);
    if (!input) return;

    const fieldEl = input.closest('.field') ?? input.parentElement;
    fieldEl.classList.add('field--error');

    let msgEl = fieldEl.querySelector('.field__error-msg');
    if (!msgEl) {
        msgEl = document.createElement('div');
        msgEl.className = 'field__error-msg';
        fieldEl.appendChild(msgEl);
    }
    msgEl.textContent = message;

    input.addEventListener('change', () => clearFieldError(fieldEl), {once: true});
    input.addEventListener('input', () => clearFieldError(fieldEl), {once: true});
}

function clearFieldError(fieldEl) {
    fieldEl.classList.remove('field--error');
    fieldEl.querySelector('.field__error-msg')?.remove();
}

function clearAllErrors() {
    document.querySelectorAll('.field--error').forEach(clearFieldError);
    hideBanner();
}

function showBanner(message) {
    let banner = document.getElementById('error-banner');
    if (!banner) {
        banner = document.createElement('div');
        banner.id = 'error-banner';
        banner.className = 'error-banner';
        document.querySelector('.hero').insertAdjacentElement('afterend', banner);
    }
    banner.innerHTML = `
    <span class="error-banner__icon">⚠</span>
    <span>${message}</span>
    <button class="error-banner__close" onclick="hideBanner()">✕</button>`;
    banner.style.display = 'flex';
    banner.scrollIntoView({behavior: 'smooth', block: 'center'});
}

function hideBanner() {
    const b = document.getElementById('error-banner');
    if (b) b.style.display = 'none';
}

function buildRequest() {
    const adults = +document.getElementById('adults').value;
    const children = +document.getElementById('children').value;
    const infants = +document.getElementById('infants').value;

    const passengers = {adult: adults};
    if (children > 0) passengers.child = children;
    if (infants > 0) passengers.infant = infants;

    const filter = {};
    const priceMin = document.getElementById('priceMin').value;
    const priceMax = document.getElementById('priceMax').value;
    if (priceMin || priceMax) filter.priceRange = {begin: +priceMin || 0, end: +priceMax || null};

    const baggage = document.getElementById('baggage').checked;
    const refundable = document.getElementById('refundable').checked;
    if (baggage) filter.isBaggageIncluded = true;
    if (refundable) filter.isRefundable = true;

    const depTime = document.getElementById('depTime').value;
    if (depTime) filter.scheduledDepartureTimeType = depTime;

    const orderType = document.getElementById('orderType').value;
    if (orderType) filter.orderType = orderType;

    const checkedAirlines = [...document.querySelectorAll('#filter-airlines input:checked')].map(el => el.value);
    if (checkedAirlines.length) filter.airlineCodes = checkedAirlines;

    return {
        cityCodeFrom: document.getElementById('cityCodeFrom').value,
        cityCodeTo: document.getElementById('cityCodeTo').value,
        scheduledDate: {
            departure: document.getElementById('depDate').value,
            arrival: document.getElementById('arrDate').value || null,
        },
        passengers,
        cabinClass: document.getElementById('cabinClass').value,
        filter: Object.keys(filter).length ? filter : null,
        cursor: nextCursor,
    };
}

function renderFlights(flights) {
    const list = document.getElementById('results-list');
    if (!flights.length && !list.children.length) {
        list.innerHTML = '<p style="color:var(--muted);text-align:center;padding:3rem">No flights found.</p>';
        return;
    }
    flights.forEach(f => {
        const card = document.createElement('div');
        card.className = 'flight-card';
        card.style.cursor = 'pointer';
        card.addEventListener('click', () => openFlightModal(f));
        card.innerHTML = `
      <div class="flight-route">
        <div class="flight-airport">
          <div class="flight-airport__time">${f.scheduledDeparture.substring(11, 16)}</div>
          <div class="flight-airport__code">${f.departureAirport.code}</div>
          <div class="flight-airport__city">${f.departureAirport.cityName}</div>
        </div>
        <div class="flight-line">
          <div class="flight-line__bar">
            <div class="flight-line__dot"></div>
            <div class="flight-line__track"><span>✈</span></div>
            <div class="flight-line__dot"></div>
          </div>
          <div class="flight-line__date">${f.scheduledDeparture.substring(0, 10)}</div>
        </div>
        <div class="flight-airport">
          <div class="flight-airport__time">${f.scheduledArrival.substring(11, 16)}</div>
          <div class="flight-airport__code">${f.arrivalAirport.code}</div>
          <div class="flight-airport__city">${f.arrivalAirport.cityName}</div>
        </div>
      </div>
      <div class="flight-card__price">$${Number(f.price).toFixed(0)}</div>`;
        list.appendChild(card);
    });
}

function populateFilters(filter) {
    if (!filter?.airlines?.length) return;
    const el = document.getElementById('filter-airlines');
    if (el.children.length) return;
    el.innerHTML = filter.airlines.map(a =>
        `<label class="filter-checkbox">
      <input type="checkbox" value="${a.code}"/> ${a.name}
      <span style="color:var(--muted);font-size:.75rem">${a.code}</span>
    </label>`
    ).join('');
}

document.getElementById('btn-swap').addEventListener('click', () => {
    const f = document.getElementById('cityCodeFrom');
    const t = document.getElementById('cityCodeTo');
    [f.value, t.value] = [t.value, f.value];
});

document.querySelectorAll('.counter__btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const input = document.getElementById(btn.dataset.target);
        const min = +(btn.dataset.min ?? 0);
        const max = +(btn.dataset.max ?? 99);
        input.value = Math.min(max, Math.max(min, +input.value + +btn.dataset.delta));
    });
});

let modalState = {
    flightId: null, fareId: null, data: null,
    seatAssignments: {}, activePaxKey: null, passengers: [],
};

const PAX_LABELS = {adult: 'Adult', child: 'Child', infant: 'Infant'};
const SEAT_TYPE_ICONS = {window: '🪟', middle: '↔', aisle: '🚶'};

function openFlightModal(flight) {
    if (!IS_AUTHENTICATED) {
        showBanner('Please <a href="/login" style="color:var(--blue);text-decoration:underline">sign in</a> to select a fare and book a flight.');
        return;
    }
    const adults = +document.getElementById('adults').value || 1;
    const children = +document.getElementById('children').value || 0;
    const paxList = [];
    for (let i = 0; i < adults; i++) paxList.push({type: 'adult', label: adults > 1 ? `Adult ${i + 1}` : 'Adult'});
    for (let i = 0; i < children; i++) paxList.push({type: 'child', label: children > 1 ? `Child ${i + 1}` : 'Child'});

    modalState = {
        flightId: flight.flightId, fareId: null, data: null,
        seatAssignments: {}, activePaxKey: paxList[0]?.label ?? null,
        passengers: paxList,
    };

    document.getElementById('modal-route').innerHTML = `
    <div class="modal__airport">
      <div class="modal__time">${flight.scheduledDeparture.substring(11, 16)}</div>
      <div class="modal__code">${flight.departureAirport.code}</div>
    </div>
    <div class="modal__arrow">
      <div class="modal__arrow-line">──── ✈ ────</div>
      <div>${flight.scheduledDeparture.substring(0, 10)}</div>
    </div>
    <div class="modal__airport">
      <div class="modal__time">${flight.scheduledArrival.substring(11, 16)}</div>
      <div class="modal__code">${flight.arrivalAirport.code}</div>
    </div>`;

    document.getElementById('modal-meta').innerHTML = `
    <div class="modal__meta-item">From: <strong>${flight.departureAirport.cityName}</strong></div>
    <div class="modal__meta-item">To: <strong>${flight.arrivalAirport.cityName}</strong></div>
    <div class="modal__meta-item">Price from: <strong>$${Number(flight.price).toFixed(0)}</strong></div>`;

    setTab('fares');
    document.getElementById('panel-fares').innerHTML =
        `<div class="modal__loading"><div class="spinner"></div>Loading fares…</div>`;
    document.getElementById('panel-seats').innerHTML = '';
    updateSummary();

    document.getElementById('modal-overlay').classList.add('open');
    document.body.style.overflow = 'hidden';

    loadFares(flight.flightId);
}

function closeModal(e) {
    if (e.target === document.getElementById('modal-overlay')) closeModalDirect();
}

function closeModalDirect() {
    document.getElementById('modal-overlay').classList.remove('open');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeModalDirect();
});

document.querySelectorAll('.modal__tab').forEach(btn => {
    btn.addEventListener('click', () => setTab(btn.dataset.tab));
});

function setTab(name) {
    document.querySelectorAll('.modal__tab').forEach(t => t.classList.toggle('active', t.dataset.tab === name));
    document.querySelectorAll('.modal__panel').forEach(p => p.classList.toggle('active', p.id === `panel-${name}`));
}

async function loadFares(flightId) {
    const adults = +document.getElementById('adults').value || 1;
    const children = +document.getElementById('children').value;
    const infants = +document.getElementById('infants').value;
    const passengers = {adult: adults};
    if (children > 0) passengers.child = children;
    if (infants > 0) passengers.infant = infants;

    try {
        const res = await apiFetch(`/api/v1/flights/${flightId}/fares`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({passengers, cabinClass: document.getElementById('cabinClass').value}),
        });
        const data = await res.json();
        if (!res.ok) {
            document.getElementById('panel-fares').innerHTML =
                `<p style="text-align:center;color:#dc2626;padding:2rem">${data.detail ?? 'Failed to load fares.'}</p>`;
            return;
        }
        modalState.data = data;
        renderFares(data);
        renderSeats(data.seats ?? []);
    } catch {
        document.getElementById('panel-fares').innerHTML =
            `<p style="text-align:center;color:#dc2626;padding:2rem">Could not load fares.</p>`;
    }
}

function renderFares(data) {
    const fares = data.fares ?? [];
    if (!fares.length) {
        document.getElementById('panel-fares').innerHTML =
            '<p style="text-align:center;color:var(--muted);padding:2rem">No fares available for this cabin.</p>';
        return;
    }

    document.getElementById('panel-fares').innerHTML = `<div class="fares-grid">${
        fares.map(fare => {
            const priceMap = data.farePrice?.[fare.fareId]?.prices ?? {};
            const priceRows = Object.entries(priceMap).map(([type, info]) =>
                `<div class="fare-price-row">
          <span class="fare-price-row__type">${PAX_LABELS[type] ?? type}</span>
          <span class="fare-price-row__amount">$${Number(info.priceForOnePassenger).toFixed(0)}</span>
        </div>`
            ).join('');
            return `<div class="fare-card" data-fare-id="${fare.fareId}"
                   onclick="selectFare(this,'${fare.fareId}')">
        <div class="fare-card__name">${fare.name}</div>
        <div class="fare-card__tags">
          <span class="tag tag--blue">${fare.cabinClass.toLowerCase().replace('_', ' ')}</span>
          ${fare.isBaggageIncluded ? '<span class="tag tag--green">✓ Baggage</span>' : '<span class="tag tag--muted">✕ No baggage</span>'}
          ${fare.isRefundable ? '<span class="tag tag--green">✓ Refundable</span>' : '<span class="tag tag--muted">Non-refundable</span>'}
        </div>
        <div class="fare-card__prices">${priceRows || '<span style="font-size:.8rem;color:var(--muted)">Price unavailable</span>'}</div>
      </div>`;
        }).join('')
    }</div>`;
}

function selectFare(el, fareId) {
    document.querySelectorAll('.fare-card').forEach(c => c.classList.remove('selected'));
    el.classList.add('selected');
    modalState.fareId = fareId;
    setTab('seats');
    refreshSeatsPanel();
    updateSummary();
}

function renderSeats(seats) {
    modalState.allSeats = seats;
    refreshSeatsPanel();
}

function refreshSeatsPanel() {
    const panel = document.getElementById('panel-seats');
    const {passengers, seatAssignments, activePaxKey, allSeats, fareId, data} = modalState;

    if (!allSeats?.length) {
        panel.innerHTML = '<p style="text-align:center;color:var(--muted);padding:2rem">No seat information available.</p>';
        return;
    }
    if (!fareId) {
        panel.innerHTML = '<p style="text-align:center;color:var(--muted);padding:2rem">Select a fare first.</p>';
        return;
    }
    if (!passengers.length) {
        panel.innerHTML = '<p style="text-align:center;color:var(--muted);padding:2rem">No passengers require a seat.</p>';
        return;
    }

    const selectedCabin = document.getElementById('cabinClass').value; // e.g. "economy"

    const selectedFare = data?.fares?.find(f => f.fareId === fareId);
    const fareCabin = selectedFare?.cabinClass?.toLowerCase() ?? selectedCabin;

    const takenIds = new Set(
        Object.entries(seatAssignments)
            .filter(([key]) => key !== activePaxKey)
            .map(([, v]) => v.seatId)
    );

    const pills = passengers.map(p => {
        const assigned = seatAssignments[p.label];
        const isActive = activePaxKey === p.label;
        return `<button class="pax-pill${isActive ? ' pax-pill--active' : ''}${assigned ? ' pax-pill--done' : ''}"
                    onclick="setActivePax('${p.label}')">
      ${p.label}${assigned ? ` <span class="pax-pill__seat">${assigned.number}</span>` : ''}
    </button>`;
    }).join('');

    const legend = `
    <div class="seats-legend">
      <div class="seats-legend__item"><div class="legend-dot" style="background:#d6f5e7;border:1.5px solid #86efac"></div>Available (your cabin)</div>
      <div class="seats-legend__item"><div class="legend-dot" style="background:#dbeafe;border:1.5px solid #93c5fd"></div>Held / taken</div>
      <div class="seats-legend__item"><div class="legend-dot" style="background:#fee2e2;border:1.5px solid #fca5a5"></div>Sold</div>
      <div class="seats-legend__item"><div class="legend-dot" style="background:#f5f0e8;border:1.5px solid #d6c9b0"></div>Blocked</div>
      <div class="seats-legend__item"><div class="legend-dot" style="background:#f3f4f6;border:1.5px solid #d1d5db"></div>Other cabin</div>
    </div>`;

    const groups = {window: [], middle: [], aisle: []};
    allSeats.forEach(s => {
        const key = s.type?.toLowerCase();
        if (groups[key]) groups[key].push(s);
    });

    const sections = Object.entries(groups)
        .filter(([, arr]) => arr.length)
        .map(([type, arr]) => {
            const chips = arr
                .sort((a, b) => a.number.localeCompare(b.number, undefined, {numeric: true}))
                .map(s => {
                    const isMine = seatAssignments[activePaxKey]?.seatId === s.seatId;
                    const isTakenByPax = takenIds.has(s.seatId);
                    const isRightCabin = s.cabinClass?.toLowerCase() === fareCabin;
                    const isAvailable = s.status === 'available' && isRightCabin && !isTakenByPax;

                    // Visual state
                    let visualState;
                    if (isMine) {
                        visualState = 'selected';
                    } else if (!isRightCabin) {
                        visualState = 'other-cabin';
                    } else if (isTakenByPax) {
                        visualState = 'held';
                    } else {
                        visualState = s.status;
                    }

                    const tooltip = [
                        s.number,
                        s.cabinClass ? `${s.cabinClass} cabin` : '',
                        s.hasExtraLegroom ? 'extra legroom' : '',
                        s.isExitRow ? 'exit row' : '',
                        !isRightCabin ? '(different cabin — not selectable)' : '',
                    ].filter(Boolean).join(' · ');

                    const cls = `seat-chip seat-chip--${visualState}`;
                    const clickAttr = isAvailable
                        ? `onclick="assignSeat('${s.seatId}','${s.number}')" style="cursor:pointer"`
                        : `style="cursor:not-allowed"`;

                    return `<div class="${cls}" title="${tooltip}" ${clickAttr}>${s.number}</div>`;
                }).join('');

            return `<div class="seat-section">
          <div class="seat-section__title">${SEAT_TYPE_ICONS[type] ?? ''} ${type.charAt(0).toUpperCase() + type.slice(1)}</div>
          <div class="seats-grid">${chips}</div>
        </div>`;
        }).join('');

    panel.innerHTML = `
    <div class="pax-pills">${pills}</div>
    <p class="pax-hint">Selecting seat for <strong>${activePaxKey}</strong>
      <span style="color:var(--muted);font-size:.8125rem"> · ${fareCabin} cabin</span>
    </p>
    ${legend}
    ${sections}`;
}

function setActivePax(label) {
    modalState.activePaxKey = label;
    refreshSeatsPanel();
}

function assignSeat(seatId, number) {
    const {activePaxKey, passengers} = modalState;
    modalState.seatAssignments[activePaxKey] = {seatId, number};

    const next = passengers.find(p => p.label !== activePaxKey && !modalState.seatAssignments[p.label]);
    if (next) modalState.activePaxKey = next.label;

    refreshSeatsPanel();
    updateSummary();
}

function updateSummary() {
    const {fareId, data, passengers, seatAssignments} = modalState;
    const fareName = fareId ? data?.fares?.find(f => f.fareId === fareId)?.name : null;

    const assignedCount = Object.keys(seatAssignments).length;
    const allAssigned = passengers.length > 0 && assignedCount === passengers.length;
    const ready = !!fareId && allAssigned;

    let text = '';
    if (fareName) text += `Fare: <strong>${fareName}</strong>`;
    if (assignedCount) text += (text ? ' · ' : '') + `${assignedCount}/${passengers.length} seat${assignedCount > 1 ? 's' : ''} selected`;
    if (!text) text = 'Select a fare to continue';

    document.getElementById('modal-summary').innerHTML = text;
    document.getElementById('btn-continue').disabled = !ready;
}

document.getElementById('btn-continue').addEventListener('click', () => {
    closeModalDirect();
    openPaxModal();
});


let paxForms = [];
let paxIndex = 0;

function openPaxModal() {
    const {passengers, fareId, data} = modalState;

    const infants = +document.getElementById('infants').value;
    const allPax = [...passengers];
    for (let i = 0; i < infants; i++)
        allPax.push({type: 'infant', label: infants > 1 ? `Infant ${i + 1}` : 'Infant'});

    modalState.allPax = allPax;
    paxForms = allPax.map(() => ({}));
    paxIndex = 0;

    const fare = data?.fares?.find(f => f.fareId === fareId);
    document.getElementById('pax-modal-sub').textContent =
        `${fare?.name ?? ''} · ${allPax.length} passenger${allPax.length > 1 ? 's' : ''}`;

    renderPaxTabs();
    renderPaxForm(0);
    updatePaxFooter();

    document.getElementById('pax-modal-overlay').classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closePaxModal(e) {
    if (e.target === document.getElementById('pax-modal-overlay')) closePaxModalDirect();
}

function closePaxModalDirect() {
    document.getElementById('pax-modal-overlay').classList.remove('open');
    document.body.style.overflow = '';
}

function renderPaxTabs() {
    document.getElementById('pax-tabs').innerHTML = modalState.allPax.map((p, i) =>
        `<button class="modal__tab${i === paxIndex ? ' active' : ''}"
             onclick="paxGoTo(${i})">${p.label}</button>`
    ).join('');
}

function paxGoTo(i) {
    savePaxForm(paxIndex);
    paxIndex = i;
    renderPaxTabs();
    renderPaxForm(i);
    updatePaxFooter();
}

function paxNavBack() {
    if (paxIndex > 0) paxGoTo(paxIndex - 1);
}

document.getElementById('btn-pax-next').addEventListener('click', () => {
    if (!validatePaxForm()) return;
    savePaxForm(paxIndex);
    if (paxIndex < modalState.allPax.length - 1) {
        paxGoTo(paxIndex + 1);
    } else {
        submitHold();
    }
});

function renderPaxForm(i) {
    document.getElementById('hold-error-banner')?.remove();
    const p = modalState.allPax[i];
    const saved = paxForms[i] ?? {};
    const seat = modalState.seatAssignments[p.label];

    document.getElementById('pax-modal-body').innerHTML = `
    <div class="pax-form" id="pax-form">
      ${seat ? `<div class="pax-form__seat-badge">✈ Seat <strong>${seat.number}</strong></div>` : ''}
 
      <div class="pax-form__grid">
        <div class="field">
          <label class="field__label" for="pf-firstName">First name</label>
          <input id="pf-firstName" class="field__input" type="text"
                 placeholder="John" value="${saved.firstName ?? ''}" autocomplete="given-name"/>
        </div>
        <div class="field">
          <label class="field__label" for="pf-lastName">Last name</label>
          <input id="pf-lastName" class="field__input" type="text"
                 placeholder="Smith" value="${saved.lastName ?? ''}" autocomplete="family-name"/>
        </div>
      </div>
 
      <div class="pax-form__grid pax-form__grid--3">
        <div class="field">
          <label class="field__label" for="pf-dob">Date of birth</label>
          <input id="pf-dob" class="field__input" type="date"
                 value="${saved.dateOfBirth ?? ''}" max="${new Date().toISOString().split('T')[0]}"/>
        </div>
        <div class="field">
          <label class="field__label" for="pf-gender">Gender</label>
          <div class="field__select-wrap">
            <select id="pf-gender" class="field__select">
              <option value="">— select —</option>
              <option value="male"   ${saved.gender === 'male' ? 'selected' : ''}>Male</option>
              <option value="female" ${saved.gender === 'female' ? 'selected' : ''}>Female</option>
              <option value="other"  ${saved.gender === 'other' ? 'selected' : ''}>Other</option>
            </select>
          </div>
        </div>
        <div class="field">
          <label class="field__label" for="pf-docType">Document</label>
          <div class="field__select-wrap">
            <select id="pf-docType" class="field__select">
              <option value="">— select —</option>
              <option value="passport" ${saved.docType === 'passport' ? 'selected' : ''}>Passport</option>
              <option value="id card"  ${saved.docType === 'id card' ? 'selected' : ''}>ID Card</option>
            </select>
          </div>
        </div>
      </div>
 
      <div class="pax-form__grid">
        <div class="field">
          <label class="field__label" for="pf-docNumber">Document number</label>
          <input id="pf-docNumber" class="field__input" type="text"
                 placeholder="AB1234567" value="${saved.docNumber ?? ''}"/>
        </div>
        <div class="field">
          <label class="field__label" for="pf-country">
            Issuing country <span class="optional">(2 letters, e.g. US)</span>
          </label>
          <input id="pf-country" class="field__input" type="text"
                 placeholder="US" maxlength="2" value="${saved.countryCode ?? ''}"
                 style="text-transform:uppercase"/>
        </div>
      </div>
    </div>`;

    document.getElementById('pf-country').addEventListener('input', function () {
        this.value = this.value.toUpperCase();
    });
}

function savePaxForm(i) {
    paxForms[i] = {
        firstName: document.getElementById('pf-firstName')?.value.trim(),
        lastName: document.getElementById('pf-lastName')?.value.trim(),
        dateOfBirth: document.getElementById('pf-dob')?.value,
        gender: document.getElementById('pf-gender')?.value,
        docType: document.getElementById('pf-docType')?.value,
        docNumber: document.getElementById('pf-docNumber')?.value.trim(),
        countryCode: document.getElementById('pf-country')?.value.trim().toUpperCase(),
    };
}

function validatePaxForm() {
    savePaxForm(paxIndex);
    const f = paxForms[paxIndex];

    const rules = [
        ['pf-firstName', !f.firstName, 'Required'],
        ['pf-lastName', !f.lastName, 'Required'],
        ['pf-dob', !f.dateOfBirth, 'Required'],
        ['pf-gender', !f.gender, 'Required'],
        ['pf-docType', !f.docType, 'Required'],
        ['pf-docNumber', !f.docNumber, 'Required'],
        ['pf-country', !/^[A-Z]{2}$/.test(f.countryCode ?? ''), 'Must be exactly 2 letters'],
    ];

    document.querySelectorAll('#pax-form .field--error').forEach(el => {
        el.classList.remove('field--error');
        el.querySelector('.field__error-msg')?.remove();
    });

    let firstError = null;
    rules.filter(([, invalid]) => invalid).forEach(([id, , msg]) => {
        const input = document.getElementById(id);
        if (!input) return;
        const field = input.closest('.field');
        field.classList.add('field--error');
        const msgEl = document.createElement('div');
        msgEl.className = 'field__error-msg';
        msgEl.textContent = msg;
        field.appendChild(msgEl);
        input.addEventListener('input', () => {
            field.classList.remove('field--error');
            msgEl.remove();
        }, {once: true});
        if (!firstError) firstError = field;
    });

    if (firstError) firstError.scrollIntoView({behavior: 'smooth', block: 'center'});
    return !firstError;
}

function updatePaxFooter() {
    const total = modalState.allPax?.length ?? 1;
    document.getElementById('pax-modal-summary').textContent = `Passenger ${paxIndex + 1} of ${total}`;
    document.getElementById('btn-pax-next').textContent = paxIndex === total - 1 ? 'Hold flight ✓' : 'Next →';
    document.getElementById('btn-pax-back').style.visibility = paxIndex === 0 ? 'hidden' : 'visible';
}

async function submitHold() {
    const {flightId, fareId, data, allPax, seatAssignments} = modalState;
    const priceMap = data?.farePrice?.[fareId]?.prices ?? {};

    const flights = allPax.map((p, i) => {
        const f = paxForms[i];
        const seat = seatAssignments[p.label];
        const paxPrice = priceMap[p.type];
        return {
            flightId,
            fareId,
            seatId: seat?.seatId ?? null,
            passengerType: p.type,
            price: paxPrice?.priceForOnePassenger ?? 0,
            priceHash: paxPrice?.priceHash ?? '',
            passenger: {
                firstName: f.firstName,
                lastName: f.lastName,
                dateOfBirth: f.dateOfBirth,
                gender: f.gender,
                document: {
                    type: f.docType,
                    number: f.docNumber,
                    countryCode: f.countryCode,
                },
            },
        };
    });

    const btn = document.getElementById('btn-pax-next');
    btn.disabled = true;
    btn.textContent = 'Holding…';

    try {
        const res = await apiFetch('/api/v1/flights/reservations/hold', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({flights}),
        });

        if (!res.ok) {
            const err = await res.json();
            handleHoldError(err);
            return;
        }

        closePaxModalDirect();
        showHoldSuccess();

    } catch {
        document.getElementById('pax-modal-summary').innerHTML =
            '<span style="color:#dc2626">Connection error. Please try again.</span>';
    } finally {
        btn.disabled = false;
        updatePaxFooter();
    }
}

function handleHoldError(err) {
    if (err.errors && Object.keys(err.errors).length) {
        const fieldMap = {
            firstName: 'pf-firstName',
            lastName: 'pf-lastName',
            dateOfBirth: 'pf-dob',
            gender: 'pf-gender',
            type: 'pf-docType',
            number: 'pf-docNumber',
            countryCode: 'pf-country',
        };
        let firstError = null;
        for (const [path, messages] of Object.entries(err.errors)) {
            const key = path.split('.').pop();
            const input = document.getElementById(fieldMap[key]);
            if (!input) continue;
            const field = input.closest('.field');
            field.classList.add('field--error');
            let msgEl = field.querySelector('.field__error-msg');
            if (!msgEl) {
                msgEl = document.createElement('div');
                msgEl.className = 'field__error-msg';
                field.appendChild(msgEl);
            }
            msgEl.textContent = messages[0];
            input.addEventListener('input', () => {
                field.classList.remove('field--error');
                msgEl.remove();
            }, {once: true});
            if (!firstError) firstError = field;
        }
        if (firstError) firstError.scrollIntoView({behavior: 'smooth', block: 'center'});

        const count = Object.keys(err.errors).length;
        document.getElementById('pax-modal-summary').innerHTML =
            `<span style="color:#dc2626">⚠ Please fix ${count} error${count > 1 ? 's' : ''} above</span>`;
    } else {
        const message = err.detail ?? 'Hold failed. Please try again.';

        const body = document.getElementById('pax-modal-body');
        let holdErrEl = document.getElementById('hold-error-banner');
        if (!holdErrEl) {
            holdErrEl = document.createElement('div');
            holdErrEl.id = 'hold-error-banner';
            holdErrEl.className = 'auth-error';
            holdErrEl.style.cssText = 'margin-bottom:1rem;border-radius:8px;padding:.75rem 1rem';
            body.insertAdjacentElement('afterbegin', holdErrEl);
        }
        holdErrEl.textContent = `⚠ ${message}`;
        holdErrEl.style.display = 'block';
        body.scrollTop = 0;

        document.getElementById('pax-modal-summary').innerHTML =
            `<span style="color:#dc2626">⚠ ${message}</span>`;
    }
}

function showHoldSuccess() {
    let banner = document.getElementById('success-banner');
    if (!banner) {
        banner = document.createElement('div');
        banner.id = 'success-banner';
        banner.className = 'success-banner';
        document.querySelector('.hero').insertAdjacentElement('afterend', banner);
    }
    banner.innerHTML = `
    <span>✓</span>
    <span>Flight held successfully! Check your profile for booking details.</span>
    <button onclick="this.parentElement.style.display='none'">✕</button>`;
    banner.style.display = 'flex';
    banner.scrollIntoView({behavior: 'smooth', block: 'center'});
}