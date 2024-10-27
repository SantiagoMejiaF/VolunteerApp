package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QRCodeServiceTest {

    @InjectMocks
    private QRCodeService qrCodeService;

    @BeforeEach
    void setUp() {
        qrCodeService = new QRCodeService();
    }

    @Test
    void testGenerateQrCode_Success() {
        try (MockedStatic<MatrixToImageWriter> mockedMatrixToImageWriter = mockStatic(MatrixToImageWriter.class)) {
            String data = "https://volunteer-app.online/api/v1/back-volunteer-app/test";
            byte[] expectedBytes = new byte[]{0, 1, 2, 3};

            mockedMatrixToImageWriter.when(() -> MatrixToImageWriter.writeToStream(any(), eq("PNG"), any()))
                    .thenAnswer(invocation -> {
                        ByteArrayOutputStream stream = invocation.getArgument(2);
                        stream.write(expectedBytes);
                        return null;
                    });

            byte[] result = qrCodeService.generateQrCode(data);

            assertNotNull(result);
            assertArrayEquals(expectedBytes, result);
        }
    }

    @Test
    void testGenerateQrCode_Error() {
        QRCodeService qrCodeServiceSpy = spy(new QRCodeService());

        doThrow(new BusinessException("Error generating QR code")).when(qrCodeServiceSpy).generateQrCode(anyString());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            qrCodeServiceSpy.generateQrCode("test");
        });

        assertEquals("Error generating QR code", exception.getMessage());
    }

    @Test
    void testGenerateCheckInQrCode_Success()  {
        Integer activityId = 1;

        try (MockedStatic<MatrixToImageWriter> mockedMatrixToImageWriter = mockStatic(MatrixToImageWriter.class)) {
            byte[] expectedBytes = new byte[]{0, 1, 2, 3};

            mockedMatrixToImageWriter.when(() -> MatrixToImageWriter.writeToStream(any(), eq("PNG"), any()))
                    .thenAnswer(invocation -> {
                        ByteArrayOutputStream stream = invocation.getArgument(2);
                        stream.write(expectedBytes);
                        return null;
                    });

            byte[] result = qrCodeService.generateCheckInQrCode(activityId);

            assertNotNull(result);
            assertArrayEquals(expectedBytes, result);
        }
    }

    @Test
    void testGenerateCheckOutQrCode_Success(){
        Integer activityId = 1;

        try (MockedStatic<MatrixToImageWriter> mockedMatrixToImageWriter = mockStatic(MatrixToImageWriter.class)) {
            byte[] expectedBytes = new byte[]{0, 1, 2, 3};

            mockedMatrixToImageWriter.when(() -> MatrixToImageWriter.writeToStream(any(), eq("PNG"), any()))
                    .thenAnswer(invocation -> {
                        ByteArrayOutputStream stream = invocation.getArgument(2);
                        stream.write(expectedBytes);
                        return null;
                    });

            byte[] result = qrCodeService.generateCheckOutQrCode(activityId);

            assertNotNull(result);
            assertArrayEquals(expectedBytes, result);
        }
    }
}
