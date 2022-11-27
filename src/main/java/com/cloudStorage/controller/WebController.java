package com.cloudStorage.controller;

import com.cloudStorage.exeptions.StorageException;
import com.cloudStorage.service.UserAuthenticationService;
import com.cloudStorage.transfer.Request;
import com.cloudStorage.transfer.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class WebController {
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private final UserAuthenticationService userAuthenticationService;

    public WebController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping(value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Request> login(@Valid @RequestBody Request authorization) throws StorageException {
        String authToken = userAuthenticationService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new Request(authToken), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(LOGOUT)
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        userAuthenticationService.logout(authToken);
        return HttpStatus.OK;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage(), 400));
    }
}