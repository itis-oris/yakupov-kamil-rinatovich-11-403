<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Admin Panel" activePage="account" extraCss="admin.css">

    <div class="admin-layout">

        <!-- ── Sidebar ── -->
        <nav class="admin-sidebar">
            <div class="admin-sidebar__title">Admin Panel</div>
            <div class="admin-sidebar__user">${account.username()}</div>

            <div class="admin-sidebar__group">
                <div class="admin-sidebar__group-title">Reference</div>
                <button class="admin-nav-btn active" data-section="airlines">Airlines</button>
                <button class="admin-nav-btn" data-section="airplanes">Airplanes</button>
            </div>

            <div class="admin-sidebar__group">
                <div class="admin-sidebar__group-title">Flights</div>
                <button class="admin-nav-btn" data-section="flights">Flights</button>
                <button class="admin-nav-btn" data-section="inventories">Inventories</button>
            </div>

            <div class="admin-sidebar__group">
                <div class="admin-sidebar__group-title">Pricing</div>
                <button class="admin-nav-btn" data-section="fares">Fares</button>
                <button class="admin-nav-btn" data-section="rules">Pricing Rules</button>
                <button class="admin-nav-btn" data-section="adjustments">Adjustments</button>
            </div>

            <div class="admin-sidebar__group">
                <div class="admin-sidebar__group-title">Routes</div>
                <button class="admin-nav-btn" data-section="routes">Route Search</button>
            </div>
        </nav>

        <!-- ── Main content ── -->
        <div class="admin-main">

            <!-- ════════════════ AIRLINES ════════════════ -->
            <div class="admin-section active" id="section-airlines">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Airlines</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-airline-create')">+ Add airline
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Country code</label>
                        <div class="field__select-wrap">
                            <select id="al-country" class="field__select field__select--sm">
                                <option value="">All types</option>
                                <#list countries as c>
                                    <option value="${c.code()}">${c.name()} (${c.code()})</option>
                                </#list>
                            </select>
                        </div>
                    </div>

                    <div class="field">
                        <label class="field__label">Active</label>
                        <div class="field__select-wrap">
                            <select id="al-active" class="field__select field__select--sm">
                                <option value="">All</option>
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('airlines',1)">Search</button>
                </div>

                <div id="airlines-table-wrap" class="admin-table-wrap"></div>
                <div id="airlines-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ AIRPLANES ════════════════ -->
            <div class="admin-section" id="section-airplanes">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Airplanes</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-airplane-create')">+ Add
                        airplane
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Airline code</label>
                        <input id="ap-airline" class="field__input field__input--sm" placeholder="SU" maxlength="2"/>
                    </div>
                    <div class="field">
                        <label class="field__label">Airplane type</label>
                        <div class="field__select-wrap">
                            <select id="ap-type" class="field__select field__select--sm">
                                <option value="">All types</option>
                                <#list airplaneTypes as t>
                                    <option value="${t.code()}">${t.manufacturer()} ${t.model()} (${t.code()})</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('airplanes',1)">Search</button>
                </div>

                <div id="airplanes-table-wrap" class="admin-table-wrap"></div>
                <div id="airplanes-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ FLIGHTS ════════════════ -->
            <div class="admin-section" id="section-flights">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Flights</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-flight-create')">+ Add flight
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Route ID</label>
                        <input id="fl-route" class="field__input field__input--sm" placeholder="UUID"/>
                    </div>
                    <div class="field">
                        <label class="field__label">Status</label>
                        <div class="field__select-wrap">
                            <select id="fl-status" class="field__select field__select--sm">
                                <option value="">All</option>
                                <#list flightStatuses as s>
                                    <option value="${s.getValue()}">${s.getValue()?cap_first}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('flights',1)">Search</button>
                </div>

                <div id="flights-table-wrap" class="admin-table-wrap"></div>
                <div id="flights-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ INVENTORIES ════════════════ -->
            <div class="admin-section" id="section-inventories">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Flight Inventories</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-inventory-create')">+ Add
                        inventory
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Flight ID</label>
                        <input id="inv-flight" class="field__input field__input--sm" placeholder="UUID"/>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('inventories',1)">Search</button>
                </div>

                <div id="inventories-table-wrap" class="admin-table-wrap"></div>
                <div id="inventories-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ FARES ════════════════ -->
            <div class="admin-section" id="section-fares">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Fares</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-fare-create')">+ Add fare
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Airline code</label>
                        <input id="fa-airline" class="field__input field__input--sm" placeholder="SU" maxlength="2"/>
                    </div>
                    <div class="field">
                        <label class="field__label">Cabin class</label>
                        <div class="field__select-wrap">
                            <select id="fa-cabin" class="field__select field__select--sm">
                                <option value="">All</option>
                                <#list cabinClasses as c>
                                    <option value="${c.getValue()}">${c.getValue()?cap_first}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('fares',1)">Search</button>
                </div>

                <div id="fares-table-wrap" class="admin-table-wrap"></div>
                <div id="fares-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ PRICING RULES ════════════════ -->
            <div class="admin-section" id="section-rules">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Pricing Rules</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-rule-create')">+ Add rule
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Fare ID</label>
                        <input id="ru-fare" class="field__input field__input--sm" placeholder="UUID"/>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('rules',1)">Search</button>
                </div>

                <div id="rules-table-wrap" class="admin-table-wrap"></div>
                <div id="rules-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ ADJUSTMENTS ════════════════ -->
            <div class="admin-section" id="section-adjustments">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Pricing Adjustments</h2>
                    <button class="btn btn--primary btn--sm" onclick="openModal('modal-adj-create')">+ Add adjustment
                    </button>
                </div>

                <div class="admin-filter-bar">
                    <div class="field">
                        <label class="field__label">Flight ID</label>
                        <input id="adj-flight" class="field__input field__input--sm" placeholder="UUID"/>
                    </div>
                    <div class="field">
                        <label class="field__label">Fare ID</label>
                        <input id="adj-fare" class="field__input field__input--sm" placeholder="UUID"/>
                    </div>
                    <button class="btn btn--outline btn--sm" onclick="loadSection('adjustments',1)">Search</button>
                </div>

                <div id="adjustments-table-wrap" class="admin-table-wrap"></div>
                <div id="adjustments-pagination" class="admin-pagination"></div>
            </div>

            <!-- ════════════════ ROUTES ════════════════ -->
            <div class="admin-section" id="section-routes">
                <div class="admin-section__header">
                    <h2 class="admin-section__title">Routes</h2>
                </div>

                <!-- Sub-tabs -->
                <div class="admin-subtabs">
                    <button class="admin-subtab active" data-tab="rt-search"  onclick="switchRouteTab('search')">Search</button>
                    <button class="admin-subtab"        data-tab="rt-nofly"   onclick="switchRouteTab('nofly')">Without active flights</button>
                    <button class="admin-subtab"        data-tab="rt-conflict" onclick="switchRouteTab('conflict')">Conflicting statuses</button>
                </div>

                <!-- Search tab -->
                <div id="rt-search" class="admin-subtab-panel active">
                    <div class="admin-filter-bar">
                        <div class="field">
                            <label class="field__label">Airline code</label>
                            <input id="rt-airline" class="field__input field__input--sm" placeholder="SU"/>
                        </div>
                        <div class="field">
                            <label class="field__label">Dep. airport</label>
                            <div class="field__select-wrap">
                                <select id="rt-dep" class="field__select field__select--sm">
                                    <option value="">All types</option>
                                    <#list airports as a>
                                        <option value="${a.code()}">${a.name()} (${a.code()})</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="field">
                            <label class="field__label">Arr. airport</label>
                            <div class="field__select-wrap">
                                <select id="rt-arr" class="field__select field__select--sm">
                                    <option value="">All types</option>
                                    <#list airports as a>
                                        <option value="${a.code()}">${a.name()} (${a.code()})</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="field">
                            <label class="field__label">Active</label>
                            <div class="field__select-wrap">
                                <select id="rt-active" class="field__select field__select--sm">
                                    <option value="">All</option>
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </select>
                            </div>
                        </div>
                        <button class="btn btn--outline btn--sm" onclick="loadRoutes()">Search</button>
                    </div>
                    <div id="routes-table-wrap" class="admin-table-wrap"></div>
                </div>

                <!-- Without active flights tab -->
                <div id="rt-nofly" class="admin-subtab-panel">
                    <div class="admin-filter-bar" style="align-items:center">
                        <p style="color:var(--muted);font-size:.875rem;margin:0">
                            Active routes that currently have no scheduled, on-time, delayed, boarding or departed flights.
                        </p>
                        <button class="btn btn--outline btn--sm" onclick="loadRoutesWithoutFlights()">Load</button>
                    </div>
                    <div id="routes-nofly-wrap" class="admin-table-wrap"></div>
                </div>

                <!-- Conflicting statuses tab -->
                <div id="rt-conflict" class="admin-subtab-panel">
                    <div class="admin-filter-bar">
                        <div class="field">
                            <label class="field__label">Date</label>
                            <input id="rt-conflict-date" class="field__input field__input--sm" type="date"/>
                        </div>
                        <button class="btn btn--outline btn--sm" onclick="loadConflictingRoutes()">Load</button>
                    </div>
                    <p style="color:var(--muted);font-size:.8125rem;padding:0 0 .75rem">
                        Routes that have both a <strong>scheduled</strong> and a <strong>cancelled</strong> flight on the same day.
                    </p>
                    <div id="routes-conflict-wrap" class="admin-table-wrap"></div>
                </div>
            </div>

        </div><!-- /admin-main -->
    </div><!-- /admin-layout -->


    <!-- ══════════════════ MODALS ══════════════════════════════════════════════ -->

    <!-- Create Airline -->
    <div id="modal-airline-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Airline</div>
                    <button class="modal__close" onclick="closeModal('modal-airline-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-airline-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Code (2 chars, A-Z0-9)</label>
                    <input id="al-c-code" class="field__input" placeholder="SU" maxlength="2"/></div>
                <div class="field"><label class="field__label">Name</label>
                    <input id="al-c-name" class="field__input" placeholder="Aeroflot"/></div>
                <div class="field"><label class="field__label">Country code</label>
                    <div class="field__select-wrap">
                        <select id="al-c-country" class="field__select">
                            <#list countries as c>
                                <option value="${c.code()}">${c.name()} (${c.code()})</option></#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-airline-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createAirline()">Create</button>
            </div>
        </div>
    </div>

    <!-- Create Airplane -->
    <div id="modal-airplane-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Airplane</div>
                    <button class="modal__close" onclick="closeModal('modal-airplane-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-airplane-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Number</label>
                    <input id="ap-c-number" class="field__input" placeholder="VP-BQA"/></div>
                <div class="field"><label class="field__label">Airplane type</label>
                    <div class="field__select-wrap"><select id="ap-c-type" class="field__select">
                            <#list airplaneTypes as t>
                                <option value="${t.code()}">${t.manufacturer()} ${t.model()} — ${t.totalSeats()} seats
                                (${t.code()})</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Airline code</label>
                    <input id="ap-c-airline" class="field__input" placeholder="SU" maxlength="2"/></div>
                <div class="field"><label class="field__label">Manufactured year</label>
                    <input id="ap-c-year" class="field__input" type="number" placeholder="2020" min="1900" max="2100"/>
                </div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-airplane-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createAirplane()">Create</button>
            </div>
        </div>
    </div>

    <!-- Create Flight -->
    <div id="modal-flight-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Flight</div>
                    <button class="modal__close" onclick="closeModal('modal-flight-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-flight-create" class="auth-error" style="display:none"></div>
                <div class="field">
                    <label class="field__label">Route</label>
                    <input id="fl-c-route" class="field__input" placeholder="UUID"/>
                </div>
                <div class="field"><label class="field__label">Airplane type code</label>
                    <div class="field__select-wrap"><select id="fl-c-aptype" class="field__select">
                            <#list airplaneTypes as t>
                                <option value="${t.code()}">${t.manufacturer()} ${t.model()} (${t.code()}
                                )</option></#list>
                        </select></div>
                </div>
                <div class="field">
                    <label class="field__label">Airplane ID</label>
                    <input id="fl-c-airplane" class="field__input" placeholder="UUID"/>
                </div>
                <div class="field"><label class="field__label">Scheduled departure (ISO)</label>
                    <input id="fl-c-dep" class="field__input" type="datetime-local"/></div>
                <div class="field"><label class="field__label">Scheduled arrival (ISO)</label>
                    <input id="fl-c-arr" class="field__input" type="datetime-local"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-flight-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createFlight()">Create</button>
            </div>
        </div>
    </div>

    <!-- Update Flight -->
    <div id="modal-flight-update" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Update Flight</div>
                    <button class="modal__close" onclick="closeModal('modal-flight-update')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-flight-update" class="auth-error" style="display:none"></div>
                <input type="hidden" id="fl-u-id"/>
                <div class="field"><label class="field__label">Status</label>
                    <div class="field__select-wrap"><select id="fl-u-status" class="field__select">
                            <option value="">— no change —</option>
                            <#list flightStatuses as s>
                                <option value="${s.getValue()}">${s.getValue()?cap_first}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Airplane ID (optional)</label>
                    <input id="fl-u-airplane" class="field__input" placeholder="UUID or leave blank"/></div>
                <div class="field"><label class="field__label">Actual departure (ISO)</label>
                    <input id="fl-u-adep" class="field__input" type="datetime-local"/></div>
                <div class="field"><label class="field__label">Actual arrival (ISO)</label>
                    <input id="fl-u-aarr" class="field__input" type="datetime-local"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-flight-update')">Cancel</button>
                <button class="btn btn--primary" onclick="updateFlight()">Save</button>
            </div>
        </div>
    </div>

    <!-- Create Inventory -->
    <div id="modal-inventory-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Inventory</div>
                    <button class="modal__close" onclick="closeModal('modal-inventory-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-inventory-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Flight ID</label>
                    <input id="inv-c-flight" class="field__input" placeholder="UUID"/></div>
                <div class="field"><label class="field__label">Cabin class</label>
                    <div class="field__select-wrap"><select id="inv-c-cabin" class="field__select">
                            <#list cabinClasses as c>
                                <option value="${c.getValue()}">${c.getValue()?cap_first}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Price</label>
                    <input id="inv-c-price" class="field__input" type="number" step="0.01" placeholder="0.00"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-inventory-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createInventory()">Create</button>
            </div>
        </div>
    </div>

    <!-- Update Inventory -->
    <div id="modal-inventory-update" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Update Inventory Price</div>
                    <button class="modal__close" onclick="closeModal('modal-inventory-update')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-inventory-update" class="auth-error" style="display:none"></div>
                <input type="hidden" id="inv-u-id"/>
                <div class="field"><label class="field__label">New price</label>
                    <input id="inv-u-price" class="field__input" type="number" step="0.01"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-inventory-update')">Cancel</button>
                <button class="btn btn--primary" onclick="updateInventory()">Save</button>
            </div>
        </div>
    </div>

    <!-- Create Fare -->
    <div id="modal-fare-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Fare</div>
                    <button class="modal__close" onclick="closeModal('modal-fare-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-fare-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Airline code</label>
                    <input id="fa-c-airline" class="field__input" placeholder="SU" maxlength="2"/></div>
                <div class="field"><label class="field__label">Cabin class</label>
                    <div class="field__select-wrap"><select id="fa-c-cabin" class="field__select">
                            <#list cabinClasses as c>
                                <option value="${c.getValue()}">${c.getValue()?cap_first}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Name</label>
                    <input id="fa-c-name" class="field__input" placeholder="Economy Flex"/></div>
                <div class="field" style="display:flex;gap:1.5rem">
                    <label style="display:flex;align-items:center;gap:.4rem;font-size:.875rem;cursor:pointer">
                        <input type="checkbox" id="fa-c-baggage"/> Baggage included</label>
                    <label style="display:flex;align-items:center;gap:.4rem;font-size:.875rem;cursor:pointer">
                        <input type="checkbox" id="fa-c-refund"/> Refundable</label>
                </div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-fare-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createFare()">Create</button>
            </div>
        </div>
    </div>

    <!-- Create Pricing Rule -->
    <div id="modal-rule-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Pricing Rule</div>
                    <button class="modal__close" onclick="closeModal('modal-rule-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-rule-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Fare ID</label>
                    <input id="ru-c-fare" class="field__input" placeholder="UUID"/></div>
                <div class="field"><label class="field__label">Passenger type</label>
                    <div class="field__select-wrap"><select id="ru-c-pax" class="field__select">
                            <#list passengerTypes as p>
                                <option value="${p.getValue()}">${p.getValue()?cap_first}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Multiplier</label>
                    <input id="ru-c-mult" class="field__input" type="number" step="0.01" placeholder="1.00"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-rule-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createRule()">Create</button>
            </div>
        </div>
    </div>

    <!-- Update Pricing Rule -->
    <div id="modal-rule-update" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Update Pricing Rule</div>
                    <button class="modal__close" onclick="closeModal('modal-rule-update')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-rule-update" class="auth-error" style="display:none"></div>
                <input type="hidden" id="ru-u-id"/>
                <div class="field"><label class="field__label">Multiplier</label>
                    <input id="ru-u-mult" class="field__input" type="number" step="0.01"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-rule-update')">Cancel</button>
                <button class="btn btn--primary" onclick="updateRule()">Save</button>
            </div>
        </div>
    </div>

    <!-- Create Adjustment -->
    <div id="modal-adj-create" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Create Pricing Adjustment</div>
                    <button class="modal__close" onclick="closeModal('modal-adj-create')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-adj-create" class="auth-error" style="display:none"></div>
                <div class="field"><label class="field__label">Flight ID</label>
                    <input id="adj-c-flight" class="field__input" placeholder="UUID"/></div>
                <div class="field"><label class="field__label">Fare ID</label>
                    <input id="adj-c-fare" class="field__input" placeholder="UUID"/></div>
                <div class="field"><label class="field__label">Type</label>
                    <div class="field__select-wrap"><select id="adj-c-type" class="field__select">
                            <#list pricingAdjTypes as t>
                                <option value="${t.getValue()}">${t.getValue()}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Value</label>
                    <input id="adj-c-value" class="field__input" type="number" step="0.01" placeholder="10.00"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-adj-create')">Cancel</button>
                <button class="btn btn--primary" onclick="createAdjustment()">Create</button>
            </div>
        </div>
    </div>

    <!-- Update Adjustment -->
    <div id="modal-adj-update" class="modal-overlay">
        <div class="modal admin-modal">
            <div class="modal__head">
                <div class="modal__top">
                    <div style="font-weight:700">Update Adjustment</div>
                    <button class="modal__close" onclick="closeModal('modal-adj-update')">✕</button>
                </div>
            </div>
            <div class="modal__body">
                <div id="err-adj-update" class="auth-error" style="display:none"></div>
                <input type="hidden" id="adj-u-id"/>
                <div class="field"><label class="field__label">Type</label>
                    <div class="field__select-wrap"><select id="adj-u-type" class="field__select">
                            <#list pricingAdjTypes as t>
                                <option value="${t.getValue()}">${t.getValue()}</option></#list>
                        </select></div>
                </div>
                <div class="field"><label class="field__label">Value</label>
                    <input id="adj-u-value" class="field__input" type="number" step="0.01"/></div>
            </div>
            <div class="modal__footer">
                <button class="btn btn--ghost" onclick="closeModal('modal-adj-update')">Cancel</button>
                <button class="btn btn--primary" onclick="updateAdjustment()">Save</button>
            </div>
        </div>
    </div>

    <!-- Success toast -->
    <div id="admin-toast" class="admin-toast" style="display:none"></div>

    <script src="/js/admin.js"></script>
</@layout.base>
