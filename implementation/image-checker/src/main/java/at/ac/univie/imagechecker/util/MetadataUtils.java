package at.ac.univie.imagechecker.util;

import at.ac.univie.imagechecker.models.MetadataElementModel;
import at.ac.univie.imagechecker.models.MetadataModel;
import org.apache.tika.metadata.Metadata;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MetadataUtils {

    public static byte[] fromMetadataToByteArray(Metadata metadata) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
        objectOutputStream.writeObject(metadata);
        gzipOutputStream.finish();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static Metadata fromByteArrayToMetadata(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
        Metadata metadata = (Metadata)objectInputStream.readObject();
        objectInputStream.close();
        return metadata;
    }

    public static MetadataModel fromMetadataToMetadataModel(Metadata metadata) {
        MetadataModel resp = MetadataModel.builder().metadata(new ArrayList<>()).build();
        for (String name : metadata.names()) {
            resp.getMetadata().add(MetadataElementModel.builder().key(name).value(metadata.get(name)).build());
        }
        return resp;
    }

}
