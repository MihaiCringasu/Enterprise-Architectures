package com.example.vros.security;

import com.example.vros.fleet.Driver;
import com.example.vros.fleet.DriverRepository;
import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordHashService passwordHashService;
    private final JwtService jwtService;

    public AuthController(AppUserRepository userRepository,
                          DriverRepository driverRepository,
                          PasswordHashService passwordHashService,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.passwordHashService = passwordHashService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest req) {
        AppUser user = userRepository.findByUsername(req.username())
                .orElse(null);

        if (user == null || !passwordHashService.matches(req.password(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("error", "Credențiale invalide"));
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthDto.AuthResponse(token, user.getUsername(), user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto.AuthResponse> register(@RequestBody AuthDto.RegisterRequest req) {
        if (req.username() == null || req.username().isBlank() || req.password() == null || req.password().length() < 4) {
            throw new IllegalArgumentException("Username și parolă (min 4 caractere) sunt obligatorii");
        }
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username-ul există deja");
        }

        Driver driver = null;
        if (req.driverId() != null) {
            driver = driverRepository.findById(req.driverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + req.driverId()));
        }

        Role role = req.role() != null ? req.role() : Role.DISPATCHER;
        AppUser saved = userRepository.save(new AppUser(
                req.username(),
                passwordHashService.hashPassword(req.password()),
                role,
                driver
        ));

        String token = jwtService.generateToken(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthDto.AuthResponse(token, saved.getUsername(), saved.getRole().name()));
    }
}
