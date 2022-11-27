package com.cloudStorage.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    @JsonProperty("auth-token")
    private String authToken;

    private String login;
    private String password;

    public Request(String authToken) {
        this.authToken = authToken;
    }

    public Request(String login, String password) {
        this.login = login;
        this.password = password;
    }
}