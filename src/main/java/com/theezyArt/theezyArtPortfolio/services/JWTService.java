package com.theezyArt.theezyArtPortfolio.services;

import com.theezyArt.theezyArtPortfolio.utils.exceptions.JWTIsEmptyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import jakarta.annotation.PostConstruct;


@Service
public class JWTService {

    @Value("${jwt.secret:}")
    private String SECRET_KEY;

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> ClaimsResolver){
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }


    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        // Add role claim as plain string
//        extractClaims.put("role", userDetails.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList());

        extractClaims.put("role", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", ""))
                .toList());


        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("role");
        if (roles instanceof List<?> roleList) {
            return roleList.stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }
    public  boolean isTokenValid(String token, UserDetails userDetails){        
        try {            
            final String username = extractUserName(token);            
            boolean isUsernameMatch = username.equals(userDetails.getUsername());            
            boolean isNotExpired = !isTokenExpired(token);            
            System.out.println("JWTService: Token validation - username match: " + isUsernameMatch + ", not expired: " + isNotExpired);            
            return isUsernameMatch && isNotExpired;        
        } catch (Exception e) {            
            System.out.println("JWTService: Token validation failed: " + e.getMessage());            
            return false;        
        }
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Key getSignInKey() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            System.err.println("JWT Secret Key is not configured!");
            throw new JWTIsEmptyException("JWT Secret Key is not configured");
        }
        System.out.println("JWT Secret loaded successfully, length: " + SECRET_KEY.length());
        System.out.println("JWT Secret starts with: " + (SECRET_KEY.length() > 10 ? SECRET_KEY.substring(0, 10) : SECRET_KEY));
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        System.out.println("JWT Secret decoded successfully, byte length: " + keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("JWTService initializing...");
            System.out.println("JWT Secret configuration status: " + (SECRET_KEY != null && !SECRET_KEY.isEmpty() ? "Configured" : "Not configured"));
            if (SECRET_KEY != null && !SECRET_KEY.isEmpty()) {
                System.out.println("JWT Secret length: " + SECRET_KEY.length());
            }
        } catch (Exception e) {
            System.err.println("Error during JWTService initialization: " + e.getMessage());
        }
    }
}
