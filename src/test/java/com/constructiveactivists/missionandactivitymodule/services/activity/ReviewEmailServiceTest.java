package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ReviewEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ReviewEmailService reviewEmailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendFormEmail_Success() throws MessagingException {
        String recipientEmail = "test@example.com";
        Integer activityId = 1;
        String activityTitle = "Test Activity";

        ActivityEntity activity = new ActivityEntity();
        activity.setId(activityId);
        activity.setTitle(activityTitle);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

        reviewEmailService.sendFormEmail(recipientEmail, activityId);

        verify(mailSender, times(1)).send(any(MimeMessage.class));

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(recipientEmail);
        helper.setSubject("Formulario de Reseña de: Test Activity");
        verify(mailSender).send(mimeMessage);
    }
}
