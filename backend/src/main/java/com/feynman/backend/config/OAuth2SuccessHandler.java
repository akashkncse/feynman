package com.feynman.backend.config;

import com.feynman.backend.model.AuthorizedUser;
import com.feynman.backend.repository.AuthorizedUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthorizedUserRepository repository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
        throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        Optional<AuthorizedUser> user = repository.findByEmail(email);

        if (user.isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized user");
            return;
        }
        System.out.println("Authorized!");
    }
}
