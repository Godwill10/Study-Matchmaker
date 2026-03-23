package com.studymatchmaker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        System.out.println("\n==== JWT FILTER START ====");
        System.out.println("PATH: " + path);

        // ✅ SKIP PUBLIC ENDPOINTS
        if (path.startsWith("/api/auth") || path.startsWith("/api/reference")) {
            System.out.println("PUBLIC ENDPOINT - SKIPPING AUTH");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER: " + authHeader);

        // ❌ NO TOKEN
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO VALID AUTH HEADER - CONTINUING WITHOUT AUTH");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String username = null;
        try {
            username = jwtService.extractUsername(token);
            System.out.println("USERNAME FROM TOKEN: " + username);
        } catch (Exception e) {
            System.out.println("ERROR PARSING TOKEN: " + e.getMessage());
        }

        // ✅ VALIDATE USER
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                System.out.println("AUTHENTICATION SET SUCCESSFULLY ✅");

            } else {
                System.out.println("TOKEN INVALID ❌");
            }
        }

        System.out.println("FINAL AUTH OBJECT: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("==== JWT FILTER END ====\n");

        filterChain.doFilter(request, response);
    }
}