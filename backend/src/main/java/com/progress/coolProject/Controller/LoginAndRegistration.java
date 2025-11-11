package com.progress.coolProject.Controller;

import com.progress.coolProject.DTO.LoginAndRegistration.GoogleLoginRequest;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginDTO;
import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;
import com.progress.coolProject.DTO.LoginAndRegistration.UserDTO;
import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.Services.Impl.IUserService;
import com.progress.coolProject.Services.oauth.OAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginAndRegistration {
    @Autowired
    private IUserService userService;

    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        LoginOutput output = userService.verify(loginDTO);
        return new ResponseEntity<>(ResponseDTO.success("User Login Sucessfull", output), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return new ResponseEntity<>(ResponseDTO.success("User Registered Sucessfully", userDTO), HttpStatus.OK);
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<ResponseDTO> googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            LoginOutput loginOutput = oAuth2Service.loginWithGoogle(request.getToken());
            return new ResponseEntity<>(ResponseDTO.success("Login with Google successful", loginOutput), HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            return new ResponseEntity<>(ResponseDTO.error("Google authentication failed: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/serverCheck")
    public void serverCheck() {
        log.info("Server Check");
    }
}
