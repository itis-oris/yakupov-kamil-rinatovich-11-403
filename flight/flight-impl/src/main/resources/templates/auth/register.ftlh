<#import "/layout/base.ftlh" as layout>

<@layout.base pageTitle="Sign Up" activePage="register" extraJs="register.js">

  <section class="auth-page">
    <div class="card auth-card">

      <div class="auth-card__logo">
        <div class="navbar__logo-icon" style="font-size:1.4rem">✈</div>
        <span class="auth-card__logo-text">Sky<span>Query</span></span>
      </div>

      <h1 class="auth-card__title">Create account</h1>
      <p class="auth-card__sub">Join SkyQuery and start booking flights</p>

      <div id="auth-error"   class="auth-error"   style="display:none"></div>
      <div id="auth-success" class="auth-success" style="display:none"></div>

      <div class="auth-form">
        <div class="field" id="field-username">
          <label class="field__label" for="username">Username</label>
          <input id="username" class="field__input" type="text" placeholder="johndoe" autocomplete="username"/>
        </div>

        <div class="field" id="field-email">
          <label class="field__label" for="email">Email</label>
          <input id="email" class="field__input" type="email" placeholder="you@example.com" autocomplete="email"/>
        </div>

        <div class="field" id="field-password">
          <label class="field__label" for="password">Password</label>
          <div class="password-wrap">
            <input id="password" class="field__input" type="password" placeholder="Min 8 characters" autocomplete="new-password"/>
            <button type="button" class="password-toggle" onclick="togglePassword('password', this)">👁</button>
          </div>
        </div>

        <button id="btn-register" class="btn btn--primary" style="width:100%;margin-top:.25rem">Create account</button>
      </div>

      <div class="auth-card__footer">
        Already have an account? <a href="/login">Sign in</a>
      </div>

    </div>
  </section>

</@layout.base>
