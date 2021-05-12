package at.ac.univie.imagechecker.controller;

import at.ac.univie.imagechecker.handlers.EthereumHandler;
import at.ac.univie.imagechecker.handlers.RegisterImageHandler;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.util.EmailSender;
import at.ac.univie.imagechecker.util.ImageReadUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.mail.MessagingException;
import java.io.*;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = {"<origns if needed>"})
public class SerializeController {

    @Autowired
    RegisterImageHandler registerImageHandler;

    @Autowired
    EthereumHandler ethereumHandler;

    @Autowired
    EmailSender emailSender;

    @GetMapping(value = "api/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Test Token", description = "Test Token.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity test(@AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(BasicResponse.builder().message("Test " + principal + " " + principal.getName()).status(200).build());
    }

    @PostMapping(value = "/api/getTest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Serialize color histogram from image", description = "Serialize image color histogram.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity serializeHistogram(@Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file, String in) throws Exception {

        //return ResponseEntity.ok(BasicResponse.builder().message("Test: " + ethereumHandler.getTest()).status(200).build());

        log.info("Serialize Histogram entered");
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        try {
            IOUtils.copy(file[0].getInputStream(), byteArrayOutputStream1);
            byte[] img = byteArrayOutputStream1.toByteArray();
            registerImageHandler.setImage(img);
            Metadata metadata = registerImageHandler.getMetadata();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(metadata);
            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println(byteArrayOutputStream.toByteArray().length);
            ObjectInputStream objectInputStream = new ObjectInputStream(IOUtils.toInputStream(in, "UTF-8"));
            Metadata metadata1 = (Metadata)objectInputStream.readObject();
            objectInputStream.close();
            return ResponseEntity.ok(BasicResponse.builder().status(200).message("same " + metadata.equals(metadata1)).build());
        } catch (IOException | TikaException | SAXException | ClassNotFoundException e) {
            log.error("Exception in serialize histogram: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error reading image: " + e.getMessage()).build());
        }
    }

    @PostMapping(value = "/api/checkHistogram", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Serialize color histogram from image", description = "Serialize image color histogram.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity checkHistogram(@Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file, @RequestParam("addr") long addr) throws Exception {

        //return ResponseEntity.ok(BasicResponse.builder().status(200).message(ethereumHandler.setTest(test).toString()).build());

        log.info("Check Histogram entered");

        try {
            Mat image = ImageReadUtils.readInputStream(file[0].getInputStream());
            Mat histogram = registerImageHandler.getHistogram(image);
            Mat addrMat = new Mat(addr);
            log.info("Sizes: hist: {} addr: {}", histogram.size().area(), addrMat.size().area());
            return ResponseEntity.ok(BasicResponse.builder().status(200).message("Check: " + histogram.equals(addrMat)).build());
        } catch (IOException e) {
            log.error("Exception in serialize histogram: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error reading image: " + e.getMessage()).build());
        }
    }

    @GetMapping(value = "/api/sendEmail/{to}/{subj}/{text}")
    @Operation(summary = "Send Email", description = "Send email from ImageCheckerNotifier", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity sendEmail(@Parameter(description = "To") @PathVariable("to") String to, @Parameter(description = "Subject") @PathVariable("subj") String subj,
                                    @Parameter(description = "Text") @PathVariable("text") String text) {

        try {
            emailSender.sendIntegrityErrorMessage(to, subj, text);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/api/sendEmailNotif/{to}/{subj}/{text}/{user}/{img}/{sender}")
    @Operation(summary = "Send Email", description = "Send email from ImageCheckerNotifier", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity sendEmailNotif(@Parameter(description = "To") @PathVariable("to") String to, @Parameter(description = "Subject") @PathVariable("subj") String subj,
                                    @Parameter(description = "Text") @PathVariable("text") String text, @Parameter(description = "User") @PathVariable("user") String user,
                                         @Parameter(description = "Image") @PathVariable("img") String img, @Parameter(description = "Sender") @PathVariable("sender") String sender) {

        try {
            emailSender.sendNotificationMessage(to, subj, img, user, sender, text);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

}
