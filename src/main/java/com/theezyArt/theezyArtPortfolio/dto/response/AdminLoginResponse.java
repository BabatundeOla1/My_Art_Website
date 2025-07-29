package com.theezyArt.theezyArtPortfolio.dto.response;

import lombok.Data;

@Data
public class AdminLoginResponse {
    private String message;
    private Object data;
    private String accessToken;

}
