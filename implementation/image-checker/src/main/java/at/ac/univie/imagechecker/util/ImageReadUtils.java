package at.ac.univie.imagechecker.util;

import org.apache.commons.io.IOUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.io.InputStream;

/**
 * Util class to help with reading images
 */
public class ImageReadUtils {

    /**
     * Read java InputStream to OpenCV Mat data type for further processing.
     * Solution proposed https://answers.opencv.org/question/31855/java-api-loading-image-from-any-java-inputstream/
     * @param stream stream to transform
     * @return Mat image
     */
    public static Mat readInputStream(InputStream stream) throws IOException {

        byte[] imgAsByte = IOUtils.toByteArray(stream);

        return Imgcodecs.imdecode(new MatOfByte(imgAsByte), Imgcodecs.IMREAD_COLOR);
    }

}
