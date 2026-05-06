<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Labas. — ${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <style>
        .catalog-hero {
            padding: 180px 5% 80px;
            background: var(--surface);
            text-align: center;
        }
        .catalog-title {
            font-size: 3.5rem;
            margin-bottom: 2rem;
        }
        .filter-bar {
            display: flex;
            justify-content: center;
            gap: 2rem;
            margin-bottom: 2rem;
        }
        .filter-link {
            font-size: 0.85rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            color: var(--text-muted);
            font-weight: 500;
        }
        .filter-link.active, .filter-link:hover {
            color: var(--black);
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

<main>
    <div class="catalog-hero reveal">
        <h1 class="catalog-title">${title}</h1>
        <div class="filter-bar">
            <a href="${pageContext.request.contextPath}/catalog" class="filter-link ${empty param.categoryId ? 'active' : ''}">All</a>
            <c:forEach var="cat" items="${categories}">
                <a href="${pageContext.request.contextPath}/catalog?categoryId=${cat.id}" class="filter-link ${param.categoryId == cat.id ? 'active' : ''}">${cat.name}</a>
            </c:forEach>
        </div>
        <p style="color: var(--text-muted); font-size: 0.85rem;">Showing ${products.size()} products</p>
    </div>

    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <div class="product-card reveal" onclick="location.href = '${pageContext.request.contextPath}/product?id=${product.id}'">
                <div class="product-image-wrap">
                    <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}" />
                    <div class="quick-add-overlay">
                        <form action="${pageContext.request.contextPath}/cart" method="POST" style="width:100%;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" class="btn-primary" style="width:100%; padding: 0.8rem;" onclick="event.stopPropagation();">Add to Cart</button>
                        </form>
                    </div>
                </div>
                <div class="product-info">
                    <h3 class="product-name">${product.name}</h3>
                    <div class="product-price">${product.price} dh</div>
                </div>
            </div>
        </c:forEach>
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
