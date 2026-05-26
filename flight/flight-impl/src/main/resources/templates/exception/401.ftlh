<#import "/layout/base.ftlh" as layout>
<@layout.base pageTitle="401 · Unauthorized">
  <div class="error-page">
    <div class="error-card">
      <div class="error-icon">🔒</div>
      <div class="error-code">401</div>
      <h1 class="error-title">Unauthorized</h1>
      <p class="error-subtitle">You need to be signed in to access this page.</p>
      <div class="error-actions">
        <a href="/login"    class="btn btn--primary">Sign in</a>
        <a href="/register" class="btn btn--ghost">Create account</a>
      </div>
    </div>
  </div>
</@layout.base>
