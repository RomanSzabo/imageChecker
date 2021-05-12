package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class StoredOwnerModel implements Serializable {

    private static final long serialVersionUID = 1L;

    String userId;
    String displayName;
    String email;

}
