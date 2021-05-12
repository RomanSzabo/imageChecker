package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;
import org.apache.tika.metadata.Metadata;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class ImageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imageId;
    private String imageOwner;
    private byte[] imageObjectModel;
    private byte[] imageMetadataModel;
    private Timestamp created;

}
