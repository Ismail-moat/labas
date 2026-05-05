<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Labas. — Order Confirmed</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet"/>
</head>
<body>
<nav class="navbar" id="navbar">
  <div class="nav-logo"><a href="${pageContext.request.contextPath}/index.html">labas.</a></div>
  <div class="nav-icons">
    <span class="nav-icon"><a href="${pageContext.request.contextPath}/pages/catalog.html">CONTINUE SHOPPING</a></span>
  </div>
</nav>

<section class="confirmation-section">
  <div class="steps">
    <div class="step done"><span>✓</span> CART</div>
    <div class="step-divider"></div>
    <div class="step done"><span>✓</span> CHECKOUT</div>
    <div class="step-divider"></div>
    <div class="step active"><span>3</span> CONFIRMATION</div>
  </div>

  <div class="confirm-card">
    <div class="confirm-check">✓</div>
    <h1 class="confirm-title">ORDER PLACED!</h1>
    <p class="confirm-subtitle"><em>Thank you for shopping with Labas.</em></p>
    <p class="confirm-msg">
      Your order <strong>#LBS-${orderId}</strong> has been confirmed.<br/>
      A confirmation will be available in your order history.
    </p>

    <div class="confirm-details">
      <div class="confirm-row">
        <span>ORDER NUMBER</span><span>#LBS-${orderId}</span>
      </div>
      <div class="confirm-row">
        <span>ESTIMATED DELIVERY</span><span>3 – 5 business days</span>
      </div>
      <div class="confirm-row">
        <span>SHIPPING METHOD</span><span>Standard Free Shipping</span>
      </div>
    </div>

    <div class="confirm-actions">
      <a href="${pageContext.request.contextPath}/pages/catalog.html" class="btn-add-cart">CONTINUE SHOPPING</a>
      <a href="${pageContext.request.contextPath}/orders" class="btn-outline dark">VIEW MY ORDERS</a>
    </div>
  </div>

  <div class="confirm-watermark">THANK YOU</div>
</section>

<footer class="footer">
  <div class="footer-inner">
    <div class="footer-logo">labas.</div>
  </div>
  <div class="footer-bottom">© 2025 Labas. All rights reserved.</div>
</footer>
</body>
</html>
