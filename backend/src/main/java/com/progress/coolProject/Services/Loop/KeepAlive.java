package com.progress.coolProject.Services.Loop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.progress.coolProject.StringConstants.PRODUCTION_URL;


@Service
@Slf4j
public class KeepAlive {
    @Autowired
    private RestTemplate restTemplate;

    private static final String SERVER_CHECK_URL = PRODUCTION_URL+"/serverCheck";

    @Scheduled(fixedRate = 30000)
    public void sendServerCheck() {
        try {
            restTemplate.getForEntity(
                    SERVER_CHECK_URL,
                    Void.class
            );
            log.info("Server check sent successfully");

        } catch (RestClientException e) {
            log.error("Failed to send server check request: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during server check: {}", e.getMessage());
        }
    }
}
