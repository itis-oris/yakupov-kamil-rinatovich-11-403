let ticketsCursor = null;
let activeStatus = '';
let cancelBookingId = null;

loadTickets(true);

document.getElementById('btn-tickets-more').addEventListener('click', () => loadTickets(false));

async function loadTickets(reset) {
    if (reset) {
        ticketsCursor = null;
        document.getElementById('tickets-list').innerHTML = '';
    }

    try {
        const res = await fetch('/api/v1/flights/reservations/search', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                status: activeStatus || null,
                nextCursor: ticketsCursor,
            }),
        });
        const data = await res.json();
        renderTickets(data.tickets ?? []);
        ticketsCursor = data.nextCursor ?? null;
        document.getElementById('tickets-load-more').style.display = ticketsCursor ? 'block' : 'none';
    } catch {
        document.getElementById('tickets-list').innerHTML =
            '<p style="color:#dc2626;text-align:center;padding:2rem">Failed to load tickets.</p>';
    }
}

document.querySelectorAll('.filter-pill').forEach(pill => {
    pill.addEventListener('click', () => {
        document.querySelectorAll('.filter-pill').forEach(p => p.classList.remove('filter-pill--active'));
        pill.classList.add('filter-pill--active');
        activeStatus = pill.dataset.status;
        loadTickets(true);
    });
});

function renderTickets(tickets) {
    const list = document.getElementById('tickets-list');

    if (!tickets.length && !list.children.length) {
        list.innerHTML = `
      <div class="empty-state">
        <div class="empty-state__icon">🎫</div>
        <div class="empty-state__title">No tickets found</div>
        <p>Your booked flights will appear here.</p>
      </div>`;
        return;
    }

    tickets.forEach(t => {
        const card = document.createElement('div');
        card.className = 'ticket-card';
        card.addEventListener('click', () => openTicketModal(t.id));
        card.innerHTML = `
      <div>
        <div class="ticket-card__route">
          ${t.passenger.firstName} ${t.passenger.lastName}
          <span class="ticket-card__arrow">·</span>
          <span class="status-pill status-pill--${t.status}">${t.status}</span>
          <span class="status-pill" style="background:var(--border);color:var(--muted)">${t.passengerType}</span>
        </div>
        <div class="ticket-card__meta">
          <span>${new Date(t.createdAt).toLocaleDateString('en-GB', {day: 'numeric', month: 'short', year: 'numeric'})}</span>
          <span>Booking #${t.bookingId.substring(0, 8)}…</span>
        </div>
      </div>
      <div class="ticket-card__right">
        <div class="ticket-card__price">$${Number(t.totalPrice).toFixed(0)}</div>
      </div>`;
        list.appendChild(card);
    });
}

async function openTicketModal(ticketId) {
    const body = document.getElementById('ticket-modal-body');
    body.innerHTML = '<div class="modal__loading"><div class="spinner"></div>Loading…</div>';
    document.getElementById('ticket-modal-status').innerHTML = '';
    document.getElementById('btn-cancel-ticket').style.display = 'none';
    document.getElementById('ticket-modal-overlay').classList.add('open');
    document.body.style.overflow = 'hidden';

    try {
        const res = await fetch(`/api/v1/flights/reservations/${ticketId}`);
        const data = await res.json();
        if (!res.ok) {
            body.innerHTML = `<p style="color:#dc2626;padding:2rem;text-align:center">${data.detail ?? 'Failed to load ticket.'}</p>`;
            return;
        }
        renderTicketDetail(data);
    } catch {
        body.innerHTML = '<p style="color:#dc2626;padding:2rem;text-align:center">Connection error.</p>';
    }
}

