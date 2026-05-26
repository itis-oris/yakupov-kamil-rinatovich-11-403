<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="My Tickets" activePage="reservations" extraJs="reservations.js">

  <section class="hero hero--compact">
    <div class="hero__inner">
      <h1 class="hero__title">My Tickets</h1>
      <p class="hero__sub">View and manage your flight reservations</p>

      <div class="search-card">
        <div class="search-row">
          <div class="field">
            <label class="field__label" for="ticketStatus">Status</label>
            <div class="field__select-wrap">
              <select id="ticketStatus" class="field__select">
                <option value="">All</option>
                <#list ticketStatuses as s>
                  <option value="${s.getValue()}">${s.getValue()?cap_first}</option>
                </#list>
              </select>
            </div>
          </div>
          <button id="btn-search-tickets" class="btn btn--primary btn--lg">
            <span id="btn-tickets-text">Search</span>
          </button>
        </div>
      </div>
    </div>
  </section>

  <div id="error-banner-wrap"></div>

  <div class="page-wrap" id="results-section" style="display:none">
    <div id="tickets-list"></div>
    <div id="load-more-wrap" style="text-align:center;margin-top:1.5rem;display:none">
      <button id="btn-load-more" class="btn btn--outline">Load more</button>
    </div>
  </div>

  <!-- Ticket detail modal -->
  <div id="ticket-modal-overlay" class="modal-overlay" onclick="closeTicketModal(event)">
    <div class="modal">
      <div class="modal__head">
        <div class="modal__top">
          <div>
            <div style="font-weight:700;font-size:1rem" id="ticket-modal-title">Reservation details</div>
            <div id="ticket-modal-sub" style="font-size:.8125rem;color:var(--muted);margin-top:.15rem"></div>
          </div>
          <button class="modal__close" onclick="closeTicketModalDirect()">✕</button>
        </div>
      </div>
      <div class="modal__body" id="ticket-modal-body">
        <div class="modal__loading"><div class="spinner"></div>Loading…</div>
      </div>
      <div class="modal__footer">
        <div id="ticket-modal-summary" class="modal__selection-summary"></div>
        <a id="btn-view-flight" class="btn btn--outline" style="display:none" target="_blank">View flight</a>
        <button id="btn-cancel-ticket" class="btn btn--danger" style="display:none"
                onclick="confirmCancel()">Cancel reservation</button>
      </div>
    </div>
  </div>

  <!-- Confirm cancel modal -->
  <div id="confirm-overlay" class="modal-overlay" onclick="closeConfirm(event)">
    <div class="modal" style="max-width:400px">
      <div class="modal__head">
        <div class="modal__top">
          <div style="font-weight:700">Cancel reservation</div>
          <button class="modal__close" onclick="closeConfirmDirect()">✕</button>
        </div>
      </div>
      <div class="modal__body">
        <p style="color:var(--muted);font-size:.9375rem;line-height:1.6">
          Are you sure you want to cancel this reservation? This action cannot be undone.
        </p>
      </div>
      <div class="modal__footer" style="justify-content:flex-end;gap:.5rem">
        <button class="btn btn--ghost" onclick="closeConfirmDirect()">No, keep it</button>
        <button id="btn-confirm-cancel" class="btn btn--danger" onclick="doCancel()">Yes, cancel</button>
      </div>
    </div>
  </div>

</@layout.base>
