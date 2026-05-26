let nextCursor = null;
let cancelBookingId = null;

document.getElementById('btn-search-tickets').addEventListener('click', () => doSearchTickets(true));
document.getElementById('btn-load-more').addEventListener('click', () => doSearchTickets(false));
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        closeConfirmDirect();
        closeTicketModalDirect();
    }
});

doSearchTickets(true);

async function doSearchTickets(reset) {
    if (reset) nextCursor = null;

    const btn = document.getElementById('btn-search-tickets');
    btn.disabled = true;
    document.getElementById('btn-tickets-text').textContent = 'Loading…';

    const body = {};
    const status = document.getElementById('ticketStatus').value;
    if (status) body.status = status;
    if (nextCursor) body.nextCursor = nextCursor;

    try {
        const res = await apiFetch('/api/v1/flights/reservations/search', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(Object.keys(body).length ? body : null),
        });
        const data = await res.json();

        if (!res.ok) {
            showBanner(data.detail ?? 'Failed to load tickets.');
            return;
        }

        if (reset) document.getElementById('tickets-list').innerHTML = '';
        renderTickets(data.tickets ?? []);

        nextCursor = data.nextCursor ?? null;
        document.getElementById('load-more-wrap').style.display = nextCursor ? 'block' : 'none';
        document.getElementById('results-section').style.display = 'block';
    } catch {
        showBanner('Connection error. Please try again.');
    } finally {
        btn.disabled = false;
        document.getElementById('btn-tickets-text').textContent = 'Search';
    }
}

function renderTickets(tickets) {
    const list = document.getElementById('tickets-list');
    if (!tickets.length && !list.children.length) {
        list.innerHTML = '<p style="color:var(--muted);text-align:center;padding:3rem">No tickets found.</p>';
        return;
    }
    tickets.forEach(t => {
        const card = document.createElement('div');
        card.className = 'flight-card ticket-card';
        card.addEventListener('click', () => openTicketModal(t.id));

        const statusColor = {
            issued: 'tag--blue',
            confirmed: 'tag--green',
            cancelled: 'tag--muted',
            refunded: 'tag--muted'
        }[t.status] ?? 'tag--muted';
        const passengerName = t.passenger ? `${t.passenger.firstName} ${t.passenger.lastName}` : '—';
        const created = t.createdAt
            ? new Date(t.createdAt).toLocaleDateString('en-GB', {day: '2-digit', month: 'short', year: 'numeric'}) : '';

        card.innerHTML = `
          <div style="flex:1">
            <div style="display:flex;align-items:center;gap:.75rem;margin-bottom:.4rem">
              <span class="tag ${statusColor}">${t.status}</span>
              <span style="font-size:.8125rem;color:var(--muted)">#${t.id.substring(0, 8)}…</span>
            </div>
            <div style="font-weight:700;font-size:1rem">${passengerName}</div>
            <div style="font-size:.8125rem;color:var(--muted);margin-top:.2rem">${t.passengerType} · Booked ${created}</div>
          </div>
          <div style="text-align:right">
            <div style="font-size:1.25rem;font-weight:800;color:var(--blue)">$${Number(t.totalPrice).toFixed(0)}</div>
            <div style="font-size:.75rem;color:var(--muted);margin-top:.2rem">booking ${t.bookingId?.substring(0, 8)}…</div>
          </div>`;
        list.appendChild(card);
    });
}

async function openTicketModal(ticketId) {
    document.getElementById('ticket-modal-body').innerHTML =
        '<div class="modal__loading"><div class="spinner"></div>Loading…</div>';
    document.getElementById('btn-cancel-ticket').style.display = 'none';
    document.getElementById('ticket-modal-summary').textContent = '';
    cancelBookingId = null;

    document.getElementById('ticket-modal-overlay').classList.add('open');
    document.body.style.overflow = 'hidden';

    try {
        const res = await apiFetch(`/api/v1/flights/reservations/${ticketId}`);
        const data = await res.json();
        if (!res.ok) {
            document.getElementById('ticket-modal-body').innerHTML =
                `<p style="text-align:center;color:#dc2626;padding:2rem">${data.detail ?? 'Failed to load reservation.'}</p>`;
            return;
        }
        renderTicketDetail(data);
    } catch {
        document.getElementById('ticket-modal-body').innerHTML =
            '<p style="text-align:center;color:#dc2626;padding:2rem">Connection error.</p>';
    }
}

