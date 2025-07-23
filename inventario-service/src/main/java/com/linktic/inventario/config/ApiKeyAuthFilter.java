package com.linktic.inventario.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String header = "x-api-key";

    /**
     * TODO add encryptation to apikey
     */
    @Value("${spring.security.api-key}")
    private String EXPECTED_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(header);

        //if apikey is correct, authenticate user
        if (EXPECTED_KEY.equals(apiKey)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("apiKeyUser", null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
