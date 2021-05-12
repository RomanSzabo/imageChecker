package at.ac.univie.imagechecker.controller;

import at.ac.univie.imagechecker.handlers.CheckImageHandler;
import at.ac.univie.imagechecker.handlers.RegisterImageHandler;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.models.SimilarityReportModel;
import at.ac.univie.imagechecker.util.ImageReadUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.AccessControlException;
import java.security.Principal;

/**
 * Check Image Rest endpoint controller
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"<origns if needed>"})
public class CheckController {

    @Autowired
    CheckImageHandler checkImageHandler;

    @PostMapping(value = "/api/check/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register image to the system", description = "Register image to the system.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimilarityReportModel.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity checkImage(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file) {
        log.info("Check image by: {}", principal.getName() != null ? principal.getName() : "unknown");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(file[0].getInputStream(), byteArrayOutputStream);
            byte[] img = byteArrayOutputStream.toByteArray();
            Mat image = ImageReadUtils.readInputStream(new ByteArrayInputStream(img));
            checkImageHandler.setImage(img);
            if (principal.getName() == null) throw new AccessControlException("User principal is empty. Abort.");
            SimilarityReportModel result = checkImageHandler.createImageCheckReport(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in check image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in register image: " + e.getMessage()).build());
        }
    }

    @PostMapping(value = "/public/check/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register image to the system", description = "Register image to the system.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimilarityReportModel.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    })
    public ResponseEntity checkImagePublic(@Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file) {
        log.info("Check image by unknown user - public endpoint");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(file[0].getInputStream(), byteArrayOutputStream);
            byte[] img = byteArrayOutputStream.toByteArray();
            Mat image = ImageReadUtils.readInputStream(new ByteArrayInputStream(img));
            checkImageHandler.setImage(img);
            SimilarityReportModel result = checkImageHandler.createPublicImageCheckReport(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in check image public: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in register image: " + e.getMessage()).build());
        }
    }

}
