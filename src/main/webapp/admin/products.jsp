<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession adminSession = request.getSession(false);
    if (adminSession == null || !"admin".equalsIgnoreCase((String) adminSession.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<%@ taglib prefix="c"   uri="jakarta.tags.core"      %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"       %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Products Management — Labas.</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,400;0,600;1,400&family=Montserrat:wght@300;400;500;600&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body { display: flex; font-family: 'Montserrat', sans-serif; background: #f7f5f2; color: #1a1a1a; min-height: 100vh; }
        .sidebar { width: 220px; min-height: 100vh; background: #111; display: flex; flex-direction: column; padding: 2rem 1.5rem; flex-shrink: 0; }
        .sidebar-logo { font-family: 'Cormorant Garamond', serif; font-size: 1.4rem; color: #fff; letter-spacing: .15em; margin-bottom: 2.5rem; }
        .sidebar-nav { display: flex; flex-direction: column; gap: .4rem; flex: 1; }
        .sidebar-nav a { color: #aaa; text-decoration: none; font-size: .78rem; letter-spacing: .1em; text-transform: uppercase; padding: .6rem .8rem; border-radius: 4px; transition: background .2s, color .2s; }
        .sidebar-nav a:hover, .sidebar-nav a.active { background: #222; color: #fff; }
        .sidebar-footer a { color: #555; font-size: .72rem; text-decoration: none; }
        .sidebar-footer a:hover { color: #aaa; }
        .main-content { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
        .header { padding: 1.6rem 2rem; background: #fff; border-bottom: 1px solid #e8e4de; display: flex; align-items: center; justify-content: space-between; }
        .header-title { font-family: 'Cormorant Garamond', serif; font-size: 1.5rem; font-weight: 600; }
        .content-scroll { padding: 2rem; overflow-y: auto; display: flex; flex-direction: column; gap: 2rem; }
        .table-container { background: #fff; border: 1px solid #e8e4de; border-radius: 6px; overflow: hidden; }
        .table-header { padding: 1.2rem 1.6rem; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #e8e4de; }
        .table-header h3 { font-size: .85rem; letter-spacing: .1em; text-transform: uppercase; font-weight: 500; }
        table { width: 100%; border-collapse: collapse; }
        thead th { padding: .8rem 1.2rem; text-align: left; font-size: .68rem; letter-spacing: .1em; text-transform: uppercase; color: #888; border-bottom: 1px solid #e8e4de; font-weight: 500; }
        tbody td { padding: .9rem 1.2rem; font-size: .82rem; border-bottom: 1px solid #f0ede8; }
        .product-img { width: 40px; height: 40px; object-fit: cover; border-radius: 4px; }
        .actions { display: flex; gap: 0.5rem; }
        .btn-action { background: #e8e4de; color: #111; padding: 0.3rem 0.6rem; text-decoration: none; font-size: 0.7rem; border-radius: 3px; cursor: pointer; border: none; }
        .btn-action:hover { background: #d0c9c0; }
        .btn-action.danger { color: #b91c1c; }
        .btn-primary { background: #111; color: #fff; border: none; padding: .5rem 1.1rem; font-size: .72rem; letter-spacing: .1em; text-transform: uppercase; cursor: pointer; border-radius: 3px; font-family: 'Montserrat', sans-serif; transition: background .2s; }
        .btn-primary:hover { background: #333; }
        .drawer-overlay { position: fixed; inset: 0; background: rgba(10, 10, 10, 0.45); backdrop-filter: blur(3px); z-index: 100; opacity: 0; pointer-events: none; transition: opacity .35s ease; }
        .drawer-overlay.open { opacity: 1; pointer-events: all; }
        .drawer { position: fixed; top: 0; right: 0; width: 480px; max-width: 100vw; height: 100vh; background: #fff; z-index: 101; display: flex; flex-direction: column; transform: translateX(100%); transition: transform .4s cubic-bezier(.22,.68,0,1.1); box-shadow: -8px 0 40px rgba(0,0,0,.12); }
        .drawer.open { transform: translateX(0); }
        .drawer-head { padding: 1.6rem 2rem 1.4rem; border-bottom: 1px solid #e8e4de; display: flex; align-items: center; justify-content: space-between; flex-shrink: 0; }
        .drawer-head-title { font-family: 'Cormorant Garamond', serif; font-size: 1.35rem; font-weight: 600; }
        .drawer-close { background: none; border: none; cursor: pointer; color: #888; font-size: 1.2rem; padding: .2rem .4rem; }
        .drawer-body { flex: 1; overflow-y: auto; padding: 1.8rem 2rem; display: flex; flex-direction: column; gap: 1.4rem; }
        .form-section-label { font-size: .65rem; letter-spacing: .14em; text-transform: uppercase; color: #aaa; font-weight: 600; padding-bottom: .5rem; border-bottom: 1px solid #f0ede8; }
        .field { display: flex; flex-direction: column; gap: .4rem; }
        .field label { font-size: .72rem; text-transform: uppercase; color: #555; }
        .field input, .field select, .field textarea { font-family: 'Montserrat', sans-serif; font-size: .83rem; background: #faf9f7; border: 1px solid #e8e4de; border-radius: 4px; padding: .6rem .85rem; outline: none; width: 100%; }
        .field-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .img-preview { width: 100%; height: 140px; border-radius: 4px; border: 1px dashed #d0c9c0; display: flex; align-items: center; justify-content: center; background: #faf9f7; }
        .img-preview img { width: 100%; height: 100%; object-fit: cover; display: none; }
        .size-pills { display: flex; flex-wrap: wrap; gap: .4rem; }
        .size-pill { padding: .3rem .75rem; border: 1px solid #e8e4de; border-radius: 30px; font-size: .72rem; cursor: pointer; background: #faf9f7; }
        .size-pill.selected { background: #111; color: #fff; }
        .drawer-foot { padding: 1.2rem 2rem; border-top: 1px solid #e8e4de; display: flex; gap: .75rem; }
        .btn-ghost { flex: 1; padding: .7rem; font-size: .73rem; text-transform: uppercase; background: none; border: 1px solid #e8e4de; border-radius: 3px; cursor: pointer; color: #666; }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-logo">labas. admin</div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products" class="active">Products</a>
        <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/users">Users</a>
        <a href="${pageContext.request.contextPath}/admin/settings">Settings</a>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/">&larr; Back to Store</a>
    </div>
</aside>

<main class="main-content">
    <header class="header">
        <div class="header-title">Products Management</div>
    </header>
    <div class="content-scroll">
        <div class="table-container">
            <div class="table-header">
                <h3>All Products</h3>
                <button class="btn-primary" onclick="openDrawer()">+ Add New Product</button>
            </div>
            <table>
                <thead><tr><th>Image</th><th>Name</th><th>Price</th><th>Stock</th><th>Actions</th></tr></thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty products}">
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td><img src="${product.imageUrl}" class="product-img" onerror="this.style.display='none'"/></td>
                                <td><c:out value="${product.name}" /></td>
                                <td><fmt:formatNumber value="${product.price}" pattern="#,##0.00" /> dh</td>
                                <td><c:out value="${product.stockQty}" /></td>
                                <td class="actions">
                                    <button class="btn-action" onclick='editProduct(${product.id}, "${product.name}", "${product.description}", ${product.price}, ${product.vatRate}, ${product.stockQty}, "${product.size}", "${product.imageUrl}", ${product.categoryId}, ${product.subcategoryId != null ? product.subcategoryId : "null"})'>Edit</button>
                                    <button class="btn-action danger" onclick="deleteProduct(${product.id})">Delete</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise><tr><td colspan="5">No products found.</td></tr></c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<div class="drawer-overlay" id="drawerOverlay" onclick="closeDrawer()"></div>
<div class="drawer" id="productDrawer">
    <div class="drawer-head"><span class="drawer-head-title" id="drawerTitle">New Product</span><button class="drawer-close" onclick="closeDrawer()">&times;</button></div>
    <form id="productForm" action="${pageContext.request.contextPath}/admin/products" method="POST">
        <input type="hidden" name="action" value="save" /><input type="hidden" id="pId" name="id" />
        <div class="drawer-body">
            <div class="form-section-label">Product Info</div>
            <div class="field"><label>Name *</label><input type="text" id="pName" name="name" required /></div>
            <div class="field"><label>Description</label><textarea id="pDescription" name="description"></textarea></div>
            <div class="field-row">
                <div class="field"><label>Price *</label><input type="number" id="pPrice" name="price" step="0.01" required /></div>
                <div class="field"><label>VAT Rate (%)</label><input type="number" id="pVat" name="vatRate" step="0.01" value="20.00" /></div>
            </div>
            <div class="field-row">
                <div class="field"><label>Stock *</label><input type="number" id="pStock" name="stockQty" required /></div>
                <div class="field"><label>Size</label>
                    <div class="size-pills"><span class="size-pill" data-size="XS">XS</span><span class="size-pill" data-size="S">S</span><span class="size-pill" data-size="M">M</span><span class="size-pill" data-size="L">L</span><span class="size-pill" data-size="XL">XL</span></div>
                    <input type="hidden" id="pSize" name="size" />
                </div>
            </div>
            <div class="form-section-label">Image</div>
            <div class="field"><label>Image URL</label><input type="url" id="pImageUrl" name="imageUrl" oninput="previewImage(this.value)" /></div>
            <div class="img-preview" id="imgPreview"><img id="imgPreviewEl" src="" /><span id="imgPlaceholder">Preview</span></div>
            <div class="form-section-label">Category</div>
            <div class="field"><label>Category *</label>
                <select id="pCategory" name="categoryId" onchange="updateSubcategories()" required>
                    <option value="">— Select —</option>
                    <c:forEach var="cat" items="${categories}"><option value="${cat.id}"><c:out value="${cat.name}" /></option></c:forEach>
                </select>
            </div>
            <div class="field"><label>Subcategory</label><select id="pSubcategory" name="subcategoryId"><option value="">— Select category first —</option></select></div>
        </div>
        <div class="drawer-foot"><button type="button" class="btn-ghost" onclick="closeDrawer()">Cancel</button><button type="submit" class="btn-primary">Save Product</button></div>
    </form>
</div>
<form id="deleteForm" action="${pageContext.request.contextPath}/admin/products" method="POST" style="display:none;"><input type="hidden" name="action" value="delete" /><input type="hidden" name="id" id="deleteId" /></form>
<script>
    const categorySubMap = {}; <c:forEach var="cat" items="${categories}">categorySubMap["${cat.id}"] = [<c:forEach var="sub" items="${cat.subcategories}" varStatus="vs">{ id: "${sub.id}", name: "${sub.name}" }<c:if test="${!vs.last}">,</c:if></c:forEach>];</c:forEach>
    function openDrawer(isEdit = false) { document.getElementById('drawerTitle').textContent = isEdit ? 'Edit Product' : 'New Product'; if (!isEdit) { document.getElementById('productForm').reset(); document.getElementById('pId').value = ''; document.getElementById('imgPreviewEl').style.display = 'none'; document.querySelectorAll('.size-pill').forEach(p => p.classList.remove('selected')); } document.getElementById('drawerOverlay').classList.add('open'); document.getElementById('productDrawer').classList.add('open'); }
    function closeDrawer() { document.getElementById('drawerOverlay').classList.remove('open'); document.getElementById('productDrawer').classList.remove('open'); }
    function previewImage(url) { const img = document.getElementById('imgPreviewEl'); if (url) { img.src = url; img.style.display = 'block'; } else { img.style.display = 'none'; } }
    document.querySelectorAll('.size-pill').forEach(pill => { pill.addEventListener('click', () => { pill.classList.toggle('selected'); document.getElementById('pSize').value = [...document.querySelectorAll('.size-pill.selected')].map(p => p.dataset.size).join(','); }); });
    function updateSubcategories(selectedSubId = null) { const catId = document.getElementById('pCategory').value; const subSel = document.getElementById('pSubcategory'); subSel.innerHTML = '<option value="">— Select —</option>'; const subs = categorySubMap[catId]; if (subs) { subs.forEach(s => { const opt = document.createElement('option'); opt.value = s.id; opt.textContent = s.name; if (selectedSubId && s.id == selectedSubId) opt.selected = true; subSel.appendChild(opt); }); } }
    function editProduct(id, name, desc, price, vat, stock, size, img, catId, subId) { openDrawer(true); document.getElementById('pId').value = id; document.getElementById('pName').value = name; document.getElementById('pDescription').value = desc; document.getElementById('pPrice').value = price; document.getElementById('pVat').value = vat; document.getElementById('pStock').value = stock; document.getElementById('pImageUrl').value = img; previewImage(img); document.getElementById('pCategory').value = catId; updateSubcategories(subId); document.querySelectorAll('.size-pill').forEach(p => p.classList.remove('selected')); if (size) size.split(',').forEach(s => { const pill = document.querySelector(`.size-pill[data-size="${s}"]`); if (pill) pill.classList.add('selected'); }); document.getElementById('pSize').value = size; }
    function deleteProduct(id) { if (confirm('Delete this product?')) { document.getElementById('deleteId').value = id; document.getElementById('deleteForm').submit(); } }
</script>
</body>
</html>
