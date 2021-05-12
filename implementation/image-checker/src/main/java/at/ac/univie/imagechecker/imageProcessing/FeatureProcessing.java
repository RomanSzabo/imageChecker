package at.ac.univie.imagechecker.imageProcessing;

import at.ac.univie.imagechecker.models.FeatureModel;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FeatureProcessing {

    /**
     * Method for extracting features from images based on SURF method
     * How to use SURF extraction in OpenCV: https://docs.opencv.org/3.4/d5/d6f/tutorial_feature_flann_matcher.html,
     * https://docs.opencv.org/3.4/d7/d66/tutorial_feature_detection.html
     * @param image Input image
     * @return List of Keypoints
     * @throws Exception if descriptors or keypoints are empty
     */
    public static FeatureModel extractFeatures(Mat image) throws Exception {
        log.info("Extract features entered");
        double hessianThreshold = 400;
        int nOctaves = 4, nOctaveLayers = 3;
        boolean extended = false, upright = false;
        SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        detector.detectAndCompute(image, new Mat(), keypoints1, descriptors1);
        if (descriptors1.empty() || keypoints1.empty()) {
            throw new Exception("Error with image processing: " + descriptors1.size().area() + " descriptors found, "
                    + keypoints1.size().area() + " keypoint found. If one is 0, no usable descriptors or keypoints found to process image further.");
        }
        return FeatureModel.builder().sizeOfKeyPoints(keypoints1.size().area()).descriptors(descriptors1).build();
    }

    public static MatOfKeyPoint extractFeaturesForDrawing(Mat image) throws Exception {
        log.info("Extract features for drawing entered");
        double hessianThreshold = 400;
        int nOctaves = 4, nOctaveLayers = 3;
        boolean extended = false, upright = false;
        SURF detector = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        detector.detectAndCompute(image, new Mat(), keypoints1, descriptors1);
        if (descriptors1.empty() || keypoints1.empty()) {
            throw new Exception("Error with image processing: " + descriptors1.size().area() + " descriptors found, "
                    + keypoints1.size().area() + " keypoint found. If one is 0, no usable descriptors or keypoints found to process image further.");
        }
        return keypoints1;
    }

    /**
     * Method for getting similarity ratio of 2 images.
     * How to match images in OpenCV: https://docs.opencv.org/3.4/d5/d6f/tutorial_feature_flann_matcher.html
     * @param img1 FeatureModel image
     * @param img2 FeatureModel image
     * @return double ratio
     */
    public static double getSimilarityRatio(FeatureModel img1, FeatureModel img2) {
        log.info("Get Similarity Entered");
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        List<MatOfDMatch> knnMatches = new ArrayList<>();
        matcher.knnMatch(img1.getDescriptors(), img2.getDescriptors(), knnMatches, 2);
        float ratioThresh = 0.7f;
        List<DMatch> listOfGoodMatches = new ArrayList<>();
        for (MatOfDMatch knnMatch : knnMatches) {
            if (knnMatch.rows() > 1) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0]);
                }
            }
        }
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(listOfGoodMatches);
        return (double)listOfGoodMatches.size()/img1.getSizeOfKeyPoints();
    }

}
