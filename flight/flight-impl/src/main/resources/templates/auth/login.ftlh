<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Sign In" activePage="login" extraJs="login.js">

  <section class="auth-page">
    <div class="card auth-card">

      <div class="auth-card__logo">
        <div class="navbar__logo-icon" style="font-size:1.4rem">✈</div>
        <span class="auth-card__logo-text">Sky<span>Query</span></span>
      </div>

      <h1 class="auth-card__title">Welcome back</h1>
      <p class="auth-card__sub">Sign in to manage your bookings</p>

      <div id="auth-error" class="auth-error" style="display:none"></div>

      <div class="auth-form">
        <div class="field">
          <label class="field__label" for="email">Email</label>
          <input id="email" class="field__input" type="email" placeholder="you@example.com" autocomplete="email"/>
        </div>

        <div class="field">
          <label class="field__label" for="password">Password</label>
          <div class="password-wrap">
            <input id="password" class="field__input" type="password" placeholder="••••••••" autocomplete="current-password"/>
            <button type="button" class="password-toggle" onclick="togglePassword('password', this)">👁</button>
          </div>
        </div>

        <button id="btn-login" class="btn btn--primary" style="width:100%;margin-top:.25rem">Sign in</button>
      </div>

      <div class="auth-card__footer">
        Don't have an account? <a href="/register">Sign up</a>
      </div>

    </div>
  </section>

</@layout.base>
