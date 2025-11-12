package com.progress.coolProject.DTO.LoginAndRegistration.Facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookDebugTokenResponse {
    private FacebookTokenData data;

    @Data
    public static class FacebookTokenData {
        @JsonProperty("app_id")
        private String appId;

        private String type;
        private String application;

        @JsonProperty("data_access_expires_at")
        private Long dataAccessExpiresAt;

        @JsonProperty("expires_at")
        private Long expiresAt;

        @JsonProperty("is_valid")
        private Boolean isValid;

        @JsonProperty("user_id")
        private String userId;
    }
}
