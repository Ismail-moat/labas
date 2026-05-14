<%@ page import="java.sql.*, com.labas.util.DBConnection" %>
<%
    try (Connection con = DBConnection.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM category")) {
        out.println("<h3>Categories in DB:</h3>");
        while (rs.next()) {
            out.println("<p>ID: " + rs.getInt("id") + " - Name: " + rs.getString("name") + "</p>");
        }
    } catch (Exception e) {
        out.println("Error: " + e.getMessage());
    }
%>
