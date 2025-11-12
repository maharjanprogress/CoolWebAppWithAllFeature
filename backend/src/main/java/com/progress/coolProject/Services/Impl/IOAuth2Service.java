package com.progress.coolProject.Services.Impl;

import com.progress.coolProject.DTO.LoginAndRegistration.LoginOutput;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IOAuth2Service {
    LoginOutput loginWithGoogle(String googleToken) throws GeneralSecurityException, IOException;
    LoginOutput loginWithFB(String fbToken);
}
