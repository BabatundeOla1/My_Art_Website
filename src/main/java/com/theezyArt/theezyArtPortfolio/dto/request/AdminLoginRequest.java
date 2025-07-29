package com.theezyArt.theezyArtPortfolio.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AdminLoginRequest {
    private String email;
    private String password;

}
