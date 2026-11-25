package com.example.vros.fleet;

import com.example.vros.security.AppUser;
import com.example.vros.security.AppUserRepository;
import com.example.vros.shared.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final AppUserRepository userRepository;

    public DriverController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> getMyDriverProfile(HttpServletRequest request) {
        String username = (String) request.getAttribute("authUsername");
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getDriver() == null) {
            throw new ResourceNotFoundException("Acest cont nu este asociat unui șofer.");
        }

        Driver d = user.getDriver();
        return Map.of(
                "id", d.getId(),
                "name", d.getName(),
                "email", d.getEmail(),
                "phone", d.getPhone()
        );
    }
}
