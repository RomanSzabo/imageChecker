package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
public class ImageObjectModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private double keypointSize;
    private MatModel descriptors;
    private MatModel histogram;

}
