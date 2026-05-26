<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Search Flights" activePage="search">

  <section class="hero">
    <div class="hero__inner">
      <h1 class="hero__title">Find your next flight</h1>
      <p class="hero__sub">Search hundreds of routes and compare fares instantly</p>

      <div class="search-card">

        <!-- Row 1: airports + dates -->
        <div class="search-row">
          <div class="field">
            <label class="field__label" for="cityCodeFrom">From</label>
            <div class="field__select-wrap">
              <select id="cityCodeFrom" class="field__select" required>
                <option value="">— select city —</option>
                <#list cities as c>
                  <option value="${c.code()}">${c.name()} (${c.code()})</option>
                </#list>
              </select>
            </div>
          </div>

          <button type="button" id="btn-swap" class="swap-btn" title="Swap">⇄</button>

          <div class="field">
            <label class="field__label" for="cityCodeTo">To</label>
            <div class="field__select-wrap">
              <select id="cityCodeTo" class="field__select" required>
                <option value="">— select city —</option>
                <#list cities as c>
                  <option value="${c.code()}">${c.name()} (${c.code()})</option>
                </#list>
              </select>
            </div>
          </div>

          <div class="field">
            <label class="field__label" for="depDate">Departure</label>
            <input id="depDate" class="field__input" type="date" required/>
          </div>

          <div class="field">
            <label class="field__label" for="arrDate">Arrival <span class="optional">(optional)</span></label>
            <input id="arrDate" class="field__input" type="date"/>
          </div>
        </div>

        <!-- Row 2: passengers + cabin + sort + submit -->
        <div class="search-row search-row--bottom">

          <div class="field">
            <label class="field__label" for="adults">Adults (12+)</label>
            <div class="counter">
              <button type="button" class="counter__btn" data-target="adults" data-delta="-1" data-min="1">−</button>
              <input id="adults" class="field__input counter__input" type="number" value="1" min="1" max="9" readonly/>
              <button type="button" class="counter__btn" data-target="adults" data-delta="1" data-max="9">+</button>
            </div>
          </div>

          <div class="field">
            <label class="field__label" for="children">Children (2–11)</label>
            <div class="counter">
              <button type="button" class="counter__btn" data-target="children" data-delta="-1" data-min="0">−</button>
              <input id="children" class="field__input counter__input" type="number" value="0" min="0" max="9" readonly/>
              <button type="button" class="counter__btn" data-target="children" data-delta="1" data-max="9">+</button>
            </div>
          </div>

          <div class="field">
            <label class="field__label" for="infants">Infants (&lt;2)</label>
            <div class="counter">
              <button type="button" class="counter__btn" data-target="infants" data-delta="-1" data-min="0">−</button>
              <input id="infants" class="field__input counter__input" type="number" value="0" min="0" max="9" readonly/>
              <button type="button" class="counter__btn" data-target="infants" data-delta="1" data-max="9">+</button>
            </div>
          </div>

          <div class="field">
            <label class="field__label" for="cabinClass">Cabin</label>
            <div class="field__select-wrap">
              <select id="cabinClass" class="field__select">
                <#list cabinClasses as cc>
                  <option value="${cc.getValue()}">${cc.getValue()?replace("_"," ")?lower_case?cap_first}</option>
                </#list>
              </select>
            </div>
          </div>

          <div class="field">
            <label class="field__label" for="orderType">Sort by</label>
            <div class="field__select-wrap">
              <select id="orderType" class="field__select">
                <option value="">Default</option>
                <#list orderTypes as ot>
                  <option value="${ot.getValue()}">${ot.getValue()}</option>
                </#list>
              </select>
            </div>
          </div>

          <button id="btn-search" class="btn btn--primary btn--lg">
            <span id="btn-search-text">Search</span>
          </button>
        </div>

      </div>
    </div>
  </section>

  <!-- Results -->
  <div class="page-wrap" id="results-section" style="display:none">
    <div class="results-layout">

      <!-- Filter sidebar -->
      <aside id="filter-sidebar" style="display:none">
        <div class="card filter-panel">
          <div class="filter-section">
            <div class="filter-section__title">Price</div>
            <div class="price-range">
              <input id="priceMin" class="field__input" type="number" placeholder="Min"/>
              <input id="priceMax" class="field__input" type="number" placeholder="Max"/>
            </div>
          </div>
          <div class="filter-section">
            <div class="filter-section__title">Options</div>
            <label class="filter-checkbox"><input type="checkbox" id="baggage"/> Baggage included</label>
            <label class="filter-checkbox"><input type="checkbox" id="refundable"/> Refundable</label>
          </div>
          <div class="filter-section">
            <div class="filter-section__title">Departure time</div>
            <div class="field__select-wrap">
              <select id="depTime" class="field__select">
                <option value="">Any</option>
                <#list timeTypes as tt>
                  <option value="${tt.getValue()}">${tt.getValue()?cap_first} (${tt.start}–${tt.end})</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="filter-section">
            <div class="filter-section__title">Airlines</div>
            <div id="filter-airlines"></div>
          </div>
          <div class="filter-section__apply">
            <button id="btn-apply-filters" class="btn btn--primary w-full">Apply</button>
          </div>
        </div>
      </aside>

      <!-- Flight list -->
      <div>
        <div id="results-list"></div>
        <div id="load-more-wrap" style="text-align:center;margin-top:1.5rem;display:none">
          <button id="btn-load-more" class="btn btn--outline">Load more</button>
        </div>
      </div>

    </div>
  </div>

  <!-- ══ FLIGHT MODAL ══════════════════════════════════════ -->
  <div id="modal-overlay" class="modal-overlay" onclick="closeModal(event)">
    <div class="modal">

      <div class="modal__head">
        <div class="modal__top">
          <div id="modal-route" class="modal__route"></div>
          <button class="modal__close" onclick="closeModalDirect()">✕</button>
        </div>
        <div id="modal-meta" class="modal__meta"></div>

        <div class="modal__tabs">
          <button class="modal__tab active" data-tab="fares">Fares</button>
          <button class="modal__tab" data-tab="seats">Seats</button>
        </div>
      </div>

      <div class="modal__body">
        <div id="panel-fares" class="modal__panel active"></div>
        <div id="panel-seats" class="modal__panel"></div>
      </div>

      <div class="modal__footer">
        <div id="modal-summary" class="modal__selection-summary">Select a fare to continue</div>
        <button id="btn-continue" class="btn btn--primary" disabled>Continue →</button>
      </div>

    </div>
  </div>

  <!-- ══ PASSENGER DETAILS MODAL ═══════════════════════════ -->
  <div id="pax-modal-overlay" class="modal-overlay" onclick="closePaxModal(event)">
    <div class="modal modal--wide">

      <div class="modal__head">
        <div class="modal__top">
          <div>
            <div style="font-weight:700;font-size:1rem">Passenger details</div>
            <div id="pax-modal-sub" style="font-size:.8125rem;color:var(--muted);margin-top:.15rem"></div>
          </div>
          <button class="modal__close" onclick="closePaxModalDirect()">✕</button>
        </div>
        <div class="modal__tabs" id="pax-tabs"></div>
      </div>

      <div class="modal__body" id="pax-modal-body"></div>

      <div class="modal__footer">
        <div id="pax-modal-summary" class="modal__selection-summary"></div>
        <div style="display:flex;gap:.5rem">
          <button id="btn-pax-back" class="btn btn--outline" onclick="paxNavBack()">← Back</button>
          <button id="btn-pax-next" class="btn btn--primary">Next →</button>
        </div>
      </div>

    </div>
  </div>

  <footer class="footer">
    <div class="footer__inner">
      <div class="footer__brand">
        <span class="footer__logo">Sky<span>Query</span></span>
        <p>Your smart flight search engine. Compare fares across hundreds of routes, filter by what matters to you, and book with confidence.</p>
      </div>
      <div class="footer__col">
        <div class="footer__heading">Features</div>
        <ul>
          <li>Real-time flight search</li>
          <li>Flexible date options</li>
          <li>Cabin class selection</li>
          <li>Seat map preview</li>
        </ul>
      </div>
      <div class="footer__col">
        <div class="footer__heading">Company</div>
        <ul>
          <li><a href="#">About</a></li>
          <li><a href="#">Privacy policy</a></li>
          <li><a href="#">Terms of use</a></li>
          <li><a href="#">Contact</a></li>
        </ul>
      </div>
    </div>
    <div class="footer__bottom">© 2025 SkyQuery. All rights reserved.</div>
  </footer>

  <div id="search-meta" data-auth="${isAuthenticated?c}" style="display:none"></div>
  <script src="/js/search.js"></script>
</@layout.base>
