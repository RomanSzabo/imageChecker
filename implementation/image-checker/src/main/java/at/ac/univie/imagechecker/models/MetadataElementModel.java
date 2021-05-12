package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetadataElementModel {

    private String key;
    private String value;

}
