<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>

<html lang="en">


<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Labas. — Cart</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet"/>
</head>
<body>
<nav class="navbar" id="navbar">
  <div class="nav-logo"><a href="${pageContext.request.contextPath}/index.html">labas.</a></div>
  <ul class="nav-links">
    <li><a href="${pageContext.request.contextPath}/pages/catalog.html">SHOP</a></li>
    <li><a href="${pageContext.request.contextPath}/pages/catalog.html?cat=women">WOMEN</a></li>
    <li><a href="${pageContext.request.contextPath}/pages/catalog.html?cat=men">MEN</a></li>
  </ul>
  <div class="nav-icons">
    <span class="nav-icon">
      <a href="${pageContext.request.contextPath}/cart" class="active">
        🛒 (<span id="cart-count">${sessionScope.cartCount != null ? sessionScope.cartCount : 0}</span>)
      </a>
    </span>
    <c:choose>
      <c:when test="${sessionScope.userId != null}">
        <span class="nav-icon"><a href="${pageContext.request.contextPath}/profile">PROFILE</a></span>
        <span class="nav-icon"><a href="${pageContext.request.contextPath}/logout">LOGOUT</a></span>
      </c:when>
      <c:otherwise>
        <span class="nav-icon"><a href="${pageContext.request.contextPath}/login">LOGIN</a></span>
      </c:otherwise>
    </c:choose>
  </div>
</nav>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/index.html">Home</a> / <span>Shopping Cart</span>
</div>

<section class="cart-section">
  <h1 class="page-title">YOUR CART</h1>

  <div class="steps">
    <div class="step active"><span>1</span> CART</div>
    <div class="step-divider"></div>
    <div class="step"><span>2</span> CHECKOUT</div>
    <div class="step-divider"></div>
    <div class="step"><span>3</span> CONFIRMATION</div>
  </div>

  <c:choose>
    <c:when test="${empty cart.items}">
      <div class="empty-cart" style="display:flex;">
        <p>Your cart is empty.</p>
        <a href="${pageContext.request.contextPath}/pages/catalog.html" class="btn-outline dark">START SHOPPING</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="cart-layout">
        <div class="cart-items">
          <c:forEach var="item" items="${cart.items}">
            <div class="cart-item">
              <div class="cart-item-img p1"
                   <c:if test="${not empty item.product.imageUrl}">
                     style="background-image: url('${item.product.imageUrl}');"
                   </c:if>
              ></div>
              <div class="cart-item-details">
                <p class="cart-item-name">${item.product.name}</p>
                <c:if test="${not empty item.product.size}">
                  <p class="cart-item-sub">Size: ${item.product.size}</p>
                </c:if>
                <div class="qty-control small">
                  <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;">
                    <input type="hidden" name="action" value="update"/>
                    <input type="hidden" name="productId" value="${item.product.id}"/>
                    <input type="hidden" name="quantity" value="${item.quantity - 1}"/>
                    <button type="submit">−</button>
                  </form>
                  <span>${item.quantity}</span>
                  <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;">
                    <input type="hidden" name="action" value="update"/>
                    <input type="hidden" name="productId" value="${item.product.id}"/>
                    <input type="hidden" name="quantity" value="${item.quantity + 1}"/>
                    <button type="submit">+</button>
                  </form>
                </div>
              </div>
              <div class="cart-item-price">
                <span><fmt:formatNumber value="${item.unitPrice.multiply(item.quantity)}" type="currency" currencySymbol="$"/></span>
                <form method="post" action="${pageContext.request.contextPath}/cart">
                  <input type="hidden" name="action" value="remove"/>
                  <input type="hidden" name="productId" value="${item.product.id}"/>
                  <button type="submit" class="remove-btn">✕</button>
                </form>
              </div>
            </div>
          </c:forEach>
        </div>

        <div class="cart-summary">
          <h3>ORDER SUMMARY</h3>
          <div class="summary-row">
            <span>Subtotal</span>
            <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$"/></span>
          </div>
          <div class="summary-row"><span>Shipping</span><span>Free</span></div>
          <div class="divider"></div>
          <div class="summary-row total">
            <span>TOTAL</span>
            <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$"/></span>
          </div>
          <a href="${pageContext.request.contextPath}/checkout"
             class="btn-add-cart"
             style="display:block; text-align:center; text-decoration:none; margin-top:20px;">
            PROCEED TO CHECKOUT
          </a>
          <a href="${pageContext.request.contextPath}/pages/catalog.html" class="continue-link">← Continue Shopping</a>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</section>


<footer class="footer">
  <div class="footer-inner">
    <div class="footer-logo">labas.</div>
    <div class="footer-col">
      <a href="${pageContext.request.contextPath}/pages/about.html">ABOUT</a>
      <a href="${pageContext.request.contextPath}/pages/catalog.html">SHOP</a>
    </div>
    <div class="footer-col">
      <a href="${pageContext.request.contextPath}/orders">MY ORDERS</a>
    </div>
  </div>
  <div class="footer-bottom">© 2025 Labas. All rights reserved.</div>
</footer>
</body>
</html>
