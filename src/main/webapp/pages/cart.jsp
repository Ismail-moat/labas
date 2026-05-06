<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Labas. — Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <style>
        .cart-page-container {
            padding: 140px 5% 80px;
            max-width: 1200px;
            margin: 0 auto;
            min-height: 80vh;
        }
        .cart-title {
            font-size: 3rem;
            margin-bottom: 3rem;
            text-align: center;
            letter-spacing: -0.02em;
        }
        .cart-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 4rem;
            align-items: start;
        }
        .cart-items {
            display: flex;
            flex-direction: column;
            gap: 2rem;
        }
        .cart-item {
            display: grid;
            grid-template-columns: 120px 1fr auto;
            gap: 2rem;
            align-items: center;
            padding-bottom: 2rem;
            border-bottom: 1px solid var(--border);
        }
        .cart-item-img {
            width: 120px;
            height: 160px;
            background-size: cover;
            background-position: center;
            border-radius: 4px;
            background-color: var(--surface);
        }
        .cart-item-details h3 {
            font-size: 1.1rem;
            margin-bottom: 0.5rem;
        }
        .cart-item-details p {
            color: var(--text-muted);
            font-size: 0.85rem;
            margin-bottom: 1rem;
        }
        .qty-controls {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .qty-btn {
            background: transparent;
            border: 1px solid var(--border);
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: border-color 0.2s;
        }
        .qty-btn:hover {
            border-color: var(--black);
        }
        .cart-item-price {
            font-family: var(--font-display);
            font-size: 1.2rem;
            text-align: right;
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            gap: 1rem;
        }
        .remove-btn {
            background: none;
            border: none;
            color: var(--text-muted);
            cursor: pointer;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }
        .remove-btn:hover {
            color: var(--black);
            text-decoration: underline;
        }
        .cart-summary-box {
            background: var(--surface);
            padding: 3rem;
            border-radius: 8px;
            position: sticky;
            top: 120px;
        }
        .cart-summary-box h3 {
            font-size: 1.5rem;
            margin-bottom: 2rem;
            border-bottom: 1px solid var(--border);
            padding-bottom: 1rem;
        }
        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 1rem;
            font-size: 0.9rem;
        }
        .summary-total {
            display: flex;
            justify-content: space-between;
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 1px solid var(--border);
            font-weight: 600;
            font-size: 1.2rem;
        }
        .empty-cart-msg {
            text-align: center;
            padding: 4rem 0;
            color: var(--text-muted);
        }
        @media (max-width: 900px) {
            .cart-grid { grid-template-columns: 1fr; }
            .cart-summary-box { position: static; }
        }
    </style>
</head>
<body>

<nav class="navbar" id="navbar">
    <div class="nav-logo">
        <a href="${pageContext.request.contextPath}/">LABAS.</a>
    </div>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/catalog">Shop</a></li>
        <li><a href="${pageContext.request.contextPath}/catalog?categoryId=1">Women</a></li>
        <li><a href="${pageContext.request.contextPath}/catalog?categoryId=2">Men</a></li>
        <li><a href="${pageContext.request.contextPath}/about">About</a></li>
    </ul>
    <div class="nav-icons">
        <span onclick="toggleSearch()">Search</span>
        <span>
            <a href="${pageContext.request.contextPath}/cart">Cart (${sessionScope.cartCount != null ? sessionScope.cartCount : 0})</a>
        </span>
        <c:choose>
            <c:when test="${not empty sessionScope.userId}">
                <span><a href="${pageContext.request.contextPath}/profile">Account</a></span>
            </c:when>
            <c:otherwise>
                <span><a href="${pageContext.request.contextPath}/login">Login</a></span>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<div class="search-overlay" id="searchOverlay">
    <div class="search-inner">
        <form action="${pageContext.request.contextPath}/catalog" method="GET" style="display:flex; width:100%;">
            <input type="text" name="q" placeholder="What are you looking for?" id="searchInput" />
            <button type="submit">Go</button>
            <button type="button" onclick="toggleSearch()" style="margin-left:20px;">✕</button>
        </form>
    </div>
