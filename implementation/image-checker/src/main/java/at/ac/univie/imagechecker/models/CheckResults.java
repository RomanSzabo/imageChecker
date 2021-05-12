package at.ac.univie.imagechecker.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckResults {

    private double keyPointSimilarity;
    private double histogramSimilarity;
    private double metadataSimilarity;
    private MetadataModel metadata;
    private String imageName;
    private String owner;
    private String ownerId;
    private String imageId;
    private boolean isTampered;

}
