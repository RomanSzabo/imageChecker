package at.ac.univie.imagechecker.controller;

import at.ac.univie.imagechecker.database.entities.MessagesEntity;
import at.ac.univie.imagechecker.handlers.MessageHandler;
import at.ac.univie.imagechecker.models.BasicResponse;
import at.ac.univie.imagechecker.models.MessageModel;
import at.ac.univie.imagechecker.models.SimilarityReportModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.BadStringOperationException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = {"<origns if needed>"})
public class MessageController {

    @Autowired
    MessageHandler messageHandler;

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send new message to user", description = "Messaging functionality. Send new message.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity sendNewMessage(@AuthenticationPrincipal Principal principal,
                                         @Parameter(description = "Recipient User Id") @RequestParam("recipient") String recipient,
                                         @Parameter(description = "E-Mail address of sender") @RequestParam("sender") String sender,
                                         @Parameter(description = "Image Id of image in interest") @RequestParam("image") String image,
                                         @Parameter(description = "Image Name of image in interest") @RequestParam("imageName") String imageName,
                                         @Parameter(description = "Message") @RequestParam("text") String text) {
        log.info("New email message accessed by: {}", principal.getName() != null ? principal.getName() : "unknown");
        return ResponseEntity.ok(messageHandler.sendNewMessage(sender, recipient, image, imageName, text));
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user messages", description = "Messaging functionality. Get User Messages.", responses = {
            @ApiResponse(responseCode = "200", description = "OK Response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageModel.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponse.class))})
    }, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity getUserMessages(@AuthenticationPrincipal Principal principal) {
        String userId = principal.getName() != null ? principal.getName() : "unknown";
        log.info("Get messages for user: {}", userId);
        return ResponseEntity.ok(messageHandler.getMessagesForUser(userId));
    }
}
