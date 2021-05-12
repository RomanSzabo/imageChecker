package at.ac.univie.imagechecker.models;

import lombok.Builder;
import lombok.Data;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

@Data
@Builder
public class FeatureModel {

    private double sizeOfKeyPoints;
    private Mat descriptors;

}
