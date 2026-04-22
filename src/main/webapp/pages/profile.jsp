<%@ page contentType="text/html;charset=UTF-8" %>

<%--
    SÉCURITÉ : Si l'utilisateur n'est pas connecté, on le redirige vers la page de login.
    session.getAttribute("userId") est null si personne n'est connecté.
--%>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return; // Important : on arrête l'exécution ici
    }

    // Récupérer les informations depuis la session
    // Ces valeurs ont été stockées par LoginServlet après la connexion
    String firstName = (String) session.getAttribute("firstName");
    String lastName  = (String) session.getAttribute("lastName");
    String username  = (String) session.getAttribute("username");
    String email     = (String) session.getAttribute("email");
    String phone     = (String) session.getAttribute("phone");
    String address   = (String) session.getAttribute("address");
    String city      = (String) session.getAttribute("city");
    String zipCode   = (String) session.getAttribute("zipCode");

    // Si une valeur est null (ex: l'utilisateur n'a pas rempli son profil),
    // on affiche une chaîne vide plutôt que "null"
    if (firstName == null) firstName = "";
    if (lastName  == null) lastName  = "";
    if (username  == null) username  = "";
    if (phone     == null) phone     = "";
    if (address   == null) address   = "";
    if (city      == null) city      = "";
    if (zipCode   == null) zipCode   = "";
%>

<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Mon Profil - Labas.</title>
    <link rel="stylesheet" href="../css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@300;400;600&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
</head>
<body>

<!-- BARRE DE NAVIGATION -->
<nav class="navbar">
    <div class="nav-logo">
        <a href="../index.html">labas.</a>
    </div>

    <ul class="nav-links">
        <li><a href="catalog.html">BOUTIQUE</a></li>
        <li><a href="cart.html">🛒 PANIER</a></li>
    </ul>

    <div class="nav-icons">
        <span>
            🧑 Bonjour, <b><%= firstName %> <%= lastName %></b>
        </span>
        <span class="nav-icon">
            <%-- Lien vers LogoutServlet --%>
            <a href="${pageContext.request.contextPath}/logout">DÉCONNEXION</a>
        </span>
    </div>
</nav>

<!-- CONTENU PRINCIPAL -->
<section class="page-section wide">

    <h2>MON COMPTE</h2>

    <%--
        ✅ Message de succès après mise à jour du profil
        Envoyé par ProfileServlet avec ?succes=1
    --%>
    <% if ("1".equals(request.getParameter("succes"))) { %>
        <p style="color: green; margin-bottom: 1rem;">
            ✅ Profil mis à jour avec succès !
        </p>
    <% } %>

    <%--
        ❌ Message d'erreur envoyé par ProfileServlet
    --%>
    <% if (request.getAttribute("erreur") != null) { %>
        <p style="color: red; margin-bottom: 1rem;">
            ❌ <%= request.getAttribute("erreur") %>
        </p>
    <% } %>

    <div class="profile-grid">

        <!-- SIDEBAR DE NAVIGATION -->
        <div class="profile-sidebar">
            <ul>
                <li><a href="#" class="active">Informations personnelles</a></li>
                <li><a href="#">Historique des commandes</a></li>
                <li><a href="#">Liste de souhaits</a></li>
                <li><a href="#">Mes adresses</a></li>
            </ul>
        </div>

        <!-- FORMULAIRE DE PROFIL -->
        <div class="profile-content">

            <h3>Informations personnelles</h3>

            <%--
                FORMULAIRE DE MISE À JOUR
                action pointe vers ProfileServlet (POST /profile)
                Chaque name= doit correspondre à getParameter() dans ProfileServlet
            --%>
            <form class="contact-form"
                  action="${pageContext.request.contextPath}/profile"
                  method="post">

                <input type="text"
                       name="firstName"
                       value="<%= firstName %>"
                       placeholder="Prénom"
                       required />

                <input type="text"
                       name="lastName"
                       value="<%= lastName %>"
                       placeholder="Nom de famille"
                       required />

                <input type="text"
                       name="username"
                       value="<%= username %>"
                       placeholder="Nom d'utilisateur"
                       required />

                <%-- L'email est affiché mais non modifiable (readonly) --%>
                <input type="email"
                       value="<%= email %>"
                       placeholder="Email"
                       readonly
                       style="background: #f5f5f5; cursor: not-allowed;" />

                <input type="tel"
                       name="phone"
                       value="<%= phone %>"
                       placeholder="Téléphone" />

                <input type="text"
                       name="address"
                       value="<%= address %>"
                       placeholder="Adresse" />

                <input type="text"
                       name="city"
                       value="<%= city %>"
                       placeholder="Ville" />

                <input type="text"
                       name="zipCode"
                       value="<%= zipCode %>"
                       placeholder="Code postal" />

                <button type="submit" style="width: fit-content;">
                    SAUVEGARDER LES MODIFICATIONS
                </button>

            </form>

        </div>

    </div>

</section>

<script src="../js/app.js"></script>
</body>
</html>