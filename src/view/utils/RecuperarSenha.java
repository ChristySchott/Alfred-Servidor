package view.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class RecuperarSenha {
    
    public Boolean enviarEmail(String enderecoDestinatario, String senhaEnvio) {
        String enderecoEnvio = "alfredplataforma@gmail.com";

        Properties propriedades = new Properties();
        propriedades.put("mail.smtp.host", "smtp.gmail.com");
        propriedades.put("mail.smtp.socketFactory.port", "465");
        propriedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        propriedades.put("mail.smtp.auth", "true");
        propriedades.put("mail.smtp.port", "465");

        Session sessao = Session.getDefaultInstance(propriedades, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alfredplataforma@gmail.com", "alfred123@");
            }
        });

        try {
            MimeMessage message = new MimeMessage(sessao);
            message.setFrom(new InternetAddress(enderecoEnvio));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(enderecoDestinatario));
            message.setSubject("[Alfred] Recuperação de senha");
            message.setText("Olá. Conforme solicitado, sua senha é: " + senhaEnvio);

            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}
