<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession adminSession = request.getSession(false);
    if (adminSession == null || !"admin".equalsIgnoreCase((String) adminSession.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<%@ taglib prefix="c"   uri="jakarta.tags.core"      %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Users Management — Labas.</title>
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
        .table-header { padding: 1.2rem 1.6rem; border-bottom: 1px solid #e8e4de; }
        .table-header h3 { font-size: .85rem; letter-spacing: .1em; text-transform: uppercase; font-weight: 500; }
        table { width: 100%; border-collapse: collapse; }
        thead th { padding: .8rem 1.2rem; text-align: left; font-size: .68rem; letter-spacing: .1em; text-transform: uppercase; color: #888; border-bottom: 1px solid #e8e4de; font-weight: 500; }
        tbody td { padding: .9rem 1.2rem; font-size: .82rem; border-bottom: 1px solid #f0ede8; }
        .empty-row td { text-align: center; color: #aaa; font-style: italic; padding: 2rem; }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="active">Users</a>
        <a href="${pageContext.request.contextPath}/admin/settings">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/">&larr; Back to Store</a>
    </div>
</aside>

<main class="main-content">
    <header class="header">
        <div class="header-title">Clients Management</div>
    </header>

    <div class="content-scroll">
        <div class="table-container">
            <div class="table-header">
                <h3>All Clients</h3>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Username</th>
                    <th>Phone</th>
                    <th>City</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty clients}">
                        <c:forEach var="client" items="${clients}">
                            <tr>
                                <td><c:out value="${client.firstName}" /> <c:out value="${client.lastName}" /></td>
                                <td><c:out value="${client.username}" /></td>
                                <td><c:out value="${client.phone}" /></td>
                                <td><c:out value="${client.city}" /></td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="empty-row">
                            <td colspan="4">No clients found.</td>
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
