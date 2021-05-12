package at.ac.univie.imagechecker.database.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "dbo", catalog = "imagechecker")
public class UsersEntity {
    private String userId;
    private String displayName;
    private String email;
    private Timestamp created;
    private Timestamp modified;
    private Collection<ImageEntity> imagesByUserId;
    private Collection<MessagesEntity> messagesByUserId;

    @Id
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "created")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "modified")
    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (modified != null ? !modified.equals(that.modified) : that.modified != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "usersByImageOwner")
    public Collection<ImageEntity> getImagesByUserId() {
        return imagesByUserId;
    }

    public void setImagesByUserId(Collection<ImageEntity> imagesByUserId) {
        this.imagesByUserId = imagesByUserId;
    }

    @OneToMany(mappedBy = "usersByReceiver")
    public Collection<MessagesEntity> getMessagesByUserId() {
        return messagesByUserId;
    }

    public void setMessagesByUserId(Collection<MessagesEntity> messagesByUserId) {
        this.messagesByUserId = messagesByUserId;
    }
}
