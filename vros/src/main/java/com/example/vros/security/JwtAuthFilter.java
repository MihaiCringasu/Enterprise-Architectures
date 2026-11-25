package com.example.vros.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Rute publice: pagină web live, stream SSE, autentificare și documentație Swagger
        if (path.equals("/") || path.equals("/index.html")
                || path.startsWith("/api/auth")
                || path.startsWith("/api/routes/stream")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Lipsește header-ul Authorization: Bearer <token>\"}");
            return;
        }

        JwtService.JwtClaims claims = jwtService.validateAndExtract(authHeader.substring(7));
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Token JWT invalid sau expirat\"}");
            return;
        }

        // Salvăm utilizatorul autentificat în request pentru controllere
        request.setAttribute("authUsername", claims.username());
        request.setAttribute("authRole", claims.role());

        // Role-Based Access Control (RBAC):
        // 1. Ștergerile (DELETE) sunt permise doar pentru ADMIN
        if ("DELETE".equalsIgnoreCase(method) && claims.role() != Role.ADMIN) {
            sendForbidden(response, "Doar rolul ADMIN poate șterge resurse.");
            return;
        }

        // 2. Crearea/Modificarea (POST/PUT) este permisă doar pentru ADMIN și DISPATCHER (nu DRIVER)
        if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) && claims.role() == Role.DRIVER) {
            sendForbidden(response, "Rolul DRIVER nu are permisiunea de a modifica datele flotei/clienților.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"" + message + "\"}");
    }
}
