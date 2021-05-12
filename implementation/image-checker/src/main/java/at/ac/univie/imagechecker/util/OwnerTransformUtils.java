package at.ac.univie.imagechecker.util;

import at.ac.univie.imagechecker.models.StoredOwnerModel;
import org.apache.tika.metadata.Metadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

public class OwnerTransformUtils {

    public static byte[] fromStoredOwnerModelToByteArray(StoredOwnerModel model) throws IOException {
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
