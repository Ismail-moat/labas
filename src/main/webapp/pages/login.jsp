<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Connexion - Labas.</title>
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

<!-- SECTION DE CONNEXION -->
<section class="auth-section">
    <div class="auth-container">

        <h2>CONNEXION</h2>

        <%--
            ✅ Message de succès après inscription
            On vérifie si l'URL contient ?succes=1 (envoyé par RegisterServlet)
        --%>
        <% if ("1".equals(request.getParameter("succes"))) { %>
            <p style="color: green; margin-bottom: 1rem;">
                ✅ Compte créé avec succès ! Vous pouvez maintenant vous connecter.
            </p>
        <% } %>

        <%--
            ❌ Message d'erreur envoyé par LoginServlet
            request.getAttribute("erreur") est rempli si le login a échoué
        --%>
        <% if (request.getAttribute("erreur") != null) { %>
            <p style="color: red; margin-bottom: 1rem;">
                ❌ <%= request.getAttribute("erreur") %>
            </p>
        <% } %>

        <!-- FORMULAIRE DE CONNEXION -->
        <%--
            action="${pageContext.request.contextPath}/login" → pointe vers LoginServlet
            method="post" → envoie les données de façon sécurisée
        --%>
        <form action="${pageContext.request.contextPath}/login" method="post">

            <input type="email"
                   name="email"
                   placeholder="Adresse Email"
                   required />

            <input type="password"
                   name="password"
                   placeholder="Mot de passe"
                   required />

            <button type="submit">SE CONNECTER</button>

        </form>

        <p>
            Pas encore de compte ?
            <a href="register.jsp">Créer un compte</a>
        </p>

    </div>
</section>

<script src="../js/app.js"></script>
</body>
</html>