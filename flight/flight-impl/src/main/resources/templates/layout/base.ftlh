<#assign auth = isAuthenticated!false>
<#macro base pageTitle="" activePage="" extraCss="" extraJs="">
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>${pageTitle}<#if pageTitle?has_content> · </#if>SkyQuery</title>
  <link rel="preconnect" href="https://fonts.googleapis.com"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=DM+Serif+Display&display=swap"/>
  <link rel="stylesheet" href="/css/main.css"/>
  <link rel="stylesheet" href="/css/additions.css"/>
  <#if extraCss?has_content><link rel="stylesheet" href="/css/${extraCss}"/></#if>
</head>
<body>

  <nav class="navbar">
    <a class="navbar__logo" href="/">
      <div class="navbar__logo-icon">✈</div>
      Sky<span>Query</span>
    </a>

    <ul class="navbar__links">
      <li><a href="/" class="${(activePage == 'search')?then('active', '')}">Search</a></li>
      <#if auth>
        <li><a href="/account"              class="${(activePage == 'account')?then('active', '')}">Profile</a></li>
        <li><a href="/account/reservations" class="${(activePage == 'reservations')?then('active', '')}">My Tickets</a></li>
      </#if>
    </ul>

    <div class="navbar__right">
      <#if auth>
        <button class="btn btn--ghost btn--sm" id="btn-logout">Sign out</button>
      <#else>
        <a href="/login"    class="btn btn--ghost btn--sm ${(activePage == 'login')?then('active','')}">Sign in</a>
        <a href="/register" class="btn btn--primary btn--sm">Sign up</a>
      </#if>
    </div>
  </nav>

  <main><#nested/></main>

  <script src="/js/api.js"></script>
  <#if auth>
    <script src="/js/logout.js"></script>
  </#if>
  <#if extraJs?has_content><script src="/js/${extraJs}"></script></#if>
</body>
</html>
</#macro>
