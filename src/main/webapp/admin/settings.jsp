<%--
  Created by IntelliJ IDEA.
  User: moata
  Date: 26/04/2026
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Settings - Labas.</title>
    <link rel="stylesheet" href="../css/admin.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; font-size: 13px; font-weight: 500; }
        .form-group input { width: 100%; max-width: 400px; padding: 12px; border: 1px solid var(--border); border-radius: 4px; font-family: 'Montserrat'; }
        .card { background: #fff; padding: 30px; border-radius: 8px; border: 1px solid var(--border); max-width: 600px; }
    </style>
</head>
<body>
<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="dashboard.jsp">Dashboard</a>
        <a href="products.jsp">Products</a>
        <a href="orders.jsp">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/users">USERS</a>
        <a href="settings.jsp" class="active">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="../index.html" style="color: #666; text-decoration: none;">&larr; Back to Store</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <div class="header-title">Store Settings</div>
        <div class="header-user">Admin User</div>
    </header>
    <div class="content-scroll">
        <div class="card">
            <h3 style="margin-bottom: 24px; font-size: 16px; font-weight: 500;">General Information</h3>
            <form onsubmit="event.preventDefault(); alert('Settings Saved!');">
                <div class="form-group">
                    <label>Store Name</label>
                    <input type="text" value="Labas." />
                </div>
                <div class="form-group">
                    <label>Contact Email</label>
                    <input type="email" value="hello@labas.com" />
                </div>
                <div class="form-group">
                    <label>Currency</label>
                    <input type="text" value="MAD (dh)" />
                </div>
                <button class="btn-primary" type="submit">Save Changes</button>
            </form>
        </div>
    </div>
</main>
<script src="../js/admin.js"></script>
</body>
</html>

