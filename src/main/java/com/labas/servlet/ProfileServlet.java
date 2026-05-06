package com.labas.servlet;

import com.labas.dao.UserDAO;
import com.labas.model.Client;
import com.labas.model.Order;
import com.labas.model.User;
import com.labas.service.OrderService;
import com.labas.service.ProfileService;
import com.labas.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class ProfileServlet extends HttpServlet {

    private final ProfileService profileService = new ProfileService();
    private final UserDAO        userDAO        = new UserDAO();
    private final OrderService   orderService   = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        List<Order> orders = orderService.getOrdersByUserId(userId);
        request.setAttribute("orders", orders);

        if (request.getParameter("succes") != null) {
            request.setAttribute("succes", true);
        }

        CsrfUtil.getToken(session);
        request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Requête invalide. Veuillez réessayer.");
            request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
            return;
        }

        int    userId = (int)    session.getAttribute("userId");
        String email  = (String) session.getAttribute("email");
        String ip     = getClientIp(request);

        String firstName    = request.getParameter("firstName");
        String lastName     = request.getParameter("lastName");
        String username     = request.getParameter("username");
        String phone        = request.getParameter("phone");
        String address      = request.getParameter("address");
        String city         = request.getParameter("city");
        String zipCode      = request.getParameter("zipCode");
        String newEmail     = request.getParameter("email");
        String newPassword  = request.getParameter("newPassword");

        User currentUser = userDAO.findByEmail(email);
        String currentHashedPassword = (currentUser != null) ? currentUser.getPassword() : "";

        User user = new User();
        user.setIdUser(userId);
        user.setEmail((newEmail != null && !newEmail.isBlank()) ? newEmail.trim().toLowerCase() : email);

        if (newPassword != null && !newPassword.isBlank()) {

            String policyError = PasswordUtil.validatePolicy(newPassword);
            if (policyError != null) {
                request.setAttribute("error", policyError);
                request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
                return;
            }
            user.setPassword(PasswordUtil.hashPassword(newPassword));
            AuditLogger.logPasswordChange(userId, ip);
        } else {
            user.setPassword(currentHashedPassword);
        }

        String avatarUrl = null;
        try {
            Part avatarPart = request.getPart("avatar");
            if (FileUploadUtil.hasFile(avatarPart)) {
                String uploadDir = getServletContext().getRealPath("/uploads");
                avatarUrl = FileUploadUtil.saveImage(avatarPart, uploadDir, "avatars");
                AuditLogger.logUpload(email, avatarPart.getSubmittedFileName(), avatarUrl);
            }
        } catch (IllegalArgumentException e) {
            AuditLogger.logSuspiciousUpload(email, "avatar", e.getMessage());
            request.setAttribute("error", "Avatar invalide: " + e.getMessage());
            request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
            return;
        }

        Client client = new Client();
        client.setIdUser(userId);
        client.setFirstName(firstName != null ? firstName.trim() : "");
        client.setLastName(lastName   != null ? lastName.trim()  : "");
        client.setUsername(username   != null ? username.trim()  : "");
        client.setPhone(phone         != null ? phone.trim()     : "");
        client.setAddress(address     != null ? address.trim()   : "");
        client.setCity(city           != null ? city.trim()      : "");
        client.setZipCode(zipCode     != null ? zipCode.trim()   : "");
        client.setAvatarUrl(avatarUrl); 

        boolean success = profileService.updateProfile(user, client);

        if (success) {
            session.setAttribute("email",     user.getEmail());
            session.setAttribute("firstName", firstName);
            session.setAttribute("lastName",  lastName);
            session.setAttribute("username",  username);
            session.setAttribute("phone",     phone);
            session.setAttribute("address",   address);
            session.setAttribute("city",      city);
            session.setAttribute("zipCode",   zipCode);
            if (avatarUrl != null) session.setAttribute("avatarUrl", avatarUrl);

            CsrfUtil.rotate(session);
            response.sendRedirect(request.getContextPath() + "/profile?succes=1");
        } else {
            request.setAttribute("error", "Une erreur est survenue. Veuillez réessayer.");
            request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
