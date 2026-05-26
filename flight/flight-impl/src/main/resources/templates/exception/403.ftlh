<#import "/layout/base.ftlh" as layout>
<@layout.base pageTitle="403 · Forbidden">
  <div class="error-page">
    <div class="error-card">
      <div class="error-icon">🚫</div>
      <div class="error-code">403</div>
      <h1 class="error-title">Access Denied</h1>
      <p class="error-subtitle">You don't have permission to view this page.</p>
      <p class="error-hint">If you think this is a mistake, contact the administrator.</p>
      <div class="error-actions">
        <button class="btn btn--ghost" onclick="history.back()">← Go back</button>
        <a href="/" class="btn btn--primary">Go to homepage</a>
      </div>
    </div>
  </div>
</@layout.base>
