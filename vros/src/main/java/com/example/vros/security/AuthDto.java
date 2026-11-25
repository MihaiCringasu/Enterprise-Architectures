package com.example.vros.security;

public class AuthDto {
    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(String username, String password, Role role, Long driverId) {}
    public record AuthResponse(String token, String username, String role) {}
}
