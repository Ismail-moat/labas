<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String firstName = (String) session.getAttribute("firstName"); if (firstName == null) firstName = "";
    String lastName  = (String) session.getAttribute("lastName");  if (lastName  == null) lastName  = "";
    String username  = (String) session.getAttribute("username");  if (username  == null) username  = "";
    String email     = (String) session.getAttribute("email");     if (email     == null) email     = "";
    String phone     = (String) session.getAttribute("phone");     if (phone     == null) phone     = "";
    String address   = (String) session.getAttribute("address");   if (address   == null) address   = "";
    String city      = (String) session.getAttribute("city");      if (city      == null) city      = "";
    String zipCode   = (String) session.getAttribute("zipCode");   if (zipCode   == null) zipCode   = "";
    String avatarUrl = (String) session.getAttribute("avatarUrl");
%>
<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Mon Profil - Labas.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@300;400;600&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .profile-tabs { display:flex; gap:1rem; margin-bottom:2rem; border-bottom:2px solid var(--border); }
        .profile-tab  { padding:.75rem 1.5rem; cursor:pointer; font-weight:500; border-bottom:2px solid transparent; margin-bottom:-2px; transition:all 0.2s; }
        .profile-tab.active { border-bottom-color:var(--black); }
        .tab-panel { display:none; } .tab-panel.active { display:block; }
        .avatar-wrap { display:flex; align-items:center; gap:1.5rem; margin-bottom:1.5rem; }
        .avatar-img  { width:90px; height:90px; border-radius:50%; object-fit:cover; border:2px solid var(--border); }
        .avatar-placeholder { width:90px; height:90px; border-radius:50%; background:#f0f0f0; display:flex; align-items:center; justify-content:center; font-size:2.5rem; }
        .order-card  { border:1px solid var(--border); border-radius:6px; padding:1.25rem; margin-bottom:1rem; }
        .order-header{ display:flex; justify-content:space-between; align-items:center; margin-bottom:.75rem; }
        .order-status{ padding:.25rem .75rem; border-radius:20px; font-size:.8rem; font-weight:600; }
        .status-pending   { background:#fef3c7; color:#92400e; }
        .status-confirmed { background:#dbeafe; color:#1e40af; }
        .status-shipped   { background:#ede9fe; color:#5b21b6; }
        .status-delivered { background:#d1fae5; color:#065f46; }
        .status-cancelled { background:#fee2e2; color:#991b1b; }
        .order-items-list { list-style:none; padding:0; margin:.5rem 0; }
        .order-items-list li { display:flex; justify-content:space-between; font-size:.88rem; padding:.3rem 0; border-bottom:1px solid #f5f5f5; }
        .msg-success { background:#ecfdf5; color:#065f46; padding:1rem; border-radius:4px; margin-bottom:1.5rem; }
        .msg-error   { background:#fef2f2; color:#991b1b; padding:1rem; border-radius:4px; margin-bottom:1.5rem; }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="nav-logo"><a href="${pageContext.request.contextPath}/">labas.</a></div>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/catalog">BOUTIQUE</a></li>
        <li><a href="${pageContext.request.contextPath}/cart">🛒 PANIER</a></li>
    </ul>
    <div class="nav-icons">
        <span>🧑 Bonjour, <b><c:out value="<%= firstName %>"/> <c:out value="<%= lastName %>"/></b></span>
        <span class="nav-icon"><a href="${pageContext.request.contextPath}/logout">DÉCONNEXION</a></span>
    </div>
</nav>

<section class="page-section wide">
    <h2>MON COMPTE</h2>

    <c:if test="${not empty succes}">
        <div class="msg-success">✅ Profil mis à jour avec succès !</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg-error">❌ <c:out value="${error}"/></div>
    </c:if>

    <div class="profile-tabs">
        <div class="profile-tab active" onclick="showTab('tab-info', this)">Informations personnelles</div>
        <div class="profile-tab" onclick="showTab('tab-orders', this)">Commandes (${orders != null ? orders.size() : 0})</div>
    </div>

    <div id="tab-info" class="tab-panel active">

        <div class="avatar-wrap">
            <% if (avatarUrl != null && !avatarUrl.isEmpty()) { %>
                <img src="${pageContext.request.contextPath}<%= avatarUrl %>" alt="Photo de profil" class="avatar-img" />
            <% } else { %>
                <div class="avatar-placeholder">👤</div>
            <% } %>
            <div>
                <p style="font-weight:600;margin:0;">Photo de profil</p>
                <p style="font-size:.85rem;color:#888;margin:.25rem 0 0;">JPEG, PNG, WebP — max 5 Mo</p>
            </div>
        </div>

        <form class="contact-form"
              action="${pageContext.request.contextPath}/profile"
              method="post"
              enctype="multipart/form-data">

            <input type="hidden" name="_csrf_token" value="${sessionScope._csrf_token}" />

            <div style="margin-bottom:1rem;">
                <label style="font-size:.9rem;font-weight:500;">Changer la photo de profil</label>
                <input type="file" name="avatar" accept="image/jpeg,image/png,image/webp"
                       style="display:block;margin-top:.5rem;" />
            </div>

            <input type="text"  name="firstName" value="<c:out value="<%= firstName %>"/>" placeholder="Prénom" required />
            <input type="text"  name="lastName"  value="<c:out value="<%= lastName %>"/>"  placeholder="Nom de famille" required />
            <input type="text"  name="username"  value="<c:out value="<%= username %>"/>"  placeholder="Nom d'utilisateur" required />
            <input type="email" value="<c:out value="<%= email %>"/>" placeholder="Email" readonly
                   style="background:#f5f5f5;cursor:not-allowed;" />
            <input type="tel"   name="phone"   value="<c:out value="<%= phone %>"/>"   placeholder="Téléphone" />
            <input type="text"  name="address" value="<c:out value="<%= address %>"/>" placeholder="Adresse" />
            <input type="text"  name="city"    value="<c:out value="<%= city %>"/>"    placeholder="Ville" />
            <input type="text"  name="zipCode" value="<c:out value="<%= zipCode %>"/>" placeholder="Code postal" />

            <hr style="margin:1.5rem 0;" />
            <h4 style="margin-bottom:.75rem;">Changer le mot de passe</h4>
            <small style="color:#888;display:block;margin-bottom:.75rem;">
                Min. 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial.
            </small>
            <input type="password" name="newPassword" placeholder="Nouveau mot de passe (laisser vide pour ne pas changer)"
                   autocomplete="new-password" />

            <button type="submit" style="width:fit-content;">SAUVEGARDER LES MODIFICATIONS</button>
        </form>
    </div>

    <div id="tab-orders" class="tab-panel">
        <c:choose>
            <c:when test="${empty orders}">
                <p style="color:#888;padding:2rem 0;">Vous n'avez pas encore passé de commande.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <div>
                                <strong>Commande #<c:out value="${order.id}"/></strong>
                                <span style="font-size:.85rem;color:#888;margin-left:.75rem;">
                                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" type="both" dateStyle="short" timeStyle="short" />
                                </span>
                            </div>
                            <span class="order-status status-${order.status.name().toLowerCase()}">
                                <c:out value="${order.status}"/>
                            </span>
                        </div>
                        <ul class="order-items-list">
                            <c:forEach var="item" items="${order.items}">
                                <li>
                                    <span><c:out value="${item.product.name}"/> × <c:out value="${item.quantity}"/></span>
                                    <span><fmt:formatNumber value="${item.amountIncl}" type="currency" currencySymbol="€"/></span>
                                </li>
                            </c:forEach>
                        </ul>
                        <div style="text-align:right;font-weight:600;">
                            Total TTC :
                            <fmt:formatNumber value="${order.totalIncl}" type="currency" currencySymbol="€"/>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

</section>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
function showTab(id, el) {
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.profile-tab').forEach(t => t.classList.remove('active'));
    document.getElementById(id).classList.add('active');
    el.classList.add('active');
}
</script>
</body>
</html>