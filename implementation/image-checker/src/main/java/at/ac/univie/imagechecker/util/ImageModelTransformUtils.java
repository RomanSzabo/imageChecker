package at.ac.univie.imagechecker.util;

import at.ac.univie.imagechecker.models.ImageModel;
import at.ac.univie.imagechecker.models.ImageObjectModel;
import at.ac.univie.imagechecker.models.StoredOwnerModel;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ImageModelTransformUtils {

    public static byte[] fromImageModelToByteArray(ImageModel model) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
        objectOutputStream.writeObject(model);
        gzipOutputStream.finish();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static ImageObjectModel fromByteArrayToImageObjectModel(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
        ImageObjectModel imageObjectModel = (ImageObjectModel) objectInputStream.readObject();
        objectInputStream.close();
        return imageObjectModel;
    }

}
