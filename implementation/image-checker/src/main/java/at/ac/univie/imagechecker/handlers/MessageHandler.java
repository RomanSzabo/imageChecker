package at.ac.univie.imagechecker.handlers;

import at.ac.univie.imagechecker.database.entities.ImageEntity;
import at.ac.univie.imagechecker.database.entities.MessagesEntity;
import at.ac.univie.imagechecker.database.entities.UsersEntity;
import at.ac.univie.imagechecker.database.repositories.ImageRepository;
import at.ac.univie.imagechecker.database.repositories.MessageRepository;
import at.ac.univie.imagechecker.database.repositories.UsersRepository;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.models.MessageModel;
import at.ac.univie.imagechecker.util.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MessageHandler {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EmailSender emailSender;

    public BasicResponse sendNewMessage(String from, String to, String img, String imgName, String text) {
        ImageEntity imageEntity = imageRepository.getOne(img);
        UsersEntity recipient = usersRepository.getOne(to);
        MessagesEntity messagesEntity = new MessagesEntity();
        messagesEntity.setMessageId(UUID.randomUUID().toString());
        messagesEntity.setSender(from);
        messagesEntity.setReceiver(to);
        messagesEntity.setImage(img);
        messagesEntity.setText(text);
        messagesEntity.setCreated(new Timestamp(new Date().getTime()));
        try {
            emailSender.sendNotificationMessage(recipient.getEmail(), "ImageChecker Notification", imgName, recipient.getDisplayName(), from, text);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return BasicResponse.builder().status(500).message("Error in sending notification e-mail: " + e.getMessage()).build();
        }
        try {
            emailSender.sendSendNotificationMessage(from, "ImageChecker Confirmation", imgName, recipient.getDisplayName(), from, text);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error during sending notification email: {}", e.getMessage());
        }
        messageRepository.save(messagesEntity);
        return BasicResponse.builder().status(200).message("Notification Sent").build();
    }

    public List<MessageModel> getMessagesForUser(String userId) {
        List<MessageModel> messageModels = new ArrayList<>();
        messageRepository.findAllByReceiver(userId).forEach(messagesEntity -> {
            messageModels.add(MessageModel.builder()
                    .from(messagesEntity.getSender())
                    .image(messagesEntity.getImage())
                    .text(messagesEntity.getText())
                    .date(new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss").format(messagesEntity.getCreated())).build());
        });
        return messageModels;
    }

}
