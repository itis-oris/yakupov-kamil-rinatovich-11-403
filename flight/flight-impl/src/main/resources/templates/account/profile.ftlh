<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Profile" activePage="account" extraJs="profile.js">

  <div class="page-wrap">
    <div class="profile-layout">

      <div class="card profile-card">
        <div class="profile-card__avatar">
          <div class="profile-avatar">${account.username()?substring(0,1)?upper_case}</div>
          <div>
            <div class="profile-card__name">${account.username()}</div>
            <div class="profile-card__email">${account.email()}</div>
          </div>
        </div>

        <div style="margin-top:1.5rem">
          <div id="view-mode">
            <div class="profile-info-row">
              <span class="profile-info-row__label">Username</span>
              <span class="profile-info-row__value">${account.username()}</span>
            </div>
            <div class="profile-info-row">
              <span class="profile-info-row__label">Email</span>
              <span class="profile-info-row__value">${account.email()}</span>
            </div>
            <button class="btn btn--outline" style="margin-top:1rem" onclick="showEdit()">Edit username</button>
          </div>

          <div id="edit-mode" style="display:none">
            <div id="edit-error"   class="auth-error"   style="display:none;margin-bottom:.75rem"></div>
            <div id="edit-success" class="auth-success" style="display:none;margin-bottom:.75rem"></div>
            <div class="field">
              <label class="field__label" for="new-username">New username</label>
              <input id="new-username" class="field__input" type="text"
                     value="${account.username()}" placeholder="Enter new username"/>
            </div>
            <div style="display:flex;gap:.5rem;margin-top:.85rem">
              <button id="btn-save-username" class="btn btn--primary" onclick="saveUsername()">Save</button>
              <button class="btn btn--ghost" onclick="hideEdit()">Cancel</button>
            </div>
          </div>
        </div>
      </div>

      <div class="card profile-actions-card">
        <div class="profile-actions__title">Quick actions</div>

        <a href="/account/reservations" class="profile-action-item">
          <div class="profile-action-item__icon">🎫</div>
          <div>
            <div class="profile-action-item__name">My Tickets</div>
            <div class="profile-action-item__desc">View and manage your flight reservations</div>
          </div>
          <div class="profile-action-item__arrow">→</div>
        </a>

        <a href="/" class="profile-action-item">
          <div class="profile-action-item__icon">✈</div>
          <div>
            <div class="profile-action-item__name">Search Flights</div>
            <div class="profile-action-item__desc">Find and book new flights</div>
          </div>
          <div class="profile-action-item__arrow">→</div>
        </a>

        <#if isAdmin!false>
        <a href="/admin" class="profile-action-item" style="border-color:#fbbf24">
          <div class="profile-action-item__icon">⚙️</div>
          <div>
            <div class="profile-action-item__name">Admin Panel</div>
            <div class="profile-action-item__desc">Manage flights, fares and pricing</div>
          </div>
          <div class="profile-action-item__arrow">→</div>
        </a>
        </#if>
      </div>

    </div>
  </div>

</@layout.base>
