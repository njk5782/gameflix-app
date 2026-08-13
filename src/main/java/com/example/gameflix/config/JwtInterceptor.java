package com.example.gameflix.config;

import com.example.gameflix.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public JwtInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        if ("GET".equals(request.getMethod())
                && request.getRequestURI().startsWith("/api/movies")) {
            return true;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            writeError(response, 401, "Login required");
            return false;
        }

        try {
            jwtService.readToken(authorization.substring(7));
            return true;
        } catch (Exception exception) {
            writeError(response, 401, "Invalid or expired token");
            return false;
        }
    }

    private void writeError(
            HttpServletResponse response,
            int status,
            String message
    ) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
