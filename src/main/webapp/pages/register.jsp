<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Créer un compte - Labas.</title>
    <link rel="stylesheet" href="../css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@300;400;600&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
</head>
<body>

<!-- BARRE DE NAVIGATION -->
<nav class="navbar" id="navbar">
    <div class="nav-logo">
        <a href="../index.html">labas.</a>
    </div>
    <div class="nav-icons">
        <span class="nav-icon">
            <a href="../index.html">← RETOUR ACCUEIL</a>
        </span>
    </div>
</nav>

<!-- SECTION D'INSCRIPTION -->
<section class="auth-section">
    <div class="auth-container">

        <h2>CRÉER UN COMPTE</h2>

        <%--
            ❌ Message d'erreur envoyé par RegisterServlet
            (ex: l'email est déjà utilisé)
        --%>
        <% if (request.getAttribute("erreur") != null) { %>
            <p style="color: red; margin-bottom: 1rem;">
                ❌ <%= request.getAttribute("erreur") %>
            </p>
        <% } %>

        <%--
            FORMULAIRE D'INSCRIPTION
            Chaque name= doit correspondre EXACTEMENT à request.getParameter("...") dans RegisterServlet
        --%>
        <form action="${pageContext.request.contextPath}/register" method="post">

            <%-- Informations personnelles --%>
            <input type="text"
                   name="firstName"
                   placeholder="Prénom"
                   required />

            <input type="text"
                   name="lastName"
                   placeholder="Nom de famille"
                   required />

            <input type="text"
                   name="username"
                   placeholder="Nom d'utilisateur"
                   required />

            <input type="tel"
                   name="phone"
                   placeholder="Téléphone (ex: 06 12 34 56 78)"
                   required />

            <%-- Adresse --%>
            <input type="text"
                   name="address"
                   placeholder="Adresse (ex: 12 rue de Paris)"
                   required />

            <input type="text"
                   name="city"
                   placeholder="Ville"
                   required />

            <input type="text"
                   name="zipCode"
                   placeholder="Code postal"
                   required />

            <%-- Identifiants de connexion --%>
            <input type="email"
                   name="email"
                   placeholder="Adresse Email"
                   required />

            <input type="password"
                   name="password"
                   placeholder="Mot de passe"
                   minlength="6"
                   required />

            <button type="submit">CRÉER MON COMPTE</button>

        </form>

        <p>
            Déjà un compte ?
            <a href="login.jsp">Se connecter</a>
        </p>

    </div>
</section>

<script src="../js/app.js"></script>
</body>
</html>
