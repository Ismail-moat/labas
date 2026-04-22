<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession adminSession = request.getSession(false);
    if (adminSession == null || !"admin".equalsIgnoreCase((String) adminSession.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<%@ taglib prefix="c"   uri="jakarta.tags.core"      %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"       %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Products Management — Labas.</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body { display: flex; font-family: 'Montserrat', sans-serif; background: #f7f5f2; color: #1a1a1a; min-height: 100vh; }
        .sidebar { width: 220px; min-height: 100vh; background: #111; display: flex; flex-direction: column; padding: 2rem 1.5rem; flex-shrink: 0; }
        .sidebar-logo { font-family: 'Cormorant Garamond', serif; font-size: 1.4rem; color: #fff; letter-spacing: .15em; margin-bottom: 2.5rem; }
        .sidebar-nav { display: flex; flex-direction: column; gap: .4rem; flex: 1; }
        .sidebar-nav a { color: #aaa; text-decoration: none; font-size: .78rem; letter-spacing: .1em; text-transform: uppercase; padding: .6rem .8rem; border-radius: 4px; transition: background .2s, color .2s; }
        .sidebar-nav a:hover, .sidebar-nav a.active { background: #222; color: #fff; }
        .sidebar-footer a { color: #555; font-size: .72rem; text-decoration: none; }
        .sidebar-footer a:hover { color: #aaa; }
        .main-content { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
        .header { padding: 1.6rem 2rem; background: #fff; border-bottom: 1px solid #e8e4de; display: flex; align-items: center; justify-content: space-between; }
        .header-title { font-family: 'Cormorant Garamond', serif; font-size: 1.5rem; font-weight: 600; }
        .content-scroll { padding: 2rem; overflow-y: auto; display: flex; flex-direction: column; gap: 2rem; }
        .table-container { background: #fff; border: 1px solid #e8e4de; border-radius: 6px; overflow: hidden; }
        .table-header { padding: 1.2rem 1.6rem; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #e8e4de; }
        .table-header h3 { font-size: .85rem; letter-spacing: .1em; text-transform: uppercase; font-weight: 500; }
        table { width: 100%; border-collapse: collapse; }
        thead th { padding: .8rem 1.2rem; text-align: left; font-size: .68rem; letter-spacing: .1em; text-transform: uppercase; color: #888; border-bottom: 1px solid #e8e4de; font-weight: 500; }
        tbody td { padding: .9rem 1.2rem; font-size: .82rem; border-bottom: 1px solid #f0ede8; }
        tbody tr:last-child td { border-bottom: none; }
        tbody tr:hover { background: #faf9f7; }
        .btn-primary { background: #111; color: #fff; border: none; padding: .5rem 1.1rem; font-size: .72rem; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; border-radius: 3px; font-family: 'Montserrat', sans-serif; transition: background .2s; }
        .btn-primary:hover { background: #333; }
        .empty-row td { text-align: center; color: #aaa; font-style: italic; padding: 2rem; }
        .product-img { width: 40px; height: 40px; object-fit: cover; border-radius: 4px; }
        .actions { display: flex; gap: 0.5rem; }
        .btn-action { background: #e8e4de; color: #111; padding: 0.3rem 0.6rem; text-decoration: none; font-size: 0.7rem; border-radius: 3px; cursor: pointer; }
        .btn-action:hover { background: #d0c9c0; }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products" class="active">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/settings">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/">&larr; Back to Store</a>
    </div>
</aside>

<main class="main-content">
    <header class="header">
        <div class="header-title">Products Management</div>
    </header>

    <div class="content-scroll">
        <div class="table-container">
            <div class="table-header">
                <h3>All Products</h3>
                <button class="btn-primary">Add New Product</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty products}">
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty product.imageUrl}">
                                            <img src="${product.imageUrl}" class="product-img" alt="product" />
                                        </c:when>
                                        <c:otherwise>
                                            <div style="width:40px;height:40px;background:#eee;border-radius:4px;"></div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><c:out value="${product.name}" /></td>
                                <td><fmt:formatNumber value="${product.price}" pattern="#,##0.00" /> dh</td>
                                <td><c:out value="${product.stockQty}" /></td>
                                <td class="actions">
                                    <a href="#" class="btn-action">Edit</a>
                                    <a href="#" class="btn-action" style="color: red;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="empty-row">
                            <td colspan="5">No products found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

</body>
</html>