</div>

<main class="cart-page-container reveal">
    <h1 class="cart-title">Your Cart</h1>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="empty-cart-msg">
                <p style="margin-bottom: 2rem;">Your cart is currently empty.</p>
                <a href="${pageContext.request.contextPath}/catalog" class="btn-primary">Return to Shop</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-grid">
                <div class="cart-items">
                    <c:forEach var="item" items="${cart.items}">
                        <div class="cart-item">
                            <div class="cart-item-img" style="background-image: url('${pageContext.request.contextPath}/${item.product.imageUrl}');"></div>

                            <div class="cart-item-details">
                                <h3>${item.product.name}</h3>
                                <c:if test="${not empty item.product.size}">
                                    <p>Size: ${item.product.size}</p>
                                </c:if>

                                <div class="qty-controls">
                                    <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;">
                                        <input type="hidden" name="action" value="update"/>
                                        <input type="hidden" name="productId" value="${item.product.id}"/>
                                        <input type="hidden" name="quantity" value="${item.quantity - 1}"/>
                                        <button type="submit" class="qty-btn">−</button>
                                    </form>

                                    <span style="font-size: 0.9rem; font-weight: 500;">${item.quantity}</span>

                                    <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;">
                                        <input type="hidden" name="action" value="update"/>
                                        <input type="hidden" name="productId" value="${item.product.id}"/>
                                        <input type="hidden" name="quantity" value="${item.quantity + 1}"/>
                                        <button type="submit" class="qty-btn">+</button>
                                    </form>
                                </div>
                            </div>

                            <div class="cart-item-price">
                                <span>${item.unitPrice.multiply(item.quantity)} dh</span>
                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                    <input type="hidden" name="action" value="remove"/>
                                    <input type="hidden" name="productId" value="${item.product.id}"/>
                                    <button type="submit" class="remove-btn">Remove</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="cart-summary-box">
                    <h3>Order Summary</h3>
                    <div class="summary-row">
                        <span style="color: var(--text-muted);">Subtotal</span>
                        <span>${cartTotal} dh</span>
                    </div>
                    <div class="summary-row">
                        <span style="color: var(--text-muted);">Shipping</span>
                        <span>Complimentary</span>
                    </div>
                    <div class="summary-total">
                        <span>Total</span>
                        <span>${cartTotal} dh</span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn-primary" style="display:block; text-align:center; margin-top:2.5rem; width:100%;">Proceed to Checkout</a>
                    <a href="${pageContext.request.contextPath}/catalog" style="display:block; text-align:center; margin-top:1.5rem; font-size:0.85rem; color:var(--text-muted);">Continue Shopping</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<footer class="footer">
    <div class="footer-grid">
        <div>
            <div class="footer-brand">LABAS.</div>
            <p style="color: var(--text-muted); font-size: 0.85rem; max-width: 250px;">Minimalist luxury. Crafted for everyday elegance.</p>
        </div>
        <div class="footer-col">
            <h4>Shop</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/catalog">All Products</a></li>
                <li><a href="${pageContext.request.contextPath}/catalog?categoryId=1">Women</a></li>
                <li><a href="${pageContext.request.contextPath}/catalog?categoryId=2">Men</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h4>Company</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/about">Our Story</a></li>
                <li><a href="${pageContext.request.contextPath}/contact">Contact Us</a></li>
            </ul>
        </div>
        <div class="footer-col">
            <h4>Legal</h4>
            <ul>
                <li><a href="#">Privacy Policy</a></li>
                <li><a href="#">Terms of Service</a></li>
                <li><a href="#">Shipping</a></li>
            </ul>
        </div>
    </div>
    <div class="footer-bottom">
        &copy; 2025 Labas. All rights reserved.
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
