package at.ac.univie.imagechecker.controller;

import at.ac.univie.imagechecker.handlers.RegisterImageHandler;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.models.MetadataElementModel;
import at.ac.univie.imagechecker.models.MetadataModel;
import at.ac.univie.imagechecker.util.ImageReadUtils;
import at.ac.univie.imagechecker.util.MetadataUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

/**
 * Extract data Rest endpoint controller
 */
@Slf4j
@RestController
@RequestMapping("/api/extract")
@CrossOrigin(origins = {"<origns if needed>"})
public class ExtractController {

    @Autowired
    RegisterImageHandler registerImageHandler;

    @PostMapping(value = "/metadata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Extract metadata from image", description = "Extract image metadata as list of them.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity extractMetadata(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file) {

        log.info("Extract Metadata entered by: {}", principal.getName() != null ? principal.getName() : "unknown");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(file[0].getInputStream(), byteArrayOutputStream);
            byte[] img = byteArrayOutputStream.toByteArray();
            registerImageHandler.setImage(img);
            Metadata metadata = registerImageHandler.getMetadata();
            MetadataModel resp = MetadataUtils.fromMetadataToMetadataModel(metadata);
            resp.getMetadata().add(MetadataElementModel.builder().key("File Name").value(file[0].getOriginalFilename()).build());
            return ResponseEntity.ok(resp);
        } catch (TikaException | IOException | SAXException e) {
            log.error("Error occurred of type: {} with message: {}", e.getClass(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error occurred of type: " + e.getClass() + " with message: " + e.getMessage()).build());
        }
    }

    @PostMapping(value = "/histogram", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Extract color histogram from image", description = "Extract image color histogram.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity extractHistogram(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file) {

        log.info("Extract Histogram entered by: {}", principal.getName() != null ? principal.getName() : "unknown");

        try {
            Mat image = ImageReadUtils.readInputStream(file[0].getInputStream());
            Mat histogram = registerImageHandler.getHistogram(image);

            return ResponseEntity.ok(histogram);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading image: " + e.getMessage());
        }
    }

    @PostMapping(value = "/features", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Extract features from image", description = "Extract features as image with marked features.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity extractFeatures(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given Image") @RequestParam("image") MultipartFile file) {
        log.info("Extract features entered by: {}", principal.getName() != null ? principal.getName() : "unknown");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Mat image = ImageReadUtils.readInputStream(file.getInputStream());
            BufferedImage bufferedImage = registerImageHandler.getFeaturesAsPicture(image);
            ImageIO.write(bufferedImage, "jpg", outputStream);
            return new ResponseEntity<byte[]>(outputStream.toByteArray(), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in extractFeatures: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in extractFeatures: " + e.getMessage()).build());
        }
    }

}
