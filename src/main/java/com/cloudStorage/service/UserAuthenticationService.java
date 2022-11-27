package com.cloudStorage.service;

import com.cloudStorage.exeptions.StorageException;
import com.cloudStorage.repository.CloudRepository;
import com.cloudStorage.security.JwtTokenUtil;
import com.cloudStorage.transfer.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private final CloudRepository cloudRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

@Autowired
    public UserAuthenticationService (CloudRepository cloudRepository, AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.cloudRepository = cloudRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String login(Request request) throws StorageException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userPrincipal);
            cloudRepository.login(jwt, userPrincipal);
            return jwt;
        } catch (BadCredentialsException e) {
            throw new StorageException("Bad credentials");
        }
    }

    public void logout(String authToken) {
        cloudRepository.logout(authToken).orElseThrow(() -> new Error("No valid logout"));
    }
}