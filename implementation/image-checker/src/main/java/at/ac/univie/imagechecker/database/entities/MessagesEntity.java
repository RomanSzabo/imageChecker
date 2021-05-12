package at.ac.univie.imagechecker.database.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages", schema = "dbo", catalog = "imagechecker")
public class MessagesEntity {
    private String messageId;
    private String sender;
    private String receiver;
    private String image;
    private String text;
    private UsersEntity usersByReceiver;
    private ImageEntity imageByImage;
    private Timestamp created;

    @Id
    @Column(name = "messageId")
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "sender")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "receiver")
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessagesEntity that = (MessagesEntity) o;

        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "userId", updatable = false, insertable = false)
    public UsersEntity getUsersByReceiver() {
        return usersByReceiver;
    }

    public void setUsersByReceiver(UsersEntity usersByReceiver) {
        this.usersByReceiver = usersByReceiver;
    }

    @ManyToOne
    @JoinColumn(name = "image", referencedColumnName = "imageId", updatable = false, insertable = false)
    public ImageEntity getImageByImage() {
        return imageByImage;
    }

    public void setImageByImage(ImageEntity imageByImage) {
        this.imageByImage = imageByImage;
    }
}
