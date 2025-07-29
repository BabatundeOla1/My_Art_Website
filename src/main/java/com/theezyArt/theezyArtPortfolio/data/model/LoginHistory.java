package com.theezyArt.theezyArtPortfolio.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class LoginHistory {
    @Id
    private String id;
    private String email;
    private String method;
    private String endpoint;
    private IPAddress ipAddress;
    private LocalDateTime timeStamp;
}
