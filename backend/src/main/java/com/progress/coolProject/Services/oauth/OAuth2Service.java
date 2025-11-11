package com.progress.coolProject.Services.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Repo.RoleRepo;
import com.progress.coolProject.Repo.UserRepo;
import com.progress.coolProject.Services.JWT.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class OAuth2Service {
    @Value("${google.client-id}")
    private String googleClientId;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTService jwtService;

    @Transactional
    public LoginOutput loginWithGoogle(String googleToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(googleToken);
        if (idToken == null) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();

        // Find user by email or create a new one
        User user = userRepo.findByEmail(email)
                .orElseGet(() -> createNewUserFromGoogle(payload));

        return jwtService.generateLoginOutput(user);
    }

    private User createNewUserFromGoogle(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String userName = email.split("@")[0] + "_" + System.currentTimeMillis() % 1000; // Create a unique username

        Role defaultRole = roleRepo.findByRoleAlias(("CLIENT"))
                .orElseThrow(() -> new RuntimeException("Default role 'CLIENT' not found."));

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUserName(userName);
        newUser.setRole(defaultRole);
        newUser.setIsActive(true);
        newUser.setPasswordHash(null);

        return userRepo.save(newUser);
    }
}
