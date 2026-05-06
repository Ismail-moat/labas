
document.addEventListener('DOMContentLoaded', () => {

  const path = window.location.pathname;
  const links = document.querySelectorAll('.sidebar-nav a');
  links.forEach(link => {
    if(path.includes(link.getAttribute('href'))) {
      link.classList.add('active');
    }
  });
});

function deleteItem(type) {
  if(confirm(`Are you sure you want to delete this ${type}?`)) {

    alert(`${type} deleted successfully.`);
  }
}
