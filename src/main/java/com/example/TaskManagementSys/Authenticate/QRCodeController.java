package com.example.TaskManagementSys.Authenticate;

import com.example.TaskManagementSys.Service.QRCodeService;
import com.example.TaskManagementSys.Service.UserService;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "QR Code Generator Controller", description = "API for generating QR code for google authenticator")
@SecurityRequirement(name = "Email")
@RestController
public class QRCodeController {

    private final QRCodeService qrCodeService;
    private final UserService userService;

    public QRCodeController(QRCodeService qrCodeService, UserService userService) {
        this.qrCodeService = qrCodeService;
        this.userService = userService;
    }

    @Operation(summary = "Generate QR Code", description = "Additional Layer of Security For Login - QR code present on /generate-qr, Using google authenticator to provide OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Return QR Code & Save Secret to database"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @GetMapping(value = "/generate-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(@Valid @RequestParam String email) throws WriterException, IOException {
        try {
            String secretKey = userService.generateSecretKey(email);
            String qrCodeUrl = userService.generateQrCodeUrl(email, secretKey);

            byte[] qrCodeImage = qrCodeService.generateQRCodeImage(qrCodeUrl, 200, 200);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeImage);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
