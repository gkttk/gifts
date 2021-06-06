package com.epam.esm.security.filters;

import com.epam.esm.domain.exceptions.GiftApplicationException;
import com.epam.esm.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter gets an access token from a request and validate it.
 * If token is not expired and valid, user will be authenticated.
 *
 * @since 4.0
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    public JwtTokenAuthenticationFilter(JwtTokenProvider tokenProvider, HandlerExceptionResolver resolver) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenProvider.resolveToken(request);
            if (token != null && tokenProvider.validateAccessToken(token)) {
                Authentication authentication = tokenProvider.getAuthenticationWithToken(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (GiftApplicationException ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }
}
