package com.theezyArt.theezyArtPortfolio.utils.exceptions;

public class JWTIsEmptyException extends RuntimeException{

    public JWTIsEmptyException(String message){
        super(message);
    }
}
