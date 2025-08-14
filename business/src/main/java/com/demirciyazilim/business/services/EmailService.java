package com.demirciyazilim.business.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final String TO_EMAIL = "info@demirciyazilim.com"; // Alıcı e-posta adresi
    
    public void sendContactFormEmail(String from, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(from);
        helper.setTo(TO_EMAIL);
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
                            Bu e-posta Demirci Yazılım web sitesi üzerinden gönderilmiştir.
                        </p>
                    </div>
                </body>
            </html>
            """, fullName, email, email, phone, subject != null ? subject : "-", message);
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
                            Bu e-posta Demirci Yazılım web sitesi üzerinden gönderilmiştir.
                        </p>
                    </div>
                </body>
            </html>
            """, fullName, email, email, phone, service, message);
    }
} 