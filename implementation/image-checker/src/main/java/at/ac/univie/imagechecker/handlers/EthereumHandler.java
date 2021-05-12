package at.ac.univie.imagechecker.handlers;

import at.ac.univie.imagechecker.models.ImageModel;
import at.ac.univie.imagechecker.models.ImageObjectModel;
import at.ac.univie.imagechecker.models.StoredOwnerModel;
import at.ac.univie.imagechecker.util.Images;
import at.ac.univie.imagechecker.util.PoAGasProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Class wrapping Web3J communication with blockchain
 */
@Slf4j
public class EthereumHandler {

    // For production or non static, inject from application.properties
    private static final String ETHEREUM_RPC_ENDPOINT = "<RPC>";
    private static final String PRIVATE_KEY = "<PK>";
    private static final String IMAGES_ADDRESS = "<Address>";

    private final Images images;

    /**
     * Constructs class activating Web3J connection
     * Using custom GasProvider
     */
    public EthereumHandler() {
        Web3j web3j = Web3j.build(new HttpService(ETHEREUM_RPC_ENDPOINT));
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        this.images = Images.load(IMAGES_ADDRESS, web3j, credentials, new PoAGasProvider());
    }

    public boolean isValid() throws IOException {
        return images.isValid();
    }

    /**
     * Activating "newImage()" method of smart contract. Registering new image within blockchain, sending new transaction
     * @param imageId id of image
     * @param fingerprint sha256 fingerprint of data
     * @return TransactionReceipt
     * @throws Exception if error produced
     */
    public TransactionReceipt newImage(String imageId, byte[] fingerprint) throws Exception {
        return images.newImage(imageId, fingerprint).send();
    }

    /**
     * Get image representation from smart contract.
     * @param id image id
     * @return byte[] fingerprint of image elements
     * @throws Exception if error occurs
     */
    public byte[] getImage(String id) throws Exception {
        return images.getImage(id).send();
    }

    /**
     * Get all images from smart contract. Loop through image[] in smart contract. Workaround since Web3J is not supporting ABIv2 encoder
     * @return List<ImageModel>
     * @throws Exception if error occurs
     */
    /*
    public List<ImageModel> getImages() throws Exception {
        BigInteger length = getImagesLength();
        List<ImageModel> result = new ArrayList<>();
        for (BigInteger i = BigInteger.valueOf(0); i.compareTo(length) < 0; i = i.add(BigInteger.ONE)) {
            Tuple3<byte[], byte[], byte[]> image = getImage(i);
            ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(image.component1())));
            StoredOwnerModel ownerModel = (StoredOwnerModel) objectInputStream.readObject();
            objectInputStream.close();
            objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(image.component2())));
            ImageObjectModel imageObjectModel = (ImageObjectModel) objectInputStream.readObject();
            objectInputStream.close();
            objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(image.component3())));
            Metadata metadata = (Metadata)objectInputStream.readObject();
            objectInputStream.close();
            result.add(
                    ImageModel
                            .builder()
                            .name(ownerModel)
                            .image(imageObjectModel)
                            .metadata(metadata)
                            .build()
            );
        }
        return result;
    }*/

}
