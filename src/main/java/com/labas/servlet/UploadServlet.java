package com.labas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

@WebServlet("/uploads/*")
public class UploadServlet extends HttpServlet {

    private Path uploadsRoot;

    @Override
    public void init() throws ServletException {
        String realPath = getServletContext().getRealPath("/uploads");
        if (realPath == null) {
            throw new ServletException("Cannot resolve uploads directory. " +
                "Ensure the server is not running from an unexpanded WAR.");
        }
        uploadsRoot = Paths.get(realPath).toAbsolutePath().normalize();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); 
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path requested = uploadsRoot.resolve(pathInfo.substring(1)).normalize();
        if (!requested.startsWith(uploadsRoot)) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = requested.toFile();
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String filename  = file.getName().toLowerCase();
        String mimeType;
        if      (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) mimeType = "image/jpeg";
        else if (filename.endsWith(".png"))                               mimeType = "image/png";
        else if (filename.endsWith(".webp"))                              mimeType = "image/webp";
        else if (filename.endsWith(".gif"))                               mimeType = "image/gif";
        else {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType(mimeType);
        response.setHeader("Cache-Control", "public, max-age=86400"); 
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentLengthLong(file.length());

        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }
}
