package com.demirciyazilim.business.services;

import com.demirciyazilim.core.config.AppProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;
    private final String fallbackNotificationEmail;

    public EmailService(
            JavaMailSender mailSender,
            AppProperties appProperties,
            @Value("${spring.mail.properties.mail.smtp.from:${spring.mail.username:}}") String fallbackNotificationEmail
    ) {
        this.mailSender = mailSender;
        this.appProperties = appProperties;
        this.fallbackNotificationEmail = fallbackNotificationEmail;
    }

    public void sendContactFormEmail(String from, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String notificationEmail = resolveNotificationEmail();

        if (!StringUtils.hasText(notificationEmail)) {
            throw new MessagingException("Contact notification recipient is not configured");
        }

        helper.setFrom(from);
        helper.setTo(notificationEmail);
        helper.setSubject(subject != null && !subject.isEmpty() ? subject : "İletişim Formu Mesajı");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public String createContactFormEmailContent(String fullName, String email, String phone, String subject, String message) {
        return String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;">
                        <h2 style="color: #00796b; border-bottom: 1px solid #eee; padding-bottom: 10px;">Yeni İletişim Formu Mesajı</h2>
                        <p><strong>İsim Soyisim:</strong> %s</p>
                        <p><strong>E-posta:</strong> <a href="mailto:%s">%s</a></p>
                        <p><strong>Telefon:</strong> %s</p>
                        <p><strong>Konu:</strong> %s</p>
                        <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin-top: 15px;">
                            <p><strong>Mesaj:</strong></p>
                            <p>%s</p>
                        </div>
                        <p style="font-size: 12px; color: #999; margin-top: 20px; text-align: center;">
                            Bu e-posta %s web sitesi üzerinden gönderilmiştir.
                        </p>
                    </div>
                </body>
            </html>
            """, fullName, email, email, phone, subject != null ? subject : "-", message, appProperties.getRestaurantName());
    }

    public String createQuoteFormEmailContent(String fullName, String email, String phone, String service, String message) {
        return String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;">
                        <h2 style="color: #00796b; border-bottom: 1px solid #eee; padding-bottom: 10px;">Yeni Teklif Talebi</h2>
                        <p><strong>İsim Soyisim:</strong> %s</p>
                        <p><strong>E-posta:</strong> <a href="mailto:%s">%s</a></p>
                        <p><strong>Telefon:</strong> %s</p>
                        <p><strong>İlgilenilen Hizmet:</strong> %s</p>
                        <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin-top: 15px;">
                            <p><strong>Mesaj:</strong></p>
                            <p>%s</p>
                        </div>
                        <p style="font-size: 12px; color: #999; margin-top: 20px; text-align: center;">
                            Bu e-posta %s web sitesi üzerinden gönderilmiştir.
                        </p>
                    </div>
                </body>
            </html>
            """, fullName, email, email, phone, service, message, appProperties.getRestaurantName());
    }

    private String resolveNotificationEmail() {
        if (StringUtils.hasText(appProperties.getContact().getNotificationEmail())) {
            return appProperties.getContact().getNotificationEmail();
        }

        return fallbackNotificationEmail;
    }
}
