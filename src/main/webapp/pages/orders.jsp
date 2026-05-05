<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Labas. — My Orders</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet"/>
  <style>
    .orders-section { padding: 100px 60px 80px; max-width: 900px; margin: 0 auto; }
    .order-card { border: 1px solid var(--lightgrey); margin-bottom: 24px; padding: 24px; }
    .order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
    .order-id { font-weight: 600; font-size: 13px; letter-spacing: 0.08em; }
    .order-date { font-size: 11px; color: var(--grey); }
    .order-status { font-size: 10px; letter-spacing: 0.12em; font-weight: 600; padding: 4px 12px; }
    .status-pending   { background: #fff3cd; color: #856404; }
    .status-confirmed { background: #cce5ff; color: #004085; }
    .status-shipped   { background: #d4edda; color: #155724; }
    .status-delivered { background: #d4edda; color: #155724; }
    .status-cancelled { background: #f8d7da; color: #721c24; }
    .order-items-list { border-top: 1px solid var(--lightgrey); padding-top: 16px; }
    .order-item-row { display: flex; justify-content: space-between; font-size: 12px; margin-bottom: 8px; }
    .order-total { border-top: 1px solid var(--lightgrey); margin-top: 12px; padding-top: 12px; display: flex; justify-content: space-between; font-weight: 600; }
  </style>
</head>
<body>
<nav class="navbar" id="navbar">
  <div class="nav-logo"><a href="${pageContext.request.contextPath}/index.html">labas.</a></div>
  <ul class="nav-links">
    <li><a href="${pageContext.request.contextPath}/pages/catalog.html">SHOP</a></li>
  </ul>
  <div class="nav-icons">
    <span class="nav-icon"><a href="${pageContext.request.contextPath}/cart">🛒 (${sessionScope.cartCount != null ? sessionScope.cartCount : 0})</a></span>
    <span class="nav-icon"><a href="${pageContext.request.contextPath}/profile">PROFILE</a></span>
    <span class="nav-icon"><a href="${pageContext.request.contextPath}/logout">LOGOUT</a></span>
  </div>
</nav>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/index.html">Home</a> /
  <a href="${pageContext.request.contextPath}/profile">Profile</a> /
  <span>My Orders</span>
</div>

<section class="orders-section">
  <h1 class="page-title">MY ORDERS</h1>

  <c:choose>
    <c:when test="${empty orders}">
      <div style="text-align:center; padding: 60px 0; color: var(--grey);">
        <p style="font-size:16px; margin-bottom:24px;">You haven't placed any orders yet.</p>
        <a href="${pageContext.request.contextPath}/pages/catalog.html" class="btn-add-cart">START SHOPPING</a>
      </div>
    </c:when>
    <c:otherwise>
      <c:forEach var="order" items="${orders}">
        <div class="order-card">
          <div class="order-header">
            <div>
              <div class="order-id">#LBS-${order.id}</div>
              <div class="order-date">
                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy" type="both"/>
              </div>
            </div>
            <span class="order-status status-${order.status.name().toLowerCase()}">
              ${order.status.name()}
            </span>
          </div>

          <div class="order-items-list">
            <c:forEach var="item" items="${order.items}">
              <div class="order-item-row">
                <span>${item.product.name} × ${item.quantity}</span>
                <span><fmt:formatNumber value="${item.amountIncl}" type="currency" currencySymbol="$"/></span>
              </div>
            </c:forEach>
            <div class="order-total">
              <span>TOTAL</span>
              <span><fmt:formatNumber value="${order.totalIncl}" type="currency" currencySymbol="$"/></span>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</section>

<footer class="footer">
  <div class="footer-inner">
    <div class="footer-logo">labas.</div>
  </div>
  <div class="footer-bottom">© 2025 Labas. All rights reserved.</div>
</footer>
</body>
</html>
