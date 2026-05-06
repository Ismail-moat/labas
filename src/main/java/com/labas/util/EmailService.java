package com.labas.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private static final String SMTP_HOST = System.getProperty("mail.smtp.host", "smtp.gmail.com");
    private static final String SMTP_PORT = System.getProperty("mail.smtp.port", "587");
    private static final String SMTP_USER = System.getProperty("mail.smtp.user", "your-email@gmail.com");
    private static final String SMTP_PASS = System.getProperty("mail.smtp.pass", "your-app-password");
    private static final String FROM_NAME = "Labas E-Commerce";

    private static final ExecutorService EMAIL_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "email-sender");
        t.setDaemon(true);
        return t;
    });

    private static final EmailService INSTANCE = new EmailService();

    public static EmailService getInstance() { return INSTANCE; }
    private EmailService() {}

    public void sendOrderConfirmation(String toEmail, String clientName, int orderId,
                                      java.util.List<com.labas.model.OrderItem> items,
                                      java.math.BigDecimal totalIncl) {
        String subject = "Confirmation de commande #" + orderId + " — Labas";
        String body    = buildOrderEmailBody(clientName, orderId, items, totalIncl);
        sendAsync(toEmail, subject, body);
    }

    private void sendAsync(String to, String subject, String htmlBody) {
        EMAIL_EXECUTOR.submit(() -> {
            try {
                sendEmail(to, subject, htmlBody);
            } catch (Exception e) {
                LOGGER.warning("[EMAIL] Failed to send to " + to + ": " + e.getMessage());
            }
        });
    }

    private void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(SMTP_USER, FROM_NAME, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(SMTP_USER));
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");

        Transport.send(message);
        LOGGER.info("[EMAIL] Sent order confirmation to " + to);
    }

    private String buildOrderEmailBody(String clientName, int orderId,
                                       java.util.List<com.labas.model.OrderItem> items,
                                       java.math.BigDecimal totalIncl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;color:#333;max-width:600px;margin:auto;'>");
        sb.append("<h2 style='color:#2c3e50;'>Merci pour votre commande, ").append(escapeHtml(clientName)).append(" !</h2>");
        sb.append("<p>Votre commande <strong>#").append(orderId).append("</strong> a bien été enregistrée.</p>");
        sb.append("<h3>Récapitulatif :</h3>");
        sb.append("<table style='width:100%;border-collapse:collapse;'>");
        sb.append("<tr style='background:#f5f5f5;'><th style='text-align:left;padding:8px;border:1px solid #ddd;'>Produit</th>");
        sb.append("<th style='padding:8px;border:1px solid #ddd;'>Qté</th>");
        sb.append("<th style='padding:8px;border:1px solid #ddd;'>Total TTC</th></tr>");
        if (items != null) {
            for (com.labas.model.OrderItem item : items) {
                sb.append("<tr><td style='padding:8px;border:1px solid #ddd;'>")
                  .append(escapeHtml(item.getProduct() != null ? item.getProduct().getName() : "Produit"))
                  .append("</td><td style='text-align:center;padding:8px;border:1px solid #ddd;'>")
                  .append(item.getQuantity())
                  .append("</td><td style='text-align:right;padding:8px;border:1px solid #ddd;'>")
                  .append(String.format("%.2f €", item.getAmountIncl()))
                  .append("</td></tr>");
            }
        }
        sb.append("</table>");
        sb.append("<p style='text-align:right;font-size:1.1em;'><strong>Total TTC : ")
          .append(String.format("%.2f €", totalIncl)).append("</strong></p>");
        sb.append("<hr/><p style='color:#888;font-size:0.85em;'>Labas E-Commerce — Ne pas répondre à cet email.</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