function renderTicketDetail(data) {
    const {ticket, flight, fare, seat, totalPrice} = data;
    const body = document.getElementById('ticket-modal-body');

    document.getElementById('ticket-modal-title').textContent =
        `Ticket #${ticket.id.substring(0, 8)}…`;

    document.getElementById('ticket-modal-status').innerHTML =
        `<span class="status-pill status-pill--${ticket.status}">${ticket.status}</span>`;

    const canCancel = ticket.status === 'issued' || ticket.status === 'confirmed';
    const cancelBtn = document.getElementById('btn-cancel-ticket');
    cancelBtn.style.display = canCancel ? 'inline-flex' : 'none';
    cancelBtn.onclick = () => openCancelConfirm(ticket.bookingId);

    body.innerHTML = `
    <div class="ticket-detail">

      <#-- Flight route block -->
      <div>
        <div class="ticket-detail__section-title">Flight</div>
        <div class="ticket-detail__flight">
          <div class="ticket-detail__airport">
            <div class="ticket-detail__time">${flight.scheduledDeparture.substring(11, 16)}</div>
            <div class="ticket-detail__code">${flight.departureAirport.code}</div>
          </div>
          <div class="ticket-detail__line">──── ✈ ────<br/>
            <span style="font-size:.7rem">${flight.scheduledDeparture.substring(0, 10)}</span>
          </div>
          <div class="ticket-detail__airport">
            <div class="ticket-detail__time">${flight.scheduledArrival.substring(11, 16)}</div>
            <div class="ticket-detail__code">${flight.arrivalAirport.code}</div>
          </div>
        </div>
        <div style="display:flex;gap:1.5rem;margin-top:.6rem;font-size:.8125rem;color:var(--muted)">
          <span>From: <strong style="color:var(--text)">${flight.departureAirport.cityName}</strong></span>
          <span>To: <strong style="color:var(--text)">${flight.arrivalAirport.cityName}</strong></span>
        </div>
      </div>

      <div>
        <div class="ticket-detail__section-title">Passenger</div>
        ${detailRow('Name', `${ticket.passenger.firstName} ${ticket.passenger.lastName}`)}
        ${detailRow('Date of birth', ticket.passenger.dateOfBirth)}
        ${detailRow('Gender', ticket.passenger.gender)}
        ${detailRow('Document', `${ticket.passenger.document.type} ${ticket.passenger.document.number} (${ticket.passenger.document.countryCode})`)}
        ${detailRow('Type', ticket.passengerType)}
      </div>

      <div>
        <div class="ticket-detail__section-title">Fare & Seat</div>
        ${detailRow('Fare', fare.name)}
        ${detailRow('Cabin', fare.cabinClass.toLowerCase().replace('_', ' '))}
        ${detailRow('Baggage', fare.isBaggageIncluded ? '✓ Included' : '✕ Not included')}
        ${detailRow('Refundable', fare.isRefundable ? '✓ Yes' : '✕ No')}
        ${seat ? detailRow('Seat', `${seat.number} · ${seat.type}${seat.hasExtraLegroom ? ' · extra legroom' : ''}`) : ''}
      </div>

      <div>
        <div class="ticket-detail__section-title">Price</div>
        ${detailRow('Total', `$${Number(totalPrice).toFixed(2)}`)}
        ${detailRow('Booking ID', ticket.bookingId.substring(0, 8) + '…')}
        ${detailRow('Booked on', new Date(ticket.createdAt).toLocaleDateString('en-GB', {
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    }))}
      </div>

    </div>`;
}

function detailRow(label, value) {
    return `<div class="ticket-detail__row">
    <span class="ticket-detail__label">${label}</span>
    <span class="ticket-detail__value">${value ?? '—'}</span>
  </div>`;
}

function closeTicketModal(e) {
    if (e.target === document.getElementById('ticket-modal-overlay')) closeTicketModalDirect();
}

function closeTicketModalDirect() {
    document.getElementById('ticket-modal-overlay').classList.remove('open');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        closeTicketModalDirect();
        closeCancelConfirmDirect();
    }
});

function openCancelConfirm(bookingId) {
    cancelBookingId = bookingId;
    document.getElementById('cancel-confirm-overlay').classList.add('open');
}

function closeCancelConfirm(e) {
    if (e.target === document.getElementById('cancel-confirm-overlay')) closeCancelConfirmDirect();
}

function closeCancelConfirmDirect() {
    document.getElementById('cancel-confirm-overlay').classList.remove('open');
}

document.getElementById('btn-confirm-cancel').addEventListener('click', async () => {
    const btn = document.getElementById('btn-confirm-cancel');
    btn.disabled = true;
    btn.textContent = 'Cancelling…';

    try {
        const res = await fetch(`/api/v1/flights/reservations/${cancelBookingId}`, {method: 'DELETE'});
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            alert(err.detail ?? 'Cancel failed.');
            return;
        }
        closeCancelConfirmDirect();
        closeTicketModalDirect();
        loadTickets(true);   // refresh list
    } catch {
        alert('Connection error.');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Yes, cancel';
    }
});

document.getElementById('btn-save-username').addEventListener('click', async () => {
    const input = document.getElementById('input-username');
    const feedback = document.getElementById('username-feedback');
    const newName = input.value.trim();
    const btn = document.getElementById('btn-save-username');

    if (!newName) {
        showUsernameFeedback('Username cannot be empty.', false);
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Saving…';

    try {
        const res = await fetch('/account', {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: newName}),
        });
        const data = await res.json();

        if (!res.ok) {
            showUsernameFeedback(data.detail ?? 'Update failed.', false);
            return;
        }

        document.getElementById('display-username').textContent = data.username;
        showUsernameFeedback('Username updated!', true);
    } catch {
        showUsernameFeedback('Connection error.', false);
    } finally {
        btn.disabled = false;
        btn.textContent = 'Save';
    }
});

function showUsernameFeedback(msg, ok) {
    const el = document.getElementById('username-feedback');
    el.textContent = msg;
    el.className = `username-feedback username-feedback--${ok ? 'ok' : 'err'}`;
    el.classList.remove('hidden');
    setTimeout(() => el.classList.add('hidden'), 3500);
}
