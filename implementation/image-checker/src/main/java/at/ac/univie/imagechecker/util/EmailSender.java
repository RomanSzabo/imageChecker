package at.ac.univie.imagechecker.util;

import org.bytedeco.opencv.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Value("classpath:/image/logo.png")
    Resource imageFile;

    public void sendIntegrityErrorMessage(String to, String subject, String image) throws MessagingException, UnsupportedEncodingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("image", image);
        String htmlBody = thymeleafTemplateEngine.process("integrity-error-message.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    public void sendNotificationMessage(String to, String subject, String image, String user, String sender, String message) throws MessagingException, UnsupportedEncodingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("user", user);
        thymeleafContext.setVariable("image", image);
        thymeleafContext.setVariable("sender", sender);
        thymeleafContext.setVariable("text", message);
        String htmlBody = thymeleafTemplateEngine.process("notification-message.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    public void sendSendNotificationMessage(String to, String subject, String image, String user, String sender, String message) throws MessagingException, UnsupportedEncodingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("user", user);
        thymeleafContext.setVariable("image", image);
        thymeleafContext.setVariable("sender", sender);
        thymeleafContext.setVariable("text", message);
        String htmlBody = thymeleafTemplateEngine.process("send-message.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
        helper.setFrom("<email>", "ImageChecker Notifier");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("logo.png", imageFile);
        javaMailSender.send(message);
    }

}
