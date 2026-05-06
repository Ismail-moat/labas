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
  window.addEventListener("scroll", () => {
    if (window.scrollY > 50) {
      navbar.style.background = "rgba(255, 255, 255, 0.95)";
      navbar.style.boxShadow = "var(--shadow-sm)";
      navbar.style.height = "70px";
    } else {
      navbar.style.background = "rgba(255, 255, 255, 0.8)";
      navbar.style.boxShadow = "none";
      navbar.style.height = "80px";
    }
  });
}

const observerOptions = {
  root: null,
  rootMargin: '0px',
  threshold: 0.1
};

const observer = new IntersectionObserver((entries, observer) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('in-view');
      observer.unobserve(entry.target);
    }
  });
}, observerOptions);

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll('.reveal').forEach((el) => {
    observer.observe(el);
  });

});
