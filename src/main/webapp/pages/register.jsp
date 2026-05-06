<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Create Account - Labas.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .auth-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: var(--surface);
            padding: 100px 20px;
        }
        .auth-card {
            background: var(--white);
            padding: 4rem;
            width: 100%;
            max-width: 600px;
            box-shadow: var(--shadow-lg);
            border-radius: 8px;
            text-align: center;
        }
        .auth-card h2 {
            font-size: 2.5rem;
            margin-bottom: 2rem;
            letter-spacing: -0.02em;
        }
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
            text-align: left;
        }
        .form-full {
            grid-column: span 2;
        }
        .auth-card input {
            width: 100%;
            padding: 1rem;
            border: 1px solid var(--border);
            border-radius: 4px;
            font-family: var(--font-body);
            font-size: 0.9rem;
            background: var(--surface);
            transition: border-color 0.2s;
        }
        .auth-card input:focus {
            outline: none;
            border-color: var(--black);
        }
        .auth-link {
            display: inline-block;
            margin-top: 2rem;
            font-size: 0.85rem;
            color: var(--text-muted);
            font-weight: 500;
        }
        .auth-link:hover {
            color: var(--black);
        }
        .message-error {
            background: #fef2f2;
            color: #991b1b;
            padding: 1rem;
            border-radius: 4px;
            font-size: 0.85rem;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>

<nav class="navbar" id="navbar">
    <div class="nav-logo">
        <a href="${pageContext.request.contextPath}/">LABAS.</a>
    </div>
</nav>

<div class="auth-container">
    <div class="auth-card reveal">
        <h2>Create Account</h2>
        
        <c:if test="${not empty erreur}">
            <div class="message-error"><c:out value="${erreur}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <input type="hidden" name="_csrf_token" value="${sessionScope._csrf_token}" />
            <div class="form-grid">
                <input type="text" name="firstName" placeholder="First Name" required />
                <input type="text" name="lastName" placeholder="Last Name" required />
                <input type="text" name="username" placeholder="Username" required />
                <input type="tel" name="phone" placeholder="Phone Number" required />
                
                <input type="text" name="address" placeholder="Address" class="form-full" required />
                <input type="text" name="city" placeholder="City" required />
                <input type="text" name="zipCode" placeholder="Postal Code" required />
                
                <input type="email" name="email" placeholder="Email Address" class="form-full" required />
                <input type="password" name="password" class="form-full"
                       placeholder="Password (min 8 chars, 1 maj, 1 min, 1 chiffre, 1 spécial)"
                       minlength="8" required />
            </div>
            
            <button type="submit" class="btn-primary" style="width: 100%; margin-top: 2rem;">Sign Up</button>
        </form>

        <a href="${pageContext.request.contextPath}/login" class="auth-link">Already have an account? Sign in.</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
