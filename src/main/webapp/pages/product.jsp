<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Labas. — ${product.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .product-page-container {
            padding: 120px 5% 80px;
            max-width: 1400px;
            margin: 0 auto;
        }
        .product-split {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 6rem;
            align-items: start;
        }
        .product-gallery {
            position: sticky;
            top: 120px;
        }
        .product-gallery img {
            width: 100%;
            height: auto;
            border-radius: 4px;
            background: var(--surface);
        }
        .product-details {
            padding-top: 2rem;
        }
        .product-details h1 {
            font-size: 3rem;
            margin-bottom: 1rem;
            letter-spacing: -0.02em;
        }
        .product-price-large {
            font-size: 1.5rem;
            font-family: var(--font-display);
            color: var(--text-muted);
            margin-bottom: 2rem;
        }
        .product-desc {
            color: var(--text-muted);
            line-height: 1.8;
            margin-bottom: 3rem;
            font-size: 0.95rem;
        }
        .add-to-cart-form {
            margin-bottom: 3rem;
        }
        .qty-wrapper {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        .qty-wrapper label {
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            font-weight: 600;
        }
        .qty-wrapper input {
            width: 80px;
            padding: 0.75rem;
            border: 1px solid var(--border);
            text-align: center;
            font-family: var(--font-body);
            background: transparent;
        }
        .btn-full {
            width: 100%;
            padding: 1.25rem;
            font-size: 0.85rem;
        }
        .product-meta-list {
            border-top: 1px solid var(--border);
            padding-top: 2rem;
        }
        .meta-item {
            display: flex;
            justify-content: space-between;
            padding: 1rem 0;
            border-bottom: 1px solid var(--border);
            font-size: 0.85rem;
        }
        .meta-label {
            color: var(--text-muted);
        }
        .meta-value {
            font-weight: 500;
        }
        @media (max-width: 900px) {
            .product-split { grid-template-columns: 1fr; gap: 3rem; }
            .product-gallery { position: relative; top: 0; }
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
            <a href="${pageContext.request.contextPath}/cart">Cart (<span id="cart-count">${sessionScope.cartCount != null ? sessionScope.cartCount : 0}</span>)</a>
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

<main class="product-page-container">
    <div class="product-split">
        <div class="product-gallery reveal">
            <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}" />
        </div>

        <div class="product-details reveal">
            <div class="text-sm" style="color: var(--text-muted); margin-bottom: 1rem;">
                ${product.categoryId == 1 ? "Women" : (product.categoryId == 2 ? "Men" : "Collection")}
            </div>
            <h1>${product.name}</h1>
            <div class="product-price-large">${product.price} dh</div>

            <p class="product-desc">
                ${product.description != null ? product.description : "Experience the perfect balance of form and function. Crafted with premium materials for uncompromising comfort and enduring style. A true essential for the modern wardrobe."}
            </p>

            <form action="${pageContext.request.contextPath}/cart" method="POST" class="add-to-cart-form">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.id}">

                <div class="qty-wrapper">
                    <label>Quantity</label>
                    <input type="number" name="quantity" value="1" min="1">
                </div>

                <button type="submit" class="btn-primary btn-full">Add to Bag</button>
            </form>

            <div class="product-meta-list">
                <div class="meta-item">
                    <span class="meta-label">Availability</span>
                    <span class="meta-value">${product.stockQty > 0 ? "In Stock" : "Sold Out"}</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">SKU</span>
                    <span class="meta-value">LAB-${product.id}00${product.id}</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Shipping</span>
                    <span class="meta-value">Free standard shipping</span>
                </div>
            </div>
        </div>
    </div>
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
