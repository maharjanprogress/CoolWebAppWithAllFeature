package com.progress.coolProject.Services.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.progress.coolProject.DTO.LoginAndRegistration.Facebook.FacebookDebugTokenResponse;
import com.progress.coolProject.DTO.LoginAndRegistration.Facebook.FacebookUserInfo;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;
import com.progress.coolProject.Entity.Role;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Repo.RoleRepo;
import com.progress.coolProject.Repo.UserRepo;
import com.progress.coolProject.Services.Impl.IOAuth2Service;
import com.progress.coolProject.Services.JWT.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class OAuth2Service implements IOAuth2Service {

    @Value("${facebook.app-id}")
    private String facebookAppId;

    @Value("${facebook.app-secret}")
    private String facebookAppSecret;

    @Value("${google.client-id}")
    private String googleClientId;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTService jwtService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    @Override
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

    @Override
    @Transactional
    public LoginOutput loginWithFB(String fbToken) {
        try {
            // Step 1: Verify the token with Facebook
            FacebookDebugTokenResponse debugResponse = verifyFacebookToken(fbToken);

            if (debugResponse == null || debugResponse.getData() == null) {
                throw new IllegalArgumentException("Invalid Facebook token response");
            }

            FacebookDebugTokenResponse.FacebookTokenData tokenData = debugResponse.getData();

            // Check if token is valid
            if (!Boolean.TRUE.equals(tokenData.getIsValid())) {
                throw new IllegalArgumentException("Invalid Facebook token");
            }

            // Check if token belongs to our app
            if (!facebookAppId.equals(tokenData.getAppId())) {
                throw new IllegalArgumentException("Token does not belong to this application");
            }

            // Step 2: Get user information from Facebook
            FacebookUserInfo fbUserInfo = getFacebookUserInfo(fbToken);

            if (fbUserInfo == null || fbUserInfo.getEmail() == null) {
                throw new IllegalArgumentException("Could not retrieve user information from Facebook. Please ensure email permission is granted.");
            }

            // Step 3: Find user by email or create a new one
            User user = userRepo.findByEmail(fbUserInfo.getEmail())
                    .orElseGet(() -> createNewUserFromFacebook(fbUserInfo));

            return jwtService.generateLoginOutput(user);

        } catch (Exception e) {
            throw new RuntimeException("Facebook authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Verify Facebook access token
     */
    private FacebookDebugTokenResponse verifyFacebookToken(String accessToken) {
        String debugTokenUrl = String.format(
                "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s",
                accessToken,
                facebookAppId,
                facebookAppSecret
        );

        try {
            ResponseEntity<FacebookDebugTokenResponse> response = restTemplate.getForEntity(
                    debugTokenUrl,
                    FacebookDebugTokenResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Facebook token: " + e.getMessage(), e);
        }
    }

    /**
     * Get Facebook user information
     */
    private FacebookUserInfo getFacebookUserInfo(String accessToken) {
        String userInfoUrl = String.format(
                "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name&access_token=%s",
                accessToken
        );

        try {
            ResponseEntity<FacebookUserInfo> response = restTemplate.getForEntity(
                    userInfoUrl,
                    FacebookUserInfo.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Facebook user info: " + e.getMessage(), e);
        }
    }

    private User createNewUserFromGoogle(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String userName = generateUniqueUsername(email);

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

    private User createNewUserFromFacebook(FacebookUserInfo fbUserInfo) {
        String email = fbUserInfo.getEmail();
        String firstName = fbUserInfo.getFirstName() != null ? fbUserInfo.getFirstName() : "Facebook";
        String lastName = fbUserInfo.getLastName() != null ? fbUserInfo.getLastName() : "User";
        String userName = generateUniqueUsername(email);

        Role defaultRole = roleRepo.findByRoleAlias("CLIENT")
                .orElseThrow(() -> new RuntimeException("Default role 'CLIENT' not found."));

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUserName(userName);
        newUser.setRole(defaultRole);
        newUser.setIsActive(true);
        newUser.setPasswordHash(null); // OAuth users don't have passwords

        return userRepo.save(newUser);
    }

    /**
     * Generate a unique username from email
     */
    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        // Keep trying until we find a unique username
        while (userRepo.findByUserName(username).isPresent()) {
            username = baseUsername + "_" + counter;
            counter++;
        }

        return username;
    }
}
