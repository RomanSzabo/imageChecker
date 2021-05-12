package at.ac.univie.imagechecker.database.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "image", schema = "dbo", catalog = "imagechecker")
public class ImageEntity {
    private String imageId;
    private String imageOwner;
    private byte[] imageObjectModel;
    private byte[] imageMetadataModel;
    private Timestamp created;
    private UsersEntity usersByImageOwner;
    private Collection<MessagesEntity> messagesByImageId;

    @Id
    @Column(name = "imageId")
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Basic
    @Column(name = "imageOwner")
    public String getImageOwner() {
        return imageOwner;
    }

    public void setImageOwner(String imageOwner) {
        this.imageOwner = imageOwner;
    }

    @Basic
    @Column(name = "imageObjectModel")
    public byte[] getImageObjectModel() {
        return imageObjectModel;
    }

    public void setImageObjectModel(byte[] imageObjectModel) {
        this.imageObjectModel = imageObjectModel;
    }

    @Basic
    @Column(name = "imageMetadataModel")
    public byte[] getImageMetadataModel() {
        return imageMetadataModel;
    }

    public void setImageMetadataModel(byte[] imageMetadataModel) {
        this.imageMetadataModel = imageMetadataModel;
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

        ImageEntity that = (ImageEntity) o;

        if (imageId != null ? !imageId.equals(that.imageId) : that.imageId != null) return false;
        if (imageOwner != null ? !imageOwner.equals(that.imageOwner) : that.imageOwner != null) return false;
        if (!Arrays.equals(imageObjectModel, that.imageObjectModel)) return false;
        if (!Arrays.equals(imageMetadataModel, that.imageMetadataModel)) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageId != null ? imageId.hashCode() : 0;
        result = 31 * result + (imageOwner != null ? imageOwner.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(imageObjectModel);
        result = 31 * result + Arrays.hashCode(imageMetadataModel);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "imageOwner", referencedColumnName = "userId", updatable = false, insertable = false)
    public UsersEntity getUsersByImageOwner() {
        return usersByImageOwner;
    }

    public void setUsersByImageOwner(UsersEntity usersByImageOwner) {
        this.usersByImageOwner = usersByImageOwner;
    }

    @OneToMany(mappedBy = "imageByImage")
    public Collection<MessagesEntity> getMessagesByImageId() {
        return messagesByImageId;
    }

    public void setMessagesByImageId(Collection<MessagesEntity> messagesByImageId) {
        this.messagesByImageId = messagesByImageId;
    }
}
