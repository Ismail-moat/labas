<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>About - Labas.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
  </head>
  <body>
    <nav class="navbar" id="navbar">
      <div class="nav-logo"><a href="${pageContext.request.contextPath}/">labas.</a></div>
      <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/catalog">SHOP</a></li>
        <li><a href="${pageContext.request.contextPath}/catalog?categoryId=1">WOMEN</a></li>
        <li><a href="${pageContext.request.contextPath}/catalog?categoryId=2">MEN</a></li>
        <li><a href="#">SALE</a></li>
      </ul>
      <div class="nav-icons">
        <span class="nav-icon" onclick="toggleSearch()">🔍 SEARCH</span>
        <span class="nav-icon"><a href="${pageContext.request.contextPath}/cart">🛒 (<span id="cart-count">${sessionScope.cartCount != null ? sessionScope.cartCount : 0}</span>)</a></span>
        <c:choose>
            <c:when test="${not empty sessionScope.userId}">
                <span class="nav-icon"><a href="${pageContext.request.contextPath}/profile">👤 ${sessionScope.userName}</a></span>
            </c:when>
            <c:otherwise>
                <span class="nav-icon"><a href="${pageContext.request.contextPath}/login">👤 LOGIN</a></span>
            </c:otherwise>
        </c:choose>
      </div>
    </nav>
    <section class="page-section wide about-content" style="padding: 120px 60px 80px; max-width: 800px; margin: 0 auto; text-align: center;">
      <h2 style="font-family: 'Cormorant Garamond', serif; font-size: 2.5rem; margin-bottom: 2rem;">ABOUT LABAS.</h2>
      <p style="line-height: 1.8; color: #555; margin-bottom: 1.5rem;">Founded in 2025, Labas. redefines minimalism through timeless design and exceptional craftsmanship. We believe that true style isn't about constant change, but about finding the classic pieces that endure.</p>
      <p style="line-height: 1.8; color: #555; margin-bottom: 1.5rem;">Every piece in our collection is thoughtfully curated to provide an effortless balance of comfort and elegance. Whether you're dressing for a casual day or a sophisticated evening, our garments are designed to seamlessly integrate into your lifestyle.</p>
      <p style="line-height: 1.8; color: #555; margin-bottom: 1.5rem;">Our commitment extends beyond aesthetics. We source premium materials and collaborate with trusted artisans to ensure that what you wear not only looks stunning but also stands the test of time.</p>
    </section>
    <footer class="footer">
      <div class="footer-inner">
        <div class="footer-logo">labas.</div>
        <div class="footer-col">
          <a href="${pageContext.request.contextPath}/about">ABOUT</a>
          <a href="${pageContext.request.contextPath}/catalog">SHOP</a>
          <a href="#">SALE</a>
        </div>
        <div class="footer-col">
          <a href="#">TRACK ORDER</a>
          <a href="${pageContext.request.contextPath}/contact">CONTACT US</a>
        </div>
      </div>
      <div class="footer-bottom">© 2025 Labas. All rights reserved.</div>
    </footer>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
  </body>
</html>
