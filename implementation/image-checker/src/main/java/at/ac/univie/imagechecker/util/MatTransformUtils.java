package at.ac.univie.imagechecker.util;

import at.ac.univie.imagechecker.models.FeatureModel;
import at.ac.univie.imagechecker.models.ImageObjectModel;
import at.ac.univie.imagechecker.models.MatModel;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class MatTransformUtils {

    public static MatModel toMatModel(Mat mat) {
        float[] data = new float[mat.cols() * mat.rows() * (int)mat.elemSize()];
        mat.get(0, 0, data);
        log.info("Size of float[]: {}", data.length*4);
        return MatModel.builder()
                .cols(mat.cols())
                .rows(mat.rows())
                .type(mat.type())
                .data(data)
                .build();
    }

    public static Mat fromMatModel(MatModel model) {
        Mat mat = new Mat(model.getRows(), model.getCols(), model.getType());
        mat.put(0, 0, model.getData());
        return mat;
    }

    public static ImageObjectModel toImageObjectModel(MatOfKeyPoint keyPoint, Mat descriptors, Mat histogram) {
        return ImageObjectModel
                .builder()
                .keypointSize(keyPoint.size().area())
                .descriptors(toMatModel(descriptors))
                .histogram(toMatModel(histogram))
                .build();
    }

    public static ImageObjectModel toImageObjectModel(FeatureModel featureModel, Mat histogram) {
        return ImageObjectModel
                .builder()
                .keypointSize(featureModel.getSizeOfKeyPoints())
                .descriptors(toMatModel(featureModel.getDescriptors()))
                .histogram(toMatModel(histogram))
                .build();
    }

    public static byte[] fromImageObjectModelToByteArray(ImageObjectModel model) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
        objectOutputStream.writeObject(model);
        gzipOutputStream.finish();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

}
