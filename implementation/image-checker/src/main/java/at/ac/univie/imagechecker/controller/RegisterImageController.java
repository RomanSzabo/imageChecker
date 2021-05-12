package at.ac.univie.imagechecker.controller;

import at.ac.univie.imagechecker.database.entities.UsersEntity;
import at.ac.univie.imagechecker.database.repositories.UsersRepository;
import at.ac.univie.imagechecker.handlers.RegisterImageHandler;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.models.SimilarityReportModel;
import at.ac.univie.imagechecker.models.StoredOwnerModel;
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
import java.sql.Timestamp;
import java.util.Date;

/**
 * Register Image Rest endpoint controller
 */
@Slf4j
@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = {"<origns if needed>"})
public class RegisterImageController {

    @Autowired
    RegisterImageHandler registerImageHandler;

    @Autowired
    UsersRepository usersRepository;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register image to the system", description = "Register image to the system.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimilarityReportModel.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity registerImage(@AuthenticationPrincipal Principal principal,
                                        @Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file,
                                        @Parameter(description = "Display Name") @RequestParam("displayName") String displayName,
                                        @Parameter(description = "E-Mail") @RequestParam("email") String email ) {
        String userId = principal.getName() != null ? principal.getName() : "unknown";
        log.info("Register image by: {}", userId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(file[0].getInputStream(), byteArrayOutputStream);
            byte[] img = byteArrayOutputStream.toByteArray();
            Mat image = ImageReadUtils.readInputStream(new ByteArrayInputStream(img));
            registerImageHandler.setImage(img);
            if (principal.getName() == null) throw new AccessControlException("User principal is empty. Abort.");
            SimilarityReportModel result = registerImageHandler.registerImage(image, StoredOwnerModel.builder().displayName(displayName).email(email).userId(userId).build(), file[0].getOriginalFilename());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in register image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in register image: " + e.getMessage()).build());
        }
    }

    @PutMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update User Display Name", description = "Update DB after User has changed display name in AAD.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity updateUser(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given display name") @RequestParam("name") String name) {
        String userId = principal.getName() != null ? principal.getName() : "unknown";
        log.info("Check image size by: {}", userId);
        try {
            UsersEntity user = usersRepository.getOne(userId);
            user.setDisplayName(name);
            user.setModified(new Timestamp(new Date().getTime()));
            usersRepository.save(user);
            return ResponseEntity.ok(BasicResponse.builder().status(200).message("User updated").build());
        } catch (Exception e) {
            log.error("Error during User Update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in update user: " + e.getMessage()).build());
        }
    }

    @PostMapping(value = "/util/checkSize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Check image descriptors size", description = "Check if image will fit to transaction", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity checkSize(@AuthenticationPrincipal Principal principal, @Parameter(description = "Given Image") @RequestParam("image") MultipartFile[] file) {
        log.info("Check image size by: {}", principal.getName() != null ? principal.getName() : "unknown");
        try {
            Mat image = ImageReadUtils.readInputStream(file[0].getInputStream());
            return ResponseEntity.ok(BasicResponse.builder().status(200).message(String.valueOf(registerImageHandler.getImageSize(image))).build());
        } catch (Exception e) {
            log.error("Error in register image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BasicResponse.builder().status(500).message("Error in check image size: " + e.getMessage()).build());
        }
    }

}
