package com.labas.servlet;

import com.labas.model.Product;
import com.labas.service.CategoryService;
import com.labas.service.ProductService;
import com.labas.util.AuditLogger;
import com.labas.util.CsrfUtil;
import com.labas.util.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class AdminProductsServlet extends HttpServlet {

    private final ProductService  productService  = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("products",   productService.getAllProducts());
        request.setAttribute("categories", categoryService.getAllCategoriesWithSubcategories());
        CsrfUtil.getToken(request.getSession(true));
        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CsrfUtil.isValid(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        HttpSession session  = request.getSession(false);
        String adminEmail    = session != null ? (String) session.getAttribute("email") : "unknown";
        String action        = request.getParameter("action");

        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && idStr.matches("\\d+")) {
                int id = Integer.parseInt(idStr);
                productService.deleteProduct(id);
                AuditLogger.logAdminAction(adminEmail, "DELETE_PRODUCT", "id=" + id);
            }

        } else if ("save".equals(action)) {
            String idStr = request.getParameter("id");

            String name      = request.getParameter("name");
            String priceStr  = request.getParameter("price");
            String stockStr  = request.getParameter("stockQty");
            String catIdStr  = request.getParameter("categoryId");

            if (name == null || name.isBlank() || priceStr == null || stockStr == null) {
                request.setAttribute("error", "Champs obligatoires manquants.");
                doGet(request, response);
                return;
            }

            Product p = new Product();
            if (idStr != null && !idStr.isBlank() && idStr.matches("\\d+")) {
                p.setId(Integer.parseInt(idStr));
            }
            p.setName(name.trim());
            p.setDescription(request.getParameter("description"));

            try {
                p.setPrice(new BigDecimal(priceStr));
                String vatStr = request.getParameter("vatRate");
                if (vatStr == null || vatStr.isBlank()) {
                    p.setVatRate(new BigDecimal("20.00"));
                } else {
                    p.setVatRate(new BigDecimal(vatStr));
                }
                p.setStockQty(Integer.parseInt(stockStr));
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Valeurs numériques invalides (Prix, TVA ou Stock).");
                doGet(request, response);
                return;
            }

            p.setSize(request.getParameter("size"));

            if (catIdStr != null && !catIdStr.isBlank() && catIdStr.matches("\\d+")) {
                p.setCategoryId(Integer.parseInt(catIdStr));
            }
            String subIdStr = request.getParameter("subcategoryId");
            if (subIdStr != null && !subIdStr.isBlank() && subIdStr.matches("\\d+")) {
                p.setSubcategoryId(Integer.parseInt(subIdStr));
            }

            try {
                Part imagePart = request.getPart("productImage");
                if (FileUploadUtil.hasFile(imagePart)) {
                    String uploadDir = getServletContext().getRealPath("/uploads");
                    String imageUrl  = FileUploadUtil.saveImage(imagePart, uploadDir, "products");
                    p.setImageUrl(imageUrl);
                    AuditLogger.logUpload(adminEmail, imagePart.getSubmittedFileName(), imageUrl);
                } else {

                    String existingUrl = request.getParameter("imageUrl");
                    if (existingUrl != null && !existingUrl.isBlank()) {

                        if (existingUrl.contains("..") || existingUrl.contains("//")) {
                            p.setImageUrl(null);
                        } else {
                            p.setImageUrl(existingUrl);
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                AuditLogger.logSuspiciousUpload(adminEmail, "product-image", e.getMessage());
                request.setAttribute("error", "Image invalide: " + e.getMessage());
                doGet(request, response);
                return;
            }

            if (p.getId() == null) {
                productService.addProduct(p);
                AuditLogger.logAdminAction(adminEmail, "CREATE_PRODUCT", "name=" + p.getName());
            } else {
                productService.updateProduct(p);
                AuditLogger.logAdminAction(adminEmail, "UPDATE_PRODUCT", "id=" + p.getId() + " name=" + p.getName());
            }
        }

        CsrfUtil.rotate(request.getSession(true));
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}
