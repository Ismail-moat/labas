<%--
  Created by IntelliJ IDEA.
  User: moata
  Date: 21/04/2026
  Time: 18:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard - Labas.</title>
    <link rel="stylesheet" href="../css/admin.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
</head>
<body>
<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="dashboard.jsp" class="active">Dashboard</a>
        <a href="products.html">Products</a>
        <a href="orders.html">Orders</a>
        <a href="settings.html">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="../index.html" style="color: #666; text-decoration: none;">&larr; Back to Store</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <div class="header-title">Dashboard Overview</div>
        <div class="header-user">Admin User</div>
    </header>
    <div class="content-scroll">
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-title">Total Sales</div>
                <div class="stat-value">45,200 dh</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Orders</div>
                <div class="stat-value">128</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Products</div>
                <div class="stat-value">45</div>
            </div>
            <div class="stat-card">
                <div class="stat-title">Customers</div>
                <div class="stat-value">892</div>
            </div>
        </div>

        <div class="table-container">
            <div class="table-header">
                <h3>Recent Orders</h3>
                <button class="btn-primary" onclick="window.location.href='orders.html'">View All</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Date</th>
                    <th>Amount</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>#1024</td>
                    <td>John Doe</td>
                    <td>Oct 24, 2025</td>
                    <td>890 dh</td>
                    <td><span class="status shipped">Shipped</span></td>
                </tr>
                <tr>
                    <td>#1025</td>
                    <td>Karlina Bias</td>
                    <td>Oct 25, 2025</td>
                    <td>450 dh</td>
                    <td><span class="status pending">Processing</span></td>
                </tr>
                <tr>
                    <td>#1026</td>
                    <td>William Jack</td>
                    <td>Oct 25, 2025</td>
                    <td>1,240 dh</td>
                    <td><span class="status pending">Processing</span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</main>
<script src="../js/admin.js"></script>
</body>
</html>
