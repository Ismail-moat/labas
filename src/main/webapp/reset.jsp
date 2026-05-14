<%@ page import="com.labas.dao.UserDAO" %>
<%@ page import="com.labas.util.PasswordUtil" %>
<%@ page import="com.labas.model.User" %>
<%@ page import="com.labas.util.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reset Admin Password</title>
</head>
<body>
<%
    try {
        DBConnection.init(application);
        UserDAO userDAO = new UserDAO();
        String email = "admin@shop.com";
        String newPass = "admin123";
        
        out.println("<h2>Diagnostic</h2>");
        out.println("<ul>");
        out.println("<li><b>DB URL:</b> " + application.getInitParameter("dbUrl") + "</li>");
        out.println("<li><b>DB User:</b> " + application.getInitParameter("dbUser") + "</li>");
        out.println("</ul>");

        // List all users to see what we have
        out.println("<h3>Users in Database:</h3><ul>");
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT email, role FROM users")) {
            while (rs.next()) {
                out.println("<li>" + rs.getString("email") + " (" + rs.getString("role") + ")</li>");
            }
        } catch (Exception e) {
            out.println("<li style='color:red'>Could not list users: " + e.getMessage() + "</li>");
        }
        out.println("</ul>");

        User user = userDAO.findByEmail(email);
        if (user != null) {
            out.println("<p>User <b>" + email + "</b> found. Resetting password...</p>");
            String hashed = PasswordUtil.hashPassword(newPass);
            user.setPassword(hashed);
            boolean success = userDAO.update(user);
            if (success) {
                out.println("<h1 style='color:green'>Success!</h1>");
                out.println("<p>Password for <b>" + email + "</b> reset to: <b>" + newPass + "</b></p>");
            } else {
                out.println("<h1 style='color:red'>Error!</h1>");
                out.println("<p>Failed to update user.</p>");
            }
        } else {
            out.println("<p style='color:orange'>User <b>" + email + "</b> not found. Creating admin user...</p>");
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(PasswordUtil.hashPassword(newPass));
            newUser.setRole("admin");
            int id = userDAO.saveUser(newUser);
            if (id > 0) {
                out.println("<h1 style='color:green'>Success!</h1>");
                out.println("<p>Admin user created with email: <b>" + email + "</b> and password: <b>" + newPass + "</b></p>");
            } else {
                out.println("<h1 style='color:red'>Error!</h1>");
                out.println("<p>Could not create admin user. Check MySQL logs or table structure.</p>");
            }
        }
    } catch (Exception e) {
        out.println("<h1 style='color:red'>Exception!</h1>");
        out.println("<pre style='background:#fee; padding:10px; border:1px solid red'>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    }
%>
<br>
<a href="pages/login.jsp">Go to Login</a>
</body>
</html>
