<%--
    Admin Dashboard — Labas E-Commerce
    Servlet doit placer dans request :
      - totalSales       (Double)
      - orderCount       (Integer)
      - productCount     (Integer)
      - customerCount    (Integer)
      - recentOrders     (List<OrderDTO>)  — chaque OrderDTO : id, customerName, createdAt, totalIncl, status
      - adminName        (String)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- ── Guard : accès réservé aux admins connectés ── --%>
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
    <title>Admin Dashboard — Labas.</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <style>
        /* ── fallback inline styles so the page renders without admin.css ── */
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body { display: flex; font-family: 'Montserrat', sans-serif; background: #f7f5f2; color: #1a1a1a; min-height: 100vh; }

        /* sidebar */
        .sidebar { width: 220px; min-height: 100vh; background: #111; display: flex; flex-direction: column; padding: 2rem 1.5rem; flex-shrink: 0; }
        .sidebar-logo { font-family: 'Cormorant Garamond', serif; font-size: 1.4rem; color: #fff; letter-spacing: .15em; margin-bottom: 2.5rem; }
        .sidebar-nav { display: flex; flex-direction: column; gap: .4rem; flex: 1; }
        .sidebar-nav a { color: #aaa; text-decoration: none; font-size: .78rem; letter-spacing: .1em; text-transform: uppercase; padding: .6rem .8rem; border-radius: 4px; transition: background .2s, color .2s; }
        .sidebar-nav a:hover, .sidebar-nav a.active { background: #222; color: #fff; }
        .sidebar-footer a { color: #555; font-size: .72rem; text-decoration: none; }
        .sidebar-footer a:hover { color: #aaa; }

        /* main */
        .main-content { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
        .header { padding: 1.6rem 2rem; background: #fff; border-bottom: 1px solid #e8e4de; display: flex; align-items: center; justify-content: space-between; }
        .header-title { font-family: 'Cormorant Garamond', serif; font-size: 1.5rem; font-weight: 600; }
        .header-user  { font-size: .78rem; color: #666; letter-spacing: .05em; }
        .content-scroll { padding: 2rem; overflow-y: auto; display: flex; flex-direction: column; gap: 2rem; }

        /* stats */
        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.2rem; }
        .stat-card { background: #fff; border: 1px solid #e8e4de; border-radius: 6px; padding: 1.4rem 1.6rem; }
        .stat-title { font-size: .7rem; letter-spacing: .12em; text-transform: uppercase; color: #888; margin-bottom: .6rem; }
        .stat-value { font-family: 'Cormorant Garamond', serif; font-size: 2rem; font-weight: 600; }

        /* table */
        .table-container { background: #fff; border: 1px solid #e8e4de; border-radius: 6px; overflow: hidden; }
        .table-header { padding: 1.2rem 1.6rem; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #e8e4de; }
        .table-header h3 { font-size: .85rem; letter-spacing: .1em; text-transform: uppercase; font-weight: 500; }
        table { width: 100%; border-collapse: collapse; }
        thead th { padding: .8rem 1.2rem; text-align: left; font-size: .68rem; letter-spacing: .1em; text-transform: uppercase; color: #888; border-bottom: 1px solid #e8e4de; font-weight: 500; }
        tbody td { padding: .9rem 1.2rem; font-size: .82rem; border-bottom: 1px solid #f0ede8; }
        tbody tr:last-child td { border-bottom: none; }
        tbody tr:hover { background: #faf9f7; }

        /* status badges */
        .status { display: inline-block; padding: .25rem .7rem; border-radius: 20px; font-size: .68rem; letter-spacing: .08em; text-transform: uppercase; font-weight: 500; }
        .status.pending    { background: #fef3cd; color: #856404; }
        .status.confirmed  { background: #d1ecf1; color: #0c5460; }
        .status.shipped    { background: #d4edda; color: #155724; }
        .status.delivered  { background: #c3e6cb; color: #1a5c2a; }
        .status.cancelled  { background: #f8d7da; color: #721c24; }

        /* button */
        .btn-primary { background: #111; color: #fff; border: none; padding: .5rem 1.1rem; font-size: .72rem; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; border-radius: 3px; font-family: 'Montserrat', sans-serif; transition: background .2s; }
        .btn-primary:hover { background: #333; }

        /* empty state */
        .empty-row td { text-align: center; color: #aaa; font-style: italic; padding: 2rem; }

        @media (max-width: 900px) {
            .stats-grid { grid-template-columns: repeat(2, 1fr); }
        }
        @media (max-width: 600px) {
            .sidebar { display: none; }
            .stats-grid { grid-template-columns: 1fr 1fr; }
        }
    </style>
</head>
<body>

<%-- ══════════════════════════════════════════
     SIDEBAR
     ══════════════════════════════════════════ --%>
<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/settings">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/">&larr; Back to Store</a>
    </div>
</aside>

<%-- ══════════════════════════════════════════
     MAIN
     ══════════════════════════════════════════ --%>
<main class="main-content">

    <header class="header">
        <div class="header-title">Dashboard Overview</div>
        <div class="header-user">
            <c:choose>
                <c:when test="${not empty adminName}">${adminName}</c:when>
                <c:otherwise>Admin User</c:otherwise>
            </c:choose>
        </div>
    </header>

    <div class="content-scroll">

        <%-- ── STAT CARDS ── --%>
        <div class="stats-grid">

            <div class="stat-card">
                <div class="stat-title">Total Sales</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty totalSales}">
                            <fmt:formatNumber value="${totalSales}" pattern="#,##0.00" /> dh
                        </c:when>
                        <c:otherwise>— dh</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-title">Orders</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty orderCount}">${orderCount}</c:when>
                        <c:otherwise>—</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-title">Products</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty productCount}">${productCount}</c:when>
                        <c:otherwise>—</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-title">Customers</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty customerCount}">${customerCount}</c:when>
                        <c:otherwise>—</c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>

        <%-- ── RECENT ORDERS TABLE ── --%>
        <div class="table-container">
            <div class="table-header">
                <h3>Recent Orders</h3>
                <button class="btn-primary"
                        onclick="window.location.href='${pageContext.request.contextPath}/admin/orders'">
                    View All
                </button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Date</th>
                    <th>Amount (incl. VAT)</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>

                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <c:forEach var="order" items="${recentOrders}">
                            <tr>
                                <td><c:out value="${order.customerName}" /></td>
                                <td>
                                        <%-- order.createdAt doit être un java.util.Date ou LocalDateTime --%>
                                    <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy" />
                                </td>
                                <td>
                                    <fmt:formatNumber value="${order.totalIncl}" pattern="#,##0.00" /> dh
                                </td>
                                <td>
                                    <span class="status ${order.status}">
                                        <c:out value="${order.status}" />
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="empty-row">
                            <td colspan="5">No orders found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>

                </tbody>
            </table>
        </div>

    </div><%-- end content-scroll --%>
</main>

</body>
</html>
