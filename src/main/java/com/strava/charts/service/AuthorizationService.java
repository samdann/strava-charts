package com.strava.charts.service;

import io.swagger.client.ApiClient;
import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.rest.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("secret.properties")
@Slf4j
public class AuthorizationService {

    @Value("${strava.client_id}")
    private int clientId;

    @Value("${strava.client_secret}")
    private String clientSecret;

    private ApiClient apiClient;
    private String accessToken;

    public ApiClient getAuthorizedApiClient(final String code) {
        if (accessToken == null) {
            accessToken = authorize(code);
        }

        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        apiClient.setAccessToken(accessToken);

        return apiClient;
    }

    private String authorize(String code) {
        final TokenResponse tokenResponse = API.authorisationInstance()
                .tokenExchange(clientId, clientSecret, code);
        return tokenResponse.getAccessToken();

    }
}
