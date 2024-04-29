package config.mail_config;

import config.BaseTest;
import config.CentralizePO;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ReportConfig {

    public static CentralizePO centralize(){
        return new CentralizePO(BaseTest.getDriver());
    }

    public void sendEmail() {
        String toEmail = "vidhi.bakraniya@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                try {
                    return new PasswordAuthentication(centralize().getValueFromFile("email"), centralize().getValueFromFile("app_password"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(centralize().getValueFromFile("email")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("test");

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("test");

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            File f = new File(System.getProperty("user.dir") + "\\report\\");
            File[] files = f.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            File chosenFile = null;
            if (files != null) {
                for (File file : files) {
                    if (file.lastModified() > lastModifiedTime) {
                        chosenFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }
                System.out.println(chosenFile);
            }
            DataSource source = new FileDataSource(chosenFile);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(String.valueOf(chosenFile));
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("message sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
