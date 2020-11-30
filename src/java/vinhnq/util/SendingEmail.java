/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.util;

import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Admin
 */
public class SendingEmail {

    private String email;
    private String password;

    public SendingEmail() {
    }

    public SendingEmail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void sendEmail() throws AddressException, MessagingException {
        String sysEmail = "twotoonine@gmail.com";
        String sysPassword = "migitoga";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sysEmail, sysPassword);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sysEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("J3.L.P0010 Email Verification Link");
        message.setText("Your verification link: " + "http://localhost:8084/J3.L.P0010//DispatcherServlet?btnAction=Verify&key1=" + email + "&key2=" + password);
        Transport.send(message);
    }

}
