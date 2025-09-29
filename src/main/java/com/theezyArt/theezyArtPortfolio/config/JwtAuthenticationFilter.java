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
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    import java.io.IOException;
    import java.time.LocalDateTime;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;


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
    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("JwtAuthenticationFilter: Processing request to " + requestURI);
        
        // Skip JWT processing for debug and saveArtwork endpoints to avoid conflicts
        if (requestURI.startsWith("/api/debug")) {
            System.out.println("JwtAuthenticationFilter: Skipping JWT processing for endpoint: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("JwtAuthenticationFilter: Authorization header present: " + (authHeader != null));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("JwtAuthenticationFilter: No valid Bearer token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

            System.out.println("JwtAuthenticationFilter: Bearer token found, length: " + authHeader.length());
            String jwt = authHeader.substring(7);
            System.out.println("JwtAuthenticationFilter: Extracted JWT, length: " + jwt.length());
            try {
                System.out.println("JwtAuthenticationFilter: Attempting to extract username from JWT");
                String userEmail = jwtService.extractUserName(jwt);
                System.out.println("JwtAuthenticationFilter: Extracted username: " + userEmail);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("JwtAuthenticationFilter: Loading user details for: " + userEmail);
                    UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);
                    System.out.println("JwtAuthenticationFilter: User details loaded, checking token validity");

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        // extract roles directly from token
                        System.out.println("JwtAuthenticationFilter: Token is valid, extracting roles");
                        var roles = jwtService.extractRoles(jwt);
                        System.out.println("JwtAuthenticationFilter: Extracted roles: " + roles);
                        var authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .toList();

                        UsernamePasswordAuthenticationToken authToken = new 
                        UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("JwtAuthenticationFilter: Authentication set in security context");
                    }

                }

                //IP Address And login info
                System.out.println("JwtAuthenticationFilter: Recording login history");
                String clientIp = request.getHeader("X-Forwarded-For");
                if (clientIp == null || clientIp.isEmpty()) {
                    clientIp = request.getRemoteAddr();
                }
                System.out.println("JwtAuthenticationFilter: Client IP: " + clientIp);

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
                System.out.println("JwtAuthenticationFilter: Login history saved");


            } 
            catch (ExpiredJwtException e){
                System.out.println("JwtAuthenticationFilter: Token expired: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired. Please log in again.\"}");
                return;
            } 
            catch (Exception e){
                System.out.println("JwtAuthenticationFilter: Invalid token: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token: " + e.getMessage() + "\"}");
                return;
            }
            filterChain.doFilter(request, response);
        }
    }
