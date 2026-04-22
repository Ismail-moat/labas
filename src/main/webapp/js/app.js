function getCart() {
  return JSON.parse(localStorage.getItem("labas_cart") || "{}");
}

function setCart(cart) {
  localStorage.setItem("labas_cart", JSON.stringify(cart));
}

function addToCart(productId) {
  const cart = getCart();
  cart[productId] = (cart[productId] || 0) + 1;
  setCart(cart);
  updateCartCount();
  showToast("✓ Added to cart");
}

function updateCartCount() {
  const cart = getCart();
  const count = Object.values(cart).reduce((sum, qty) => sum + qty, 0);
  document.querySelectorAll("#cart-count").forEach((el) => (el.textContent = count));
}

function showToast(msg) {
  let toast = document.getElementById("toast");
  if (!toast) {
    toast = document.createElement("div");
    toast.className = "toast";
    toast.id = "toast";
    document.body.appendChild(toast);
  }
  toast.textContent = msg;
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2500);
}

function toggleSearch() {
  const overlay = document.getElementById("searchOverlay");
  if (!overlay) return;
  overlay.classList.toggle("open");
  if (overlay.classList.contains("open")) {
    document.getElementById("searchInput")?.focus();
  }
}

document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    document.getElementById("searchOverlay")?.classList.remove("open");
  }
});

const navbar = document.getElementById("navbar");
if (navbar) {
  // Check if we are on a page with a hero (home page)
  const isHeroPage = document.getElementById("hero");

  if (isHeroPage) {
    document.body.classList.add("hero-page");
    window.addEventListener("scroll", () => {
      if (window.scrollY > 80) {
        navbar.classList.add("scrolled");
      } else {
        navbar.classList.remove("scrolled");
      }
    });
  }
}

// PRODUCT //
let sliderOffset = 0;
const SLIDE_STEP = 1; // cards to slide

function slideProducts(dir) {
  const track = document.getElementById("productsTrack");
  if (!track) return;
  const cards = track.querySelectorAll(".product-card");
  const maxOffset = cards.length - 3;
  sliderOffset = Math.max(0, Math.min(maxOffset, sliderOffset + dir));
  const cardWidth = cards[0].offsetWidth + 16; // card + gap
  track.style.transform = `translateX(-${sliderOffset * cardWidth}px)`;
  track.style.transition = "transform 0.45s ease";
}

//
document.querySelectorAll(".size-btn").forEach((btn) => {
  btn.addEventListener("click", function () {
    const group = this.closest(".size-grid");
    group?.querySelectorAll(".size-btn").forEach((b) => b.classList.remove("active"));
    this.classList.add("active");
  });
});

//COLOR //
document.querySelectorAll(".swatch").forEach((sw) => {
  sw.addEventListener("click", function () {
    const group = this.closest(".color-swatches");
    group?.querySelectorAll(".swatch").forEach((s) => s.classList.remove("active"));
    this.classList.add("active");
  });
});

// PRICE RANGE //
function updatePrice(val) {
  const el = document.getElementById("priceVal");
  if (el) el.textContent = val;
}

// SUBSCRIBE FORM  //
const subForm = document.querySelector(".subscribe-form");
if (subForm) {
  subForm.querySelector("button")?.addEventListener("click", () => {
    const input = subForm.querySelector("input");
    if (input?.value && input.value.includes("@")) {
      showToast("✓ Subscribed! Welcome to Labas.");
      input.value = "";
    } else {
      input.style.borderColor = "red";
      setTimeout(() => {
        input.style.borderColor = "";
      }, 1500);
    }
  });
}

document.querySelectorAll("a[href]").forEach((link) => {
  const href = link.getAttribute("href");
  if (href && href.endsWith(".html") && !href.startsWith("http")) {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      document.body.style.opacity = "0";
      document.body.style.transition = "opacity 0.25s";
      setTimeout(() => {
        window.location.href = href;
      }, 250);
    });
  }
});

document.body.style.opacity = "0";
window.addEventListener("load", () => {
  document.body.style.transition = "opacity 0.4s";
  document.body.style.opacity = "1";
});

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = "1";
        entry.target.style.transform = "translateY(0)";
      }
    });
  },
  { threshold: 0.1 },
);

// Animate product cards on scroll
document.querySelectorAll(".product-card, .review-item, .article-card, .cat-item").forEach((el) => {
  el.style.opacity = "0";
  el.style.transform = "translateY(20px)";
  el.style.transition = "opacity 0.5s ease, transform 0.5s ease";
  observer.observe(el);
});

updateCartCount();
