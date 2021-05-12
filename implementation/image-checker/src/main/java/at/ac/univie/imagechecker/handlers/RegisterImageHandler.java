package at.ac.univie.imagechecker.handlers;

import at.ac.univie.imagechecker.database.entities.ImageEntity;
import at.ac.univie.imagechecker.database.entities.UsersEntity;
import at.ac.univie.imagechecker.database.repositories.ImageRepository;
import at.ac.univie.imagechecker.database.repositories.UsersRepository;
import at.ac.univie.imagechecker.imageProcessing.FeatureProcessing;
import at.ac.univie.imagechecker.imageProcessing.HistogramProcessing;
import at.ac.univie.imagechecker.imageProcessing.MetadataProcessing;
import at.ac.univie.imagechecker.models.*;
import at.ac.univie.imagechecker.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.HighGui;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterImageHandler {

    private static final String FILE_NAME = "File Name";

    @Autowired
    EthereumHandler ethereumHandler;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EmailSender emailSender;

    private byte[] image;

    public Metadata getMetadata() throws TikaException, IOException, SAXException {
        return MetadataProcessing.extractMetadata(image);
    }

    public Mat getHistogram(Mat image) {
        return HistogramProcessing.extractHistogram(image);
    }

    public BufferedImage getFeaturesAsPicture(Mat image) throws Exception {
        Mat resImg = new Mat();
        MatOfKeyPoint keyPoints = FeatureProcessing.extractFeaturesForDrawing(image);
        Features2d.drawKeypoints(image, keyPoints, resImg);
        Image res = HighGui.toBufferedImage(resImg);
        BufferedImage bufferedImage = new BufferedImage(res.getWidth(null), res.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(res, null, null);
        return bufferedImage;
    }

    public int getImageSize(Mat image) throws Exception {
        FeatureModel featureModel = FeatureProcessing.extractFeatures(image);
        Mat histogram = HistogramProcessing.extractHistogram(image);
        Metadata metadata = MetadataProcessing.extractMetadata(this.image);
        ImageObjectModel imageObjectModel = MatTransformUtils.toImageObjectModel(featureModel, histogram);
        return MatTransformUtils.fromImageObjectModelToByteArray(imageObjectModel).length + MetadataUtils.fromMetadataToByteArray(metadata).length;
    }

    public SimilarityReportModel registerImage(Mat image, StoredOwnerModel owner, String fileName) throws Exception {
        FeatureModel featureModel = FeatureProcessing.extractFeatures(image);
        Mat histogram = HistogramProcessing.extractHistogram(image);
        Metadata metadata = MetadataProcessing.extractMetadata(this.image);
        metadata.set(FILE_NAME, fileName);
        SimilarityReportModel report = checkAgainstRegisteredImages(featureModel, histogram, metadata);
        ImageObjectModel imageObjectModel = MatTransformUtils.toImageObjectModel(featureModel, histogram);
        if (report.isPassed()) {
            String imageId = UUID.randomUUID().toString();
            Timestamp timestamp = new Timestamp(new Date().getTime());
            timestamp.setNanos(0);
            byte[] imageObjectModelBytes = MatTransformUtils.fromImageObjectModelToByteArray(imageObjectModel);
            byte[] metadataBytes = MetadataUtils.fromMetadataToByteArray(metadata);
            byte[] dbRowFingerprint = Utils.createFingerprint(ImageModelTransformUtils.fromImageModelToByteArray(ImageModel.builder()
                    .imageId(imageId)
                    .imageOwner(owner.getUserId())
                    .imageObjectModel(imageObjectModelBytes)
                    .imageMetadataModel(metadataBytes)
                    .created(timestamp).build()));
            log.info("Fingerprint \nbytes: {}\nsize: {}", dbRowFingerprint, dbRowFingerprint.length);
            log.info("Timestamp: {}", timestamp);
            if (!usersRepository.existsById(owner.getUserId())) {
                UsersEntity usersEntity = new UsersEntity();
                usersEntity.setUserId(owner.getUserId());
                usersEntity.setDisplayName(owner.getDisplayName());
                usersEntity.setEmail(owner.getEmail());
                usersEntity.setCreated(timestamp);
                usersRepository.save(usersEntity);
            }
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageId(imageId);
            imageEntity.setImageOwner(owner.getUserId());
            imageEntity.setImageObjectModel(imageObjectModelBytes);
            imageEntity.setImageMetadataModel(metadataBytes);
            imageEntity.setCreated(timestamp);
            imageRepository.save(imageEntity);
            log.info("Image size: {}", MatTransformUtils.fromImageObjectModelToByteArray(imageObjectModel).length + MetadataUtils.fromMetadataToByteArray(metadata).length + OwnerTransformUtils.fromStoredOwnerModelToByteArray(owner).length);
            try {
                TransactionReceipt transactionReceipt = ethereumHandler.newImage(imageId, dbRowFingerprint);
                log.info(transactionReceipt.toString());
            } catch (Exception e) {
                log.error("Register Image Exception: {}", e.getMessage());
                imageRepository.delete(imageEntity);
                report.setRegistered(false);
                return report;
            }
            report.setRegistered(true);
        } else {
            report.setRegistered(false);
        }
        return report;
    }

    protected SimilarityReportModel checkAgainstRegisteredImages(FeatureModel img2, Mat histogram, Metadata metadata) throws Exception {
        log.info("Checking similarity");
        boolean isTampered = false;
        List<ImageEntity> images = imageRepository.findAll();
        log.info("Images size {}", images.size());
        SimilarityReportModel result = SimilarityReportModel.builder().checkResultsList(new ArrayList<>()).registered(false).build();
        if (images.size() == 0) {
            result.setPassed(true);
            result.setRegistered(false);
            result.setTampered(isTampered);
            return result;
        }
        StringBuilder img = new StringBuilder();
        for (ImageEntity entity : images) {
            //check integrity
            byte[] fingerprint = ethereumHandler.getImage(entity.getImageId());
            byte[] dbFingerprint = Utils.createFingerprint(ImageModelTransformUtils.fromImageModelToByteArray(ImageModel.builder()
                    .imageId(entity.getImageId())
                    .imageOwner(entity.getImageOwner())
                    .imageObjectModel(entity.getImageObjectModel())
                    .imageMetadataModel(entity.getImageMetadataModel())
                    .created(entity.getCreated()).build()));
            log.info("Timestamp: {}", entity.getCreated());
            if (!Arrays.equals(fingerprint, dbFingerprint)) {
                log.error("Corruption in DB detected on image: {}, fingerpint from blockchain: {}, fingerprint calculated: {}", entity.getImageId(), fingerprint, dbFingerprint);
                //email?
                isTampered = true;
            }
            ImageObjectModel img1ImgObjModel = ImageModelTransformUtils.fromByteArrayToImageObjectModel(entity.getImageObjectModel());
            Metadata img1Metadata = MetadataUtils.fromByteArrayToMetadata(entity.getImageMetadataModel());
            FeatureModel img1 = FeatureModel.builder()
                    .sizeOfKeyPoints(img1ImgObjModel.getKeypointSize())
                    .descriptors(MatTransformUtils.fromMatModel(img1ImgObjModel.getDescriptors()))
                    .build();
            double similarity = FeatureProcessing.getSimilarityRatio(img1, img2);
            double histSimilarity = HistogramProcessing.getSimilarityRatio(MatTransformUtils.fromMatModel(img1ImgObjModel.getHistogram()), histogram);
            double metadataSimilarity = MetadataProcessing.getMetadataSimilarity(img1Metadata, metadata);
            log.info("Similarity: owner: {}, SURF: {}, histogram: {}", entity.getImageOwner(), similarity, histSimilarity);
            //add similar to list, similar if ratio is more then 0.8 and histogram more than 0.9
            if (similarity > 0.8 || (histSimilarity > 0.9 && similarity > 0.6)) {
                boolean isTamperedEntity = false;
                if (!Arrays.equals(fingerprint, dbFingerprint)) {
                    isTamperedEntity = true;
                    img.append(entity.getImageId()).append("; ");
                }
                result.addCheckResult(CheckResults.builder()
                        .owner(entity.getUsersByImageOwner().getDisplayName())
                        .ownerId(entity.getUsersByImageOwner().getUserId())
                        .histogramSimilarity(histSimilarity)
                        .keyPointSimilarity(similarity)
                        .metadataSimilarity(metadataSimilarity)
                        .metadata(MetadataUtils.fromMetadataToMetadataModel(img1Metadata))
                        .imageName(img1Metadata.get(FILE_NAME))
                        .imageId(entity.getImageId())
                        .isTampered(isTamperedEntity)
                        .build());
            }
        }
        if (isTampered) {
            emailSender.sendIntegrityErrorMessage("romco.sz@gmail.com", "Image Integrity Error", img.toString());
        }
        result.setPassed(!(result.getCheckResultsList().size() > 0));
        result.setTampered(isTampered);
        return result;
    }

}
