package com.theezyArt.theezyArtPortfolio.config;

import com.theezyArt.theezyArtPortfolio.data.model.IPAddress;
import com.theezyArt.theezyArtPortfolio.data.model.LoginHistory;
import com.theezyArt.theezyArtPortfolio.data.repositories.LoginHistoryRepository;
import com.theezyArt.theezyArtPortfolio.services.IPAddressService;
import com.theezyArt.theezyArtPortfolio.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserDetailsService userDetailService;
    @Autowired
    private LoginHistoryRepository loginHistoryRepository;
    @Autowired
    private IPAddressService ipAddressService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            String userEmail = jwtService.extractUserName(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            //IP Address And login info
            String clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.isEmpty()) {
                clientIp = request.getRemoteAddr();
            }

            IPAddress ipAddressResponse = ipAddressService.geoIPLookup(clientIp);

            LoginHistory history = new LoginHistory();
            history.setEmail(userEmail);
            history.setEndpoint(request.getRequestURI());
            history.setMethod(request.getMethod());
            if (ipAddressResponse != null) {
                history.setIpAddress(ipAddressResponse);
            }
            history.setTimeStamp(LocalDateTime.now());
            loginHistoryRepository.save(history);

            filterChain.doFilter(request, response);
        }
        catch ( ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired. Please log in again.\"}");
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
        }
    }
}