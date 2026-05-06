<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Labas.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .auth-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: var(--surface);
            padding: 40px 20px;
        }
        .auth-card {
            background: var(--white);
            padding: 4rem;
            width: 100%;
            max-width: 480px;
            box-shadow: var(--shadow-lg);
            border-radius: 8px;
            text-align: center;
        }
        .auth-card h2 {
            font-size: 2.5rem;
            margin-bottom: 2rem;
            letter-spacing: -0.02em;
        }
        .auth-card input {
            width: 100%;
            padding: 1rem;
            margin-bottom: 1rem;
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
        .message-success {
            background: #ecfdf5;
            color: #065f46;
            padding: 1rem;
            border-radius: 4px;
            font-size: 0.85rem;
            margin-bottom: 1.5rem;
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
        <h2>Welcome Back</h2>

        <c:if test="${param.succes == '1'}">
            <div class="message-success">Account created successfully. Please log in.</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="message-error"><c:out value="${error}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="_csrf_token" value="${sessionScope._csrf_token}" />
            <input type="email" name="email" placeholder="Email Address" required />
            <input type="password" name="password" placeholder="Password" required />
            <button type="submit" class="btn-primary" style="width: 100%; margin-top: 1rem;">Sign In</button>
        </form>

        <a href="${pageContext.request.contextPath}/register" class="auth-link">Don't have an account? Create one.</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>