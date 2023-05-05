package com.dhbw.unternehmenb.ssp.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(FirebaseAuthFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            verifyToken(request);
        }catch (Exception ex){
            logger.atError().log("could not verify Token");
            logger.atError().log(ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) throws Exception {
        FirebaseToken decodedToken = null;
        String token = getToken(request);

        if (token != null && !token.equalsIgnoreCase("undefined")) {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        }

        if (decodedToken != null) {
            /*if (Objects.equals(decodedToken.getClaims().get("aud"), "se-unternehmenb")) {
                //TODO: we can also check if the user from jwt is in db, may be not applicable to register-endpoint
            }*/
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(decodedToken.getUid(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.atError().log("Token verfied");
        }else
            logger.atError().log("Token not verfied");
    }
    public String getToken(HttpServletRequest request) {
        String token = null;
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        return token;
    }
}