function renderTicketDetail(d) {
    const f = d.flight, t = d.ticket, fare = d.fare, seat = d.seat, p = t?.passenger;

    document.getElementById('ticket-modal-title').textContent = 'Reservation Details';
    document.getElementById('ticket-modal-sub').textContent = `#${t?.id?.substring(0, 8)}… · ${t?.status ?? ''}`;

    const statusColor = {
        issued: 'tag--blue',
        confirmed: 'tag--green',
        cancelled: 'tag--muted',
        refunded: 'tag--muted'
    }[t?.status] ?? 'tag--muted';

    document.getElementById('ticket-modal-body').innerHTML = `
      <div class="ticket-detail-section">
        <div class="ticket-detail-section__title">Flight</div>
        <div class="modal__route" style="justify-content:center;margin:.75rem 0">
          <div class="modal__airport">
            <div class="modal__time">${f?.scheduledDeparture?.substring(11, 16) ?? '--'}</div>
            <div class="modal__code">${f?.departureAirport?.code ?? '--'}</div>
            <div style="font-size:.7rem;color:var(--muted);margin-top:.1rem">${f?.departureAirport?.cityName ?? ''}</div>
          </div>
          <div class="modal__arrow">
            <div class="modal__arrow-line">──── ✈ ────</div>
            <div>${f?.scheduledDeparture?.substring(0, 10) ?? ''}</div>
          </div>
          <div class="modal__airport">
            <div class="modal__time">${f?.scheduledArrival?.substring(11, 16) ?? '--'}</div>
            <div class="modal__code">${f?.arrivalAirport?.code ?? '--'}</div>
            <div style="font-size:.7rem;color:var(--muted);margin-top:.1rem">${f?.arrivalAirport?.cityName ?? ''}</div>
          </div>
        </div>
      </div>
      <div class="ticket-detail-section">
        <div class="ticket-detail-section__title">Ticket</div>
        <div class="ticket-detail-grid">
          <div class="ticket-detail-row"><span>Status</span><span class="tag ${statusColor}">${t?.status ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Passenger type</span><span>${t?.passengerType ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Fare</span><span>${fare?.name ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Cabin</span><span>${fare?.cabinClass ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Baggage</span><span>${fare?.isBaggageIncluded ? '✓ Included' : '✕ Not included'}</span></div>
          <div class="ticket-detail-row"><span>Refundable</span><span>${fare?.isRefundable ? '✓ Yes' : '✕ No'}</span></div>
          <div class="ticket-detail-row"><span>Seat</span><span>${seat?.number ?? '—'} (${seat?.type ?? '—'})</span></div>
          <div class="ticket-detail-row"><span>Total price</span><strong style="color:var(--blue)">$${Number(d.totalPrice ?? 0).toFixed(2)}</strong></div>
        </div>
      </div>
      <div class="ticket-detail-section">
        <div class="ticket-detail-section__title">Passenger</div>
        <div class="ticket-detail-grid">
          <div class="ticket-detail-row"><span>Name</span><span>${p?.firstName ?? '—'} ${p?.lastName ?? ''}</span></div>
          <div class="ticket-detail-row"><span>Date of birth</span><span>${p?.dateOfBirth ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Gender</span><span>${p?.gender ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Document type</span><span>${p?.document?.type ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Document number</span><span>${p?.document?.number ?? '—'}</span></div>
          <div class="ticket-detail-row"><span>Country</span><span>${p?.document?.countryCode ?? '—'}</span></div>
        </div>
      </div>`;

    const flightId = f?.flightId;
    const fareId = fare?.fareId;
    const paxType = t?.passengerType;
    const seatId = seat?.seatId;

    const viewBtn = document.getElementById('btn-view-flight');
    if (flightId && fareId && paxType) {
        let url = `/flights/${flightId}?fareId=${fareId}&passengerType=${paxType}`;
        if (seatId) url += `&seatId=${seatId}`;
        viewBtn.href = url;
        viewBtn.style.display = 'inline-flex';
    } else {
        viewBtn.style.display = 'none';
    }

    if (['issued', 'confirmed'].includes(t?.status) && t?.bookingId) {
        cancelBookingId = t.bookingId;
        document.getElementById('btn-cancel-ticket').style.display = 'inline-flex';
    }
}

function closeTicketModal(e) {
    if (e.target === document.getElementById('ticket-modal-overlay')) closeTicketModalDirect();
}

function closeTicketModalDirect() {
    document.getElementById('ticket-modal-overlay').classList.remove('open');
    document.body.style.overflow = '';
}

function confirmCancel() {
    document.getElementById('confirm-overlay').classList.add('open');
}

function closeConfirm(e) {
    if (e.target === document.getElementById('confirm-overlay')) closeConfirmDirect();
}

function closeConfirmDirect() {
    document.getElementById('confirm-overlay').classList.remove('open');
}

async function doCancel() {
    if (!cancelBookingId) return;
    const btn = document.getElementById('btn-confirm-cancel');
    btn.disabled = true;
    btn.textContent = 'Cancelling…';

    try {
        const res = await apiFetch(`/api/v1/flights/reservations/${cancelBookingId}`, {method: 'DELETE'});
        if (res.ok || res.status === 204) {
            closeConfirmDirect();
            closeTicketModalDirect();
            showSuccessBanner('Reservation cancelled successfully.');
            doSearchTickets(true);
            return;
        }
        const err = await res.json().catch(() => ({}));
        document.getElementById('ticket-modal-summary').innerHTML =
            `<span style="color:#dc2626">⚠ ${err.detail ?? 'Cancel failed.'}</span>`;
        closeConfirmDirect();
    } catch {
        document.getElementById('ticket-modal-summary').innerHTML =
            '<span style="color:#dc2626">Connection error.</span>';
        closeConfirmDirect();
    } finally {
        btn.disabled = false;
        btn.textContent = 'Yes, cancel';
    }
}

function showBanner(message) {
    document.getElementById('error-banner-wrap').innerHTML = `
      <div class="error-banner" style="display:flex">
        <span class="error-banner__icon">⚠</span>
        <span>${message}</span>
        <button class="error-banner__close" onclick="this.closest('.error-banner').style.display='none'">✕</button>
      </div>`;
}

function showSuccessBanner(message) {
    document.getElementById('error-banner-wrap').innerHTML = `
      <div class="success-banner" style="display:flex">
        <span>✓</span><span>${message}</span>
        <button onclick="this.closest('.success-banner').style.display='none'">✕</button>
      </div>`;
}
