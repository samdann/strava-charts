package com.strava.charts.service;

import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.rest.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorizationService {

    private static final String CLIENT_SECRET = "2a79d4e93f324224faa2f67cd4e92483f1946b02";
    private static final int CLIENT_ID = 40865;

    public String authorize(String code) {
        final TokenResponse tokenResponse = API.authorisationInstance().tokenExchange(CLIENT_ID, CLIENT_SECRET, code);
        return tokenResponse.getAccessToken();

    }
}
