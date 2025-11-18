package com.progress.coolProject.Configurations;

import com.progress.coolProject.Services.JWT.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final CorsProperties corsProperties;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(corsProperties.getAllowedOriginsArray())
                .withSockJS()
        ;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Only authenticate on CONNECT command (when user first connects)
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Get Authorization header from WebSocket connection
                    String authToken = accessor.getFirstNativeHeader("Authorization");

                    if (authToken != null && authToken.startsWith("Bearer ")) {
                        String token = authToken.substring(7);

                        try {
                            // Extract username from token
                            String username = jwtService.extractUserName(token);

                            // Check if token is not expired
                            if (username != null && !jwtService.isTokenExpired(token)) {
                                // Load user details
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                                // Create authentication object
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities()
                                        );

                                // Set the user in the accessor
                                accessor.setUser(authentication);

                                System.out.println("WebSocket authenticated for user: " + username);
                            } else {
                                System.out.println("WebSocket authentication failed: Token expired or invalid");
                            }
                        } catch (Exception e) {
                            System.out.println("WebSocket authentication error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("WebSocket connection without Authorization header");
                    }
                }

                return message;
            }
        });
    }

}
