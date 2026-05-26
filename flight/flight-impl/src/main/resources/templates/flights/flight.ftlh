<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Flight Details" activePage="search">

  <div class="page-wrap" style="max-width:760px">

    <a href="javascript:history.back()" class="back-link">← Back</a>

    <div class="card flight-detail-card">

      <!-- ── Route header ── -->
      <div class="flight-detail-header">
        <div class="flight-detail-airline">
          <span class="flight-detail-airline__code">${flight.airline().code()}</span>
          <span class="flight-detail-airline__name">${flight.airline().name()}</span>
        </div>
        <div class="flight-detail-route">
          <div class="flight-detail-airport">
            <div class="flight-detail-airport__time">${flight.scheduledDeparture()?substring(11,16)}</div>
            <div class="flight-detail-airport__code">${flight.departureAirport().code()}</div>
            <div class="flight-detail-airport__city">${flight.departureAirport().cityName()}</div>
            <div class="flight-detail-airport__name">${flight.departureAirport().name()}</div>
          </div>
          <div class="flight-detail-arrow">
            <div class="flight-detail-arrow__line">──────── ✈ ────────</div>
            <div class="flight-detail-arrow__date">${flight.scheduledDeparture()?substring(0,10)}</div>
          </div>
          <div class="flight-detail-airport flight-detail-airport--right">
            <div class="flight-detail-airport__time">${flight.scheduledArrival()?substring(11,16)}</div>
            <div class="flight-detail-airport__code">${flight.arrivalAirport().code()}</div>
            <div class="flight-detail-airport__city">${flight.arrivalAirport().cityName()}</div>
            <div class="flight-detail-airport__name">${flight.arrivalAirport().name()}</div>
          </div>
        </div>
      </div>

      <!-- ── Details grid ── -->
      <div class="flight-detail-sections">

        <!-- Fare -->
        <div class="flight-detail-section">
          <div class="flight-detail-section__title">Fare</div>
          <div class="flight-detail-grid">
            <div class="ticket-detail-row"><span>Name</span><span>${flight.fare().name()}</span></div>
            <div class="ticket-detail-row"><span>Cabin</span><span>${flight.fare().cabinClass()}</span></div>
            <div class="ticket-detail-row"><span>Airline</span><span>${flight.fare().airlineCode()}</span></div>
            <div class="ticket-detail-row">
              <span>Baggage</span>
              <span>${flight.fare().isBaggageIncluded()?then('✓ Included', '✕ Not included')}</span>
            </div>
            <div class="ticket-detail-row">
              <span>Refundable</span>
              <span>${flight.fare().isRefundable()?then('✓ Yes', '✕ No')}</span>
            </div>
          </div>
        </div>

        <!-- Seat -->
        <#if flight.seat()??>
        <div class="flight-detail-section">
          <div class="flight-detail-section__title">Seat</div>
          <div class="flight-detail-grid">
            <div class="ticket-detail-row"><span>Number</span><span>${flight.seat().number()}</span></div>
            <div class="ticket-detail-row"><span>Type</span><span>${flight.seat().type()}</span></div>
            <div class="ticket-detail-row"><span>Cabin</span><span>${flight.seat().cabinClass()}</span></div>
            <div class="ticket-detail-row">
              <span>Extra legroom</span>
              <span>${flight.seat().hasExtraLegroom()?then('✓ Yes', '—')}</span>
            </div>
            <div class="ticket-detail-row">
              <span>Exit row</span>
              <span>${flight.seat().isExitRow()?then('✓ Yes', '—')}</span>
            </div>
            <div class="ticket-detail-row">
              <span>Status</span>
              <#assign seatStatus = flight.seat().status()?string>
              <span class="tag <#if seatStatus == 'available'>tag--green<#elseif seatStatus == 'held'>tag--blue<#else>tag--muted</#if>">
                ${seatStatus}
              </span>
            </div>
          </div>
        </div>
        </#if>

        <!-- Airplane -->
        <div class="flight-detail-section">
          <div class="flight-detail-section__title">Aircraft</div>
          <div class="flight-detail-grid">
            <div class="ticket-detail-row"><span>Manufacturer</span><span>${flight.airplaneType().manufacturer()}</span></div>
            <div class="ticket-detail-row"><span>Model</span><span>${flight.airplaneType().model()}</span></div>
            <div class="ticket-detail-row"><span>Code</span><span>${flight.airplaneType().code()}</span></div>
          </div>
        </div>

        <!-- Price -->
        <div class="flight-detail-section flight-detail-section--price">
          <div class="flight-detail-section__title">Price</div>
          <div class="flight-detail-price">
            <div class="flight-detail-price__label">${flight.farePrice().passengerType()} · per passenger</div>
            <div class="flight-detail-price__amount">$${flight.farePrice().priceForOnePassenger()?string["0.00"]}</div>
          </div>
        </div>

      </div>
    </div>
  </div>

</@layout.base>
