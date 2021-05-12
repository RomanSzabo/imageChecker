package at.ac.univie.imagechecker.handlers;

import at.ac.univie.imagechecker.imageProcessing.FeatureProcessing;
import at.ac.univie.imagechecker.imageProcessing.HistogramProcessing;
import at.ac.univie.imagechecker.imageProcessing.MetadataProcessing;
import at.ac.univie.imagechecker.models.FeatureModel;
import at.ac.univie.imagechecker.models.SimilarityReportModel;
import org.apache.tika.metadata.Metadata;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class CheckImageHandler {

    @Autowired
    private RegisterImageHandler registerImageHandler;

    @Autowired
    EthereumHandler ethereumHandler;

    private byte[] image;

    public SimilarityReportModel createImageCheckReport(Mat image) throws Exception {
        FeatureModel featureModel = FeatureProcessing.extractFeatures(image);
        Mat histogram = HistogramProcessing.extractHistogram(image);
        Metadata metadata = MetadataProcessing.extractMetadata(this.image);
        return registerImageHandler.checkAgainstRegisteredImages(featureModel, histogram, metadata);
    }

    public SimilarityReportModel createPublicImageCheckReport(Mat image) throws Exception {
        FeatureModel featureModel = FeatureProcessing.extractFeatures(image);
        Mat histogram = HistogramProcessing.extractHistogram(image);
        Metadata metadata = MetadataProcessing.extractMetadata(this.image);
        SimilarityReportModel reportModel = registerImageHandler.checkAgainstRegisteredImages(featureModel, histogram, metadata);
        reportModel.setCheckResultsList(new ArrayList<>());
        return reportModel;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
