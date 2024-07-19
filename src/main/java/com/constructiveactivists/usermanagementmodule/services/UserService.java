package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.controllers.user.configuration.SocialProperties;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SocialProperties socialProperties;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<GoogleIdToken.Payload> google(TokenRequest tokenDto) {
        try {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(socialProperties.getGoogleClientId()));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<User> facebook(TokenRequest tokenDto){
    try {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        User user = facebook.fetchObject("me", User.class);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
}
