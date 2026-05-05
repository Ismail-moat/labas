<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Labas. — Checkout</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet"/>
</head>
<body>
<nav class="navbar" id="navbar">
  <div class="nav-logo"><a href="${pageContext.request.contextPath}/index.html">labas.</a></div>
  <div class="nav-icons">
    <span class="nav-icon"><a href="${pageContext.request.contextPath}/cart">🛒 (${sessionScope.cartCount != null ? sessionScope.cartCount : 0})</a></span>
  </div>
</nav>

<section class="checkout-section">
  <div class="steps">
    <div class="step done"><span>✓</span> CART</div>
    <div class="step-divider"></div>
    <div class="step active"><span>2</span> CHECKOUT</div>
    <div class="step-divider"></div>
    <div class="step"><span>3</span> CONFIRMATION</div>
  </div>

  <c:if test="${not empty error}">
    <div style="color:red; text-align:center; margin-bottom:20px;">${error}</div>
  </c:if>

  <div class="checkout-layout">
    <div class="checkout-form-col">
      <form method="post" action="${pageContext.request.contextPath}/checkout">

        <div class="form-section">
          <h3>CONTACT INFORMATION</h3>
          <div class="form-grid">
            <div class="form-field">
              <label>First Name</label>
              <input type="text" value="${sessionScope.firstName}" readonly style="background:#f5f4f0;"/>
            </div>
            <div class="form-field">
              <label>Last Name</label>
              <input type="text" value="${sessionScope.lastName}" readonly style="background:#f5f4f0;"/>
            </div>
            <div class="form-field full">
              <label>Email Address</label>
              <input type="email" value="${sessionScope.email}" readonly style="background:#f5f4f0;"/>
            </div>
            <div class="form-field full">
              <label>Phone Number</label>
              <input type="tel" value="${sessionScope.phone}" readonly style="background:#f5f4f0;"/>
            </div>
          </div>
        </div>

        <div class="form-section">
          <h3>SHIPPING ADDRESS</h3>
          <div class="form-grid">
            <div class="form-field full">
              <label>Address *</label>
              <input type="text" name="address" placeholder="123 Rue Mohammed V"
                     value="${sessionScope.address}" required/>
            </div>
            <div class="form-field full">
              <label>Apartment / Suite (optional)</label>
              <input type="text" name="addressExtra" placeholder="Apt 4B"/>
            </div>
            <div class="form-field">
              <label>City *</label>
              <input type="text" name="city" placeholder="Casablanca"
                     value="${sessionScope.city}" required/>
            </div>
            <div class="form-field">
              <label>Postal Code</label>
              <input type="text" name="zipCode" placeholder="20000"
                     value="${sessionScope.zipCode}"/>
            </div>
          </div>
        </div>

        <div class="form-section">
          <h3>PAYMENT</h3>
          <div class="payment-tabs">
            <button type="button" class="pay-tab active" onclick="setPayTab(this,'card')">💳 Card</button>
            <button type="button" class="pay-tab" onclick="setPayTab(this,'cod')">💵 Cash on Delivery</button>
          </div>
          <div id="cardPanel" class="pay-panel active">
            <div class="form-grid">
              <div class="form-field full">
                <label>Card Number</label>
                <input type="text" placeholder="•••• •••• •••• ••••" maxlength="19"/>
              </div>
              <div class="form-field full">
                <label>Cardholder Name</label>
                <input type="text" placeholder="YOUR NAME"/>
              </div>
              <div class="form-field">
                <label>Expiry</label>
                <input type="text" placeholder="MM / YY" maxlength="7"/>
              </div>
              <div class="form-field">
                <label>CVV</label>
                <input type="text" placeholder="•••" maxlength="4"/>
              </div>
            </div>
          </div>
          <div id="codPanel" class="pay-panel" style="display:none;">
            <p style="color:#888; font-size:13px; margin:20px 0;">
              Pay cash when your order arrives.
            </p>
          </div>
        </div>

        <button type="submit" class="btn-add-cart full-width">PLACE ORDER</button>
        <a href="${pageContext.request.contextPath}/cart" class="continue-link">← Back to Cart</a>
      </form>
    </div>

    <div class="checkout-summary">
      <h3>YOUR ORDER</h3>
      <c:forEach var="item" items="${cart.items}">
        <div class="summary-row small">
          <span>${item.product.name} ×${item.quantity}</span>
          <span><fmt:formatNumber value="${item.unitPrice.multiply(item.quantity)}" type="currency" currencySymbol="$"/></span>
        </div>
      </c:forEach>
      <div class="divider"></div>
      <div class="summary-row"><span>Shipping</span><span>Free</span></div>
      <div class="divider"></div>
      <div class="summary-row total">
        <span>TOTAL</span>
        <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$"/></span>
      </div>
      <div class="trust-badges">
        <span>🔒 SSL Secure</span>
        <span>🚚 Free Delivery</span>
        <span>↩ Easy Returns</span>
      </div>
    </div>
  </div>
</section>

<script>
  function setPayTab(btn, panel) {
    document.querySelectorAll('.pay-tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');
    document.querySelectorAll('.pay-panel').forEach(p => p.style.display = 'none');
    document.getElementById(panel + 'Panel').style.display = 'block';
  }
</script>
</body>
</html>
