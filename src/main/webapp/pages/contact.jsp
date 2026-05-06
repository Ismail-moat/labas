<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Contact Us - Labas.</title>
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
    <section class="page-section wide" style="padding: 120px 60px 80px; max-width: 1000px; margin: 0 auto;">
      <h2 style="font-family: 'Cormorant Garamond', serif; font-size: 2.5rem; text-align: center; margin-bottom: 3rem;">CONTACT US</h2>
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 60px; text-align: left;">
        <div>
          <form class="contact-form" onsubmit="event.preventDefault(); alert('Thank you! Your message has been sent.');">
            <input type="text" placeholder="Your Name" required style="width: 100%; padding: 12px; margin-bottom: 1rem; border: 1px solid #e8e4de;" />
            <input type="email" placeholder="Your Email" required style="width: 100%; padding: 12px; margin-bottom: 1rem; border: 1px solid #e8e4de;" />
            <input type="text" placeholder="Subject" required style="width: 100%; padding: 12px; margin-bottom: 1rem; border: 1px solid #e8e4de;" />
            <textarea placeholder="Your Message..." rows="5" required style="width: 100%; padding: 12px; margin-bottom: 1rem; border: 1px solid #e8e4de; font-family: inherit;"></textarea>
            <button type="submit" style="background: #111; color: #fff; border: none; padding: 12px 24px; cursor: pointer; text-transform: uppercase; letter-spacing: 0.1em;">SEND MESSAGE</button>
          </form>
        </div>
        <div>
          <h3 style="font-size: 18px; margin-bottom: 12px; font-weight: 500;">Get in Touch</h3>
          <p style="font-size: 14px; color: #555; line-height: 1.6; margin-bottom: 24px;">Have any questions or concerns? We're always ready to help!</p>
          <p style="font-size: 14px; margin-bottom: 12px;"><strong>Email:</strong> <a href="mailto:hello@labas.com" style="color:#111;">hello@labas.com</a></p>
          <p style="font-size: 14px; margin-bottom: 12px;"><strong>Phone:</strong> +212 600 000 000</p>
          <p style="font-size: 14px; margin-bottom: 12px;"><strong>Hours:</strong> Mon - Fri, 9am - 6pm</p>
          <div style="margin-top: 30px;">
            <h3 style="font-size: 18px; margin-bottom: 12px; font-weight: 500;">Visit Us</h3>
            <p style="font-size: 14px; color: #555; line-height: 1.6;">123 Rue Mohammed V,<br/>Casablanca 20000,<br/>Morocco</p>
          </div>
        </div>
      </div>
    </section>
    <footer class="footer">
      <div class="footer-inner">
        <div class="footer-logo">labas.</div>
        <div class="footer-col">
          <a href="${pageContext.request.contextPath}/about">ABOUT</a>
          <a href="${pageContext.request.contextPath}/catalog">SHOP</a>
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
