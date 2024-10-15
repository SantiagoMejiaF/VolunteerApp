package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {

    private static final String BASE_URL = "https://volunteer-app.online/api/v1/back-volunteer-app";

    public byte[] generateQrCode(String data) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new BusinessException("Error generating QR code", e);
        }
    }

    public byte[] generateCheckInQrCode(Integer activityId) {
        String data = BASE_URL + "/attendances/google/auth/checkin?activityId=" + activityId;
        return generateQrCode(data);
    }

    public byte[] generateCheckOutQrCode(Integer activityId) {
        String data = BASE_URL + "/attendances/google/auth/checkout?activityId=" + activityId;
        return generateQrCode(data);
    }
}
