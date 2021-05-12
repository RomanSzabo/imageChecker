package at.ac.univie.imagechecker.imageProcessing;

import at.ac.univie.imagechecker.models.FeatureModel;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class HistogramProcessing {

    /**
     * Method for extracting histogram from given picture which can be later used in comparison
     * How to extract and compare histograms with OpenCV: https://docs.opencv.org/master/d8/dc8/tutorial_histogram_comparison.html
     * @param image Input image
     * @return Mat
     */
    public static Mat extractHistogram(Mat image) {
        Mat hsvBase = new Mat();
        Imgproc.cvtColor(image, hsvBase, Imgproc.COLOR_BGR2HSV);

        int[] histSize = {50, 60};
        float[] ranges = {0, 180, 0, 256};
        int[] channels = {0, 1};

        Mat histogram = new Mat();
        List<Mat> hsvList = Arrays.asList(hsvBase);
        Imgproc.calcHist(hsvList, new MatOfInt(channels), new Mat(),histogram, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histogram, histogram, 0, 1, Core.NORM_MINMAX);

        return histogram;
    }

    /**
     * Method for getting similarity of color histograms of image. Using correlation
     * results 0-1, closer to 1, more similar histograms
     * https://docs.opencv.org/master/d8/dc8/tutorial_histogram_comparison.html
     * @param histogram1 Mat histogram of image 1
     * @param histogram2 Mar histogram of image 2
     * @return double correlation
     */
    public static double getSimilarityRatio(Mat histogram1, Mat histogram2) {
        return Imgproc.compareHist(histogram1, histogram2, Imgproc.HISTCMP_CORREL);
    }

}
