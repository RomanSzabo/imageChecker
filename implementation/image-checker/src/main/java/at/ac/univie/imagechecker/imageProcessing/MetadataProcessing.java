package at.ac.univie.imagechecker.imageProcessing;

import at.ac.univie.imagechecker.util.Utils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class MetadataProcessing {

    /**
     * Method for extracting metadata from input image. Using Apache Tika library and parsers for the task
     * @param image Input image as input stream
     * @return extracted metadata
     * @throws IOException If input stream is null
     * @throws TikaException If error during parsing
     * @throws SAXException If error during parsing
     */
    public static Metadata extractMetadata(byte[] image) throws IOException, TikaException, SAXException {
        log.info("Extracting metadata");
        Tika tika = new Tika();
        Metadata metadata = new Metadata();
        Parser parser;
        ParseContext parseContext = new ParseContext();
        ContentHandler contentHandler = new ToXMLContentHandler();
        String mimeType;

        try {
            mimeType = tika.detect(image);
            log.info(mimeType);
            if (mimeType.equals("image/jpeg")) parser = new JpegParser();
            else parser = new ImageParser();
            metadata.set(Metadata.CONTENT_TYPE, mimeType);
            parser.parse(new ByteArrayInputStream(image), contentHandler, metadata,parseContext);
        } catch (TikaException e) {
            log.error("Exception during extracting metadata (TikaException): {}", e.getMessage());
            throw e;
        } catch (SAXException e) {
            log.error("Exception during extracting metadata (SAXException): {}", e.getMessage());
            throw e;
        }

        return metadata;
    }

    public static double getMetadataSimilarity(Metadata img1, Metadata img2) {
        String[] names = img2.names();
        int sameMetadata = 0;
        for (String name : names) {
            if (Utils.isNotNull(img1.get(name))) {
                sameMetadata += img1.get(name).equals(img2.get(name)) ? 1 : 0;
            }
        }
        return (double)sameMetadata/img1.size();
    }

}
