package services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService {
    private static final String FROM_EMAIL = "ziedfilali2710@gmail.com"; // Change this to your email
    private static final String PASSWORD = "hlmg uaad fnqs qaif"; // Change this to your email password

    public static void send(String recipientEmail, String mailHtml, String subject) throws MessagingException {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");



        // Set up authentication
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        };

        // Create a session
        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(true);

        // Create MimeMessage
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);

        // Create MimeBodyPart
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(mailHtml, "text/html");

        // Create Multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        // Set content
        message.setContent(multipart);

        // Send the message
        Transport.send(message);
    }
}