package com.strava.charts.auth;

import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.service.exception.BadRequestException;
import javastrava.api.v3.service.exception.UnauthorizedException;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AuthorisationAPI {

    @FormUrlEncoded
    @POST("/oauth/token")
    public TokenResponse tokenExchange(@Field("client_id") final Integer clientId, @Field("client_secret") final String clientSecret,
                                       @Field("code") final String code)  throws BadRequestException, UnauthorizedException;
}
